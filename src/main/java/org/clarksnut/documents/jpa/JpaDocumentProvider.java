package org.clarksnut.documents.jpa;

import org.apache.commons.io.FileUtils;
import org.clarksnut.documents.*;
import org.clarksnut.documents.DocumentModel.DocumentCreationEvent;
import org.clarksnut.documents.DocumentModel.DocumentRemovedEvent;
import org.clarksnut.documents.exceptions.UnreadableDocumentException;
import org.clarksnut.documents.exceptions.UnrecognizableDocumentTypeException;
import org.clarksnut.documents.exceptions.UnsupportedDocumentTypeException;
import org.clarksnut.documents.jpa.entity.DocumentEntity;
import org.clarksnut.documents.jpa.entity.DocumentUtil;
import org.clarksnut.documents.jpa.entity.DocumentVersionEntity;
import org.clarksnut.files.XmlUBLFileModel;
import org.clarksnut.mapper.document.DocumentMapped;
import org.clarksnut.mapper.document.DocumentMapped.DocumentBean;
import org.clarksnut.mapper.document.DocumentMapperProvider;
import org.clarksnut.mapper.document.DocumentMapperProviderFactory;
import org.jboss.logging.Logger;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
public class JpaDocumentProvider implements DocumentProvider {

    private static final Logger logger = Logger.getLogger(JpaDocumentProvider.class);

    @PersistenceContext
    private EntityManager em;

    @Inject
    @ConfigurationValue("clarksnut.document.mapper.default")
    private Optional<String> clarksnutDocumentMapperDefault;

    @Inject
    @ConfigurationValue("clarksnut.document.additionalTypes")
    private Optional<String[]> clarksnutAdditionalDocumentTypes;

    @Inject
    private Event<DocumentEntity> documentEntityEvent;

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
    public DocumentModel addDocument(ImportedDocumentModel importedDocument, XmlUBLFileModel file)
            throws UnsupportedDocumentTypeException, UnreadableDocumentException, UnrecognizableDocumentTypeException {

        DocumentMapped mappedDocument = readDocument(file);
        if (mappedDocument == null) {
            throw new UnreadableDocumentException(file.getDocumentType() + " Is supported but could not be map");
        }

        final DocumentBean documentBean = mappedDocument.getBean();

        DocumentModel document = getDocument(file.getDocumentType(), documentBean.getAssignedId(), documentBean.getSupplierAssignedId());
        if (document == null) {
            DocumentEntity documentEntity = new DocumentEntity();
            documentEntity.setId(UUID.randomUUID().toString());
            documentEntity.setType(file.getDocumentType());
            documentEntity.setAssignedId(documentBean.getAssignedId());
            documentEntity.setSupplierAssignedId(documentBean.getSupplierAssignedId());
            em.persist(documentEntity);

            DocumentVersionEntity documentVersionEntity = toDocumentVersionEntity(documentBean);
            documentVersionEntity.setId(UUID.randomUUID().toString());
            documentVersionEntity.setImportedFile(ImportedDocumentAdapter.toEntity(importedDocument, em));
            documentVersionEntity.setDocument(documentEntity);
            em.persist(documentVersionEntity);

            document = new DocumentAdapter(em, documentEntity);
        } else {
            byte[] current = document.getCurrentVersion()
                    .getImportedDocument()
                    .getFile()
                    .getFile();
            byte[] newVersion = file.getFile();
            if (!Arrays.equals(current, newVersion)) {
                DocumentVersionEntity documentVersionEntity = toDocumentVersionEntity(documentBean);
                documentVersionEntity.setId(UUID.randomUUID().toString());
                documentVersionEntity.setImportedFile(ImportedDocumentAdapter.toEntity(importedDocument, em));
                documentVersionEntity.setDocument(DocumentAdapter.toEntity(document, em));
                em.persist(documentVersionEntity);
            } else {
                importedDocument.setStatus(ImportedDocumentStatus.ALREADY_IMPORTED);
            }
        }

        importedDocument.setDocumentReferenceId(document.getId());

//        creationEvent.fire(new DocumentCreationEvent() {
//            @Override
//            public String getDocumentType() {
//                return file.getDocumentType();
//            }
//
//            @Override
//            public Object getJaxb() {
//                return mappedDocument.getType();
//            }
//
//            @Override
//            public DocumentModel getCreatedDocument() {
//                return document;
//            }
//        });

        documentEntityEvent.fire(DocumentAdapter.toEntity(document, em));
        return document;
    }

    private DocumentMapped readDocument(XmlUBLFileModel file) throws UnsupportedDocumentTypeException, UnrecognizableDocumentTypeException {
        String group = clarksnutDocumentMapperDefault.orElse("basic");
        DocumentMapperProvider provider = DocumentMapperProviderFactory.getInstance().getParsedDocumentProvider(group, file.getDocumentType());

        DocumentMapped documentMapped = provider.map(file);
        if (documentMapped == null && !group.equals("basic")) {
            provider = DocumentMapperProviderFactory.getInstance().getParsedDocumentProvider(group, file.getDocumentType());
            documentMapped = provider.map(file);
        }
        return documentMapped;
    }

    @Override
    public DocumentModel getDocument(String documentId) {
        DocumentEntity entity = em.find(DocumentEntity.class, documentId);
        if (entity == null) return null;
        return new DocumentAdapter(em, entity);
    }

    @Override
    public DocumentModel getDocument(String type, String assignedId, String supplierAssignedId) {
        TypedQuery<DocumentEntity> typedQuery = em.createNamedQuery("getDocumentByTypeAssignedIdAndSupplierAssignedId", DocumentEntity.class);
        typedQuery.setParameter("type", type);
        typedQuery.setParameter("assignedId", assignedId);
        typedQuery.setParameter("supplierAssignedId", supplierAssignedId);

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
