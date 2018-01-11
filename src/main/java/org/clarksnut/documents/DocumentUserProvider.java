package org.clarksnut.documents;

import org.clarksnut.models.SpaceModel;
import org.clarksnut.models.UserModel;

public interface DocumentUserProvider {

    /**
     * @param documentId unique identity generated by the system
     * @return document
     */
    DocumentUserModel getDocumentUser(UserModel user, String documentId);

    /**
     * @return list of documents
     */
    SearchResultModel<DocumentUserModel> getDocumentsUser(UserModel user, DocumentUserQueryModel query, SpaceModel... space);

}