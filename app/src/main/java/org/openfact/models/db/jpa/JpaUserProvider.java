package org.openfact.models.db.jpa;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.openfact.models.ScrollableResultsModel;
import org.openfact.models.UserModel;
import org.openfact.models.UserProvider;
import org.openfact.models.db.HibernateProvider;
import org.openfact.models.db.jpa.entity.UserEntity;
import org.openfact.models.utils.OpenfactModelUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Stateless
public class JpaUserProvider extends HibernateProvider implements UserProvider {

    private EntityManager em;

    @Inject
    public JpaUserProvider(EntityManager em) {
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public UserModel getByUsername(String username) {
        TypedQuery<UserEntity> query = em.createNamedQuery("getUserByUsername", UserEntity.class);
        query.setParameter("username", username);
        List<UserEntity> entities = query.getResultList();
        if (entities.size() == 0) return null;
        return new UserAdapter(em, entities.get(0));
    }

    @Override
    public UserModel addUser(String username) {
        UserEntity entity = new UserEntity();
        entity.setId(OpenfactModelUtils.generateId());
        entity.setUsername(username);
        entity.setRegistrationCompleted(false);
        em.persist(entity);
        em.flush();
        return new UserAdapter(em, entity);
    }

    @Override
    public List<UserModel> getUsers() {
        TypedQuery<UserEntity> query = em.createNamedQuery("getAllUsers", UserEntity.class);
        return query.getResultList().stream()
                .map(f -> new UserAdapter(em, f))
                .collect(Collectors.toList());
    }

    @Override
    public ScrollableResultsModel<UserModel> getScrollableUsers() {
        ScrollableResults scrollableResults = getSession().createNamedQuery("getAllUsers").scroll(ScrollMode.FORWARD_ONLY);
        Function<UserEntity, UserModel> mapper = entity -> new UserAdapter(em, entity);
        return new ScrollableResultsAdapter<>(scrollableResults, mapper);
    }

}
