package org.openfact.documents;

import com.fasterxml.jackson.databind.JsonNode;
import org.openfact.documents.exceptions.PreexistedDocumentException;
import org.openfact.documents.exceptions.UnreadableDocumentException;
import org.openfact.documents.exceptions.UnsupportedDocumentTypeException;
import org.openfact.files.XmlUBLFileModel;

import java.util.List;

public interface DocumentProvider {

    /**
     * @param file that contains xml file to be persisted
     */
    DocumentModel addDocument(XmlUBLFileModel file, DocumentProviderType providerType)
            throws UnsupportedDocumentTypeException, UnreadableDocumentException, PreexistedDocumentException;

    /**
     * @param id unique identity generated by the system
     * @return document
     */
    DocumentModel getDocument(String id);

    /**
     * @param type               document type
     * @param assignedId         document assigned id
     * @param supplierAssignedId supplier assigned id
     * @return document
     */
    DocumentModel getDocument(String type, String assignedId, String supplierAssignedId);

    /**
     * @param document document to be removed
     * @return true if document was removed
     */
    boolean removeDocument(DocumentModel document);

    /**
     * @return list of documents
     */
    List<DocumentModel> getDocuments(String nativeQuery);

    /**
     * @return list of documents
     */
    List<DocumentModel> getDocuments(JsonNode json);
}
