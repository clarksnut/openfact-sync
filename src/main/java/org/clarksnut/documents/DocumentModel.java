package org.clarksnut.documents;

import java.util.List;

public interface DocumentModel {

    String getId();

    String getType();

    String getAssignedId();

    String getSupplierAssignedId();

    List<DocumentVersionModel> getVersions();
    DocumentVersionModel getCurrentVersion();

    interface DocumentCreationEvent {
        String getDocumentType();

        Object getJaxb();

        DocumentModel getCreatedDocument();
    }

    interface DocumentRemovedEvent {
        DocumentModel getDocument();
    }

}
