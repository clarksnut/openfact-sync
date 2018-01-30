package org.clarksnut.documents.jpa;

import org.clarksnut.documents.IndexedDocumentProvider;
import org.clarksnut.documents.jpa.IndexedManagerType.Type;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;

@Stateless
public class IndexedDocumentProviderFactory {

    @Produces
    public IndexedDocumentProvider getIndexedDocumentProvider(@IndexedManagerType(type = Type.ELASTICSEARCH) IndexedDocumentProvider es,
                                                              @IndexedManagerType(type = Type.LUCENE) IndexedDocumentProvider lucene) {
        String indexManager = System.getenv("HIBERNATE_INDEX_MANAGER");
        if (indexManager.equalsIgnoreCase("elasticsearch")) {
            return es;
        } else if (indexManager.equalsIgnoreCase("directory-based")) {
            return lucene;
        } else {
            throw new IllegalStateException("Invalid HIBERNATE_INDEX_MANAGER:" + indexManager);
        }
    }
}
