package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.models.Relationship;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
@Transactional
public class RelationshipDAO extends GeneralDAO<Relationship> {

    private static final String GET_RELATIONSHIP_BY_USERS_ID
            = "SELECT * FROM RELATIONSHIP WHERE USER_FROM = :userIdFrom AND USER_TO = :userIdTo";

    private static final String GET_FRIENDS_RELATIONSHIP_BY_USERS_ID
            = "SELECT * FROM RELATIONSHIP WHERE (USER_FROM = :firstUserId AND USER_TO = :secondUserId) " +
            "OR (USER_FROM = :secondUserId AND USER_TO = :firstUserId) AND STATUS ='FRIENDS'";

    private static final String GET_USER_FRIENDS_COUNT
            = "SELECT COUNT(*) FROM RELATIONSHIP WHERE (USER_FROM = :userId OR USER_TO = :userId) AND " +
            "STATUS = 'FRIENDS'";

    private static final String GET_OUTCOME_REQUESTS_COUNT
            = "SELECT COUNT(*) FROM RELATIONSHIP WHERE USER_FROM = :userId AND STATUS = 'REQUEST_SENT'";

    public RelationshipDAO() {
        setTypeParameterOfClass(Relationship.class);
    }

    public int getUserFriendsCount(long userId) throws InternalServerError {
        return getByUserId(userId, GET_USER_FRIENDS_COUNT);
    }

    public int getOutcomeRequestsCount(long userId) throws InternalServerError {
        return getByUserId(userId, GET_OUTCOME_REQUESTS_COUNT);
    }

    private int getByUserId(long userId, String sqlRequest) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(sqlRequest);
            query.setParameter("userId", userId);
            return Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            throw new InternalServerError("Getting is failed");
        }
    }

    public Relationship getRelationshipByUsersId(long userIdFrom, long userIdTo) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(GET_RELATIONSHIP_BY_USERS_ID, Relationship.class);
            query.setParameter("userIdFrom", userIdFrom);
            query.setParameter("userIdTo", userIdTo);
            return (Relationship) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new InternalServerError("Getting is failed");
        }
    }

    public Relationship getFriendRelationshipByUsersId(long firstUserId, long secondUserId) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(GET_FRIENDS_RELATIONSHIP_BY_USERS_ID, Relationship.class);
            query.setParameter("firstUserId", firstUserId);
            query.setParameter("secondUserId", secondUserId);

            if (query.getResultList().size() == 0) {
                return null;
            }

            return (Relationship) query.getResultList().get(0);
        } catch (Exception e) {
            throw new InternalServerError("Getting is failed");
        }
    }

    @Override
    public Relationship save(Relationship obj) throws InternalServerError {
        return super.save(obj);
    }

    @Override
    public void delete(long id) throws InternalServerError {
        super.delete(id);
    }

    @Override
    public Relationship update(Relationship obj) throws InternalServerError {
        return super.update(obj);
    }

    @Override
    public Relationship findById(long id) throws InternalServerError {
        return super.findById(id);
    }
}
