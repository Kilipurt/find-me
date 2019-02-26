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

    public RelationshipDAO() {
        setTypeParameterOfClass(Relationship.class);
    }

    public Relationship getRelationshipByUsersId(long userIdFrom, long userIdTo) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(GET_RELATIONSHIP_BY_USERS_ID, Relationship.class);
            query.setParameter("userIdFrom", userIdFrom);
            query.setParameter("userIdTo", userIdTo);
            return (Relationship) query.getSingleResult();
        }catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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
