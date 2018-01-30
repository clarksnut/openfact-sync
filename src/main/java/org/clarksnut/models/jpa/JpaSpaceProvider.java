package org.clarksnut.models.jpa;

import org.clarksnut.models.*;
import org.clarksnut.models.jpa.entity.CollaboratorEntity;
import org.clarksnut.models.jpa.entity.SpaceEntity;
import org.clarksnut.models.utils.ClarksnutModelUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Stateless
public class JpaSpaceProvider implements SpaceProvider {

    private final static String[] SEARCH_FIELDS = {SpaceModel.ASSIGNED_ID, SpaceModel.NAME};
    private final static Function<String, String> FIELD_MAPPER = modelFieldName -> {
        switch (modelFieldName) {
            case SpaceModel.ASSIGNED_ID:
                return "assignedId";
            case SpaceModel.NAME:
                return "name";
            default:
                return modelFieldName;
        }
    };

    @PersistenceContext
    private EntityManager em;

    @Override
    public SpaceModel addSpace(String assignedId, String name) {
        SpaceEntity entity = new SpaceEntity();
        entity.setId(ClarksnutModelUtils.generateId());
        entity.setAssignedId(assignedId);
        entity.setName(name);
        em.persist(entity);
        em.flush();

        return new SpaceAdapter(em, entity);
    }

    @Override
    public SpaceModel getSpace(String id) {
        SpaceEntity entity = em.find(SpaceEntity.class, id);
        if (entity == null) return null;
        return new SpaceAdapter(em, entity);
    }

    @Override
    public SpaceModel getByAssignedId(String assignedId) {
        TypedQuery<SpaceEntity> query = em.createNamedQuery("getSpaceByAssignedId", SpaceEntity.class);
        query.setParameter("assignedId", assignedId);
        List<SpaceEntity> entities = query.getResultList();
        if (entities.size() == 0) return null;
        return new SpaceAdapter(em, entities.get(0));
    }

    @Override
    public boolean removeSpace(SpaceModel space) {
        SpaceEntity entity = em.find(SpaceEntity.class, space.getId());
        if (entity == null) return false;

        em.createNamedQuery("deleteCollaboratorsBySpaceId").setParameter("spaceId", space.getId()).executeUpdate();
        em.remove(entity);
        return true;
    }

    @Override
    public List<SpaceModel> getSpaces(UserModel user) {
        return getSpaces(user, -1, -1);
    }

    @Override
    public List<SpaceModel> getSpaces(UserModel user, int offset, int limit, PermissionType... role) {
        if (role == null || role.length == 0) {
            role = PermissionType.values();
        }

        TypedQuery<CollaboratorEntity> query = em.createNamedQuery("getCollaboratorsByUserIdAndRole", CollaboratorEntity.class);
        query.setParameter("userId", user.getId());
        query.setParameter("role", Arrays.asList(role));

        if (offset != -1) query.setFirstResult(offset);
        if (limit != -1) query.setMaxResults(limit);

        return query.getResultList().stream()
                .map(CollaboratorEntity::getSpace)
                .map(f -> new SpaceAdapter(em, f))
                .collect(Collectors.toList());
    }

    @Override
    public int countSpaces(UserModel user, PermissionType... role) {
        if (role == null || role.length == 0) {
            role = PermissionType.values();
        }

        TypedQuery<Long> query = em.createNamedQuery("countCollaboratorsByUserIdAndRole", Long.class);
        query.setParameter("userId", user.getId());
        query.setParameter("role", Arrays.asList(role));

        return query.getSingleResult().intValue();
    }

    @Override
    public List<SpaceModel> getSpaces(QueryModel query) {
        TypedQuery<SpaceEntity> typedQuery = new JpaCriteria<>(em, SpaceEntity.class, SpaceEntity.class, query, SEARCH_FIELDS, FIELD_MAPPER).buildTypedQuery();
        return typedQuery.getResultList().stream()
                .map(f -> new SpaceAdapter(em, f))
                .collect(Collectors.toList());
    }
}
