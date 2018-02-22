package org.clarksnut.documents.jpa;

import org.clarksnut.documents.DocumentModel;
import org.clarksnut.documents.DocumentModel.DocumentCreationEvent;
import org.clarksnut.documents.DocumentModel.DocumentRemovedEvent;
import org.clarksnut.documents.DocumentProvider;
import org.clarksnut.documents.ImportedDocumentModel;
import org.clarksnut.documents.exceptions.AlreadyImportedDocumentException;
import org.clarksnut.documents.jpa.entity.DocumentEntity;
import org.clarksnut.documents.jpa.entity.DocumentVersionEntity;
import org.clarksnut.mapper.document.DocumentMapped.DocumentBean;
import org.clarksnut.models.SpaceModel;
import org.clarksnut.models.jpa.SpaceAdapter;
import org.jboss.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@Stateless
public class JpaDocumentProvider implements DocumentProvider {

    private static final Logger logger = Logger.getLogger(JpaDocumentProvider.class);

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Event<DocumentCreationEvent> creationEvent;

    @Inject
    private Event<DocumentRemovedEvent> removedEvent;

    public static DocumentVersionEntity toDocumentVersionEntity(DocumentBean bean) {
        DocumentVersionEntity entity = new DocumentVersionEntity();

        entity.setAmount(bean.getAmount());
        entity.setTax(bean.getTax());
        entity.setCurrency(bean.getCurrency());
        entity.setIssueDate(bean.getIssueDate());

        entity.setSupplierName(bean.getSupplierName());
        entity.setSupplierStreetAddress(bean.getSupplierStreetAddress());
        entity.setSupplierCity(bean.getSupplierCity());
        entity.setSupplierCountry(bean.getSupplierCountry());

        entity.setCustomerName(bean.getCustomerName());
        entity.setCustomerAssignedId(bean.getCustomerAssignedId());
        entity.setCustomerStreetAddress(bean.getCustomerStreetAddress());
        entity.setCustomerCity(bean.getCustomerCity());
        entity.setCustomerCountry(bean.getCustomerCountry());

        return entity;
    }

    @Override
    public DocumentModel addDocument(String documentType,
                                     ImportedDocumentModel importedDocument,
                                     DocumentBean bean,
                                     SpaceModel supplier,
                                     SpaceModel customer) throws AlreadyImportedDocumentException {
        DocumentModel document = getDocument(documentType, bean.getAssignedId(), supplier);
        if (document == null) {
            DocumentEntity documentEntity = new DocumentEntity();
            documentEntity.setId(UUID.randomUUID().toString());
            documentEntity.setType(documentType);
            documentEntity.setAssignedId(bean.getAssignedId());
            documentEntity.setSupplier(SpaceAdapter.toEntity(supplier, em));
            if (customer != null) {
                documentEntity.setCustomer(SpaceAdapter.toEntity(customer, em));
            }
            em.persist(documentEntity);

            DocumentVersionEntity documentVersionEntity = toDocumentVersionEntity(bean);
            documentVersionEntity.setId(UUID.randomUUID().toString());
            documentVersionEntity.setCurrentVersion(true);
            documentVersionEntity.setDocument(documentEntity);
            documentVersionEntity.setImportedDocument(ImportedDocumentAdapter.toEntity(importedDocument, em));
            em.persist(documentVersionEntity);

            document = new DocumentAdapter(em, documentEntity);
            logger.debug("New Document has been imported");

            final DocumentModel documentCreated = document;
            creationEvent.fire(() -> documentCreated);
        } else {
            long currentChecksum = document.getCurrentVersion().getImportedDocument().getFile().getChecksum();
            if (currentChecksum == importedDocument.getFile().getChecksum()) {
                DocumentVersionEntity documentVersionEntity = toDocumentVersionEntity(bean);
                documentVersionEntity.setId(UUID.randomUUID().toString());
                documentVersionEntity.setCurrentVersion(false);
                documentVersionEntity.setDocument(DocumentAdapter.toEntity(document, em));
                documentVersionEntity.setImportedDocument(ImportedDocumentAdapter.toEntity(importedDocument, em));
                em.persist(documentVersionEntity);

                logger.debug("New Document Version has been imported");
            } else {
                throw new AlreadyImportedDocumentException("Document has already been imported");
            }
        }

        return document;
    }

    @Override
    public DocumentModel getDocument(String id) {
        DocumentEntity entity = em.find(DocumentEntity.class, id);
        if (entity == null) return null;
        return new DocumentAdapter(em, entity);
    }

    @Override
    public DocumentModel getDocument(String type, String assignedId, SpaceModel supplier) {
        TypedQuery<DocumentEntity> typedQuery = em.createNamedQuery("getDocumentByTypeAssignedIdAndSupplierId", DocumentEntity.class);
        typedQuery.setParameter("type", type);
        typedQuery.setParameter("assignedId", assignedId);
        typedQuery.setParameter("supplierId", supplier.getId());

        List<DocumentEntity> resultList = typedQuery.getResultList();
        if (resultList.size() == 1) {
            return new DocumentAdapter(em, resultList.get(0));
        } else if (resultList.size() == 0) {
            return null;
        } else {
            throw new IllegalStateException("Invalid number of results");
        }
    }

    @Override
    public boolean removeDocument(DocumentModel document) {
        DocumentEntity entity = em.find(DocumentEntity.class, document.getId());
        if (entity == null) return false;
        em.remove(entity);
        em.flush();

        removedEvent.fire(() -> document);
        return true;
    }

}
