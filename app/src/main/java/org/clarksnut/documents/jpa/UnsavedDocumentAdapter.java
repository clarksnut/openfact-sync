package org.clarksnut.documents.jpa;

import org.clarksnut.common.jpa.JpaModel;
import org.clarksnut.documents.UnsavedDocumentModel;
import org.clarksnut.documents.UnsavedReasonType;
import org.clarksnut.documents.jpa.entity.UnsupportedDocumentEntity;

import javax.persistence.EntityManager;

public class UnsavedDocumentAdapter implements UnsavedDocumentModel, JpaModel<UnsupportedDocumentEntity> {

    private final EntityManager em;
    private final UnsupportedDocumentEntity document;

    public UnsavedDocumentAdapter(EntityManager em, UnsupportedDocumentEntity document) {
        this.em = em;
        this.document = document;
    }

    public static UnsupportedDocumentEntity toEntity(UnsavedDocumentModel model, EntityManager em) {
        if (model instanceof UnsavedDocumentAdapter) {
            return ((UnsavedDocumentAdapter) model).getEntity();
        }
        return em.getReference(UnsupportedDocumentEntity.class, model.getId());
    }

    @Override
    public UnsupportedDocumentEntity getEntity() {
        return document;
    }

    @Override
    public String getId() {
        return document.getId();
    }

    @Override
    public String getType() {
        return document.getType();
    }

    @Override
    public String getFileId() {
        return document.getFileId();
    }

    @Override
    public String getFileProvider() {
        return document.getFileProvider();
    }

    @Override
    public UnsavedReasonType getReason() {
        return document.getReason();
    }


}
