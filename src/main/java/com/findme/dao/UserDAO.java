package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.models.User;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
@Log4j
public class UserDAO extends GeneralDAO<User> {

    private static final String GET_USER_BY_PHONE = "SELECT * FROM USERS WHERE PHONE = :phone";

    private static final String GET_INCOME_REQUESTS = "SELECT U.* FROM USERS U JOIN RELATIONSHIP R ON " +
            "U.ID = R.USER_FROM WHERE R.USER_TO = :userId AND R.STATUS = 'REQUEST_SENT'";

    private static final String GET_OUTCOME_REQUESTS = "SELECT U.* FROM USERS U JOIN RELATIONSHIP R ON " +
            "U.ID = R.USER_TO WHERE R.USER_FROM = :userId AND R.STATUS = 'REQUEST_SENT'";

//    private static final String GET_FRIENDS = "SELECT U.* FROM USERS U, RELATIONSHIP R WHERE " +
//            "(R.USER_FROM = :userId AND U.ID = R.USER_TO) OR (R.USER_FROM = U.ID AND R.USER_TO = :userId) AND " +
//            "R.STATUS = 'FRIENDS'";

    public UserDAO() {
        setTypeParameterOfClass(User.class);
    }

    public User getUserByPhone(String phone) throws InternalServerError {
        log.info("UserDAO getUserByPhone method. Selecting user by phone " + phone);
        try {
            Query query = getEntityManager().createNativeQuery(GET_USER_BY_PHONE, User.class);
            query.setParameter("phone", phone);

            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.error("Getting is failed");
            throw new InternalServerError("Getting is failed");
        }
    }

//    public List<User> getFriends(long userId) throws InternalServerError {
//        logger.info("UserDAO getFriends method. Selecting friends of user " + userId);
//
//        try {
//            Query query = getEntityManager().createNativeQuery(GET_FRIENDS, User.class);
//            query.setParameter("userId", userId);
//
//            return query.getResultList();
//        } catch (Exception e) {
//            logger.error("Getting is failed");
//            throw new InternalServerError("Getting is failed");
//        }
//    }

    public List<User> getIncomeRequests(long userId) throws InternalServerError {
        log.info("UserDAO getIncomeRequests method. Selecting income requests of user " + userId);
        return getRequests(GET_INCOME_REQUESTS, userId);
    }

    public List<User> getOutcomeRequests(long userId) throws InternalServerError {
        log.info("UserDAO getOutcomeRequests method. Selecting outcome requests of user " + userId);
        return getRequests(GET_OUTCOME_REQUESTS, userId);
    }

    private List<User> getRequests(String queryToDb, long userId) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(queryToDb, User.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            log.error("Getting is failed");
            throw new InternalServerError("Getting is failed");
        }
    }

    @Override
    public User save(User obj) throws InternalServerError {
        return super.save(obj);
    }

    @Override
    public void delete(long id) throws InternalServerError {
        super.delete(id);
    }

    @Override
    public User update(User obj) throws InternalServerError {
        return super.update(obj);
    }

    @Override
    public User findById(long id) throws InternalServerError {
        return super.findById(id);
    }
}
