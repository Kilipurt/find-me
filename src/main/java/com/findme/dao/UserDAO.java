package com.findme.dao;

import com.findme.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
//    private static final String GET_FRIENDS = "SELECT U.* FROM USER U, RELATIONSHIP R WHERE " +
//            "(R.USER_FROM = :userId AND U.ID = R.USER_TO) OR (R.USER_FROM = U.ID AND R.USER_TO = :userId) AND " +
//            "R.STATUS = 'FRIENDS'";

    @Query(value = "SELECT * FROM USER WHERE PHONE = ?1", nativeQuery = true)
    User getUserByPhone(String phone);

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

    @Query(value = "SELECT U.* FROM USER U JOIN RELATIONSHIP R ON U.ID = R.USER_FROM WHERE R.USER_TO = ?1 AND " +
            "R.STATUS = 'REQUEST_SENT'", nativeQuery = true)
    List<User> getIncomeRequests(String userId);

    @Query(value = "SELECT U.* FROM USER U JOIN RELATIONSHIP R ON U.ID = R.USER_TO WHERE R.USER_FROM = ?1 AND " +
            "R.STATUS = 'REQUEST_SENT'", nativeQuery = true)
    List<User> getOutcomeRequests(String userId);
}