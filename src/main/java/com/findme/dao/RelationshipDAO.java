package com.findme.dao;

import com.findme.models.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipDAO extends JpaRepository<Relationship, Long> {

    @Query(value = "SELECT COUNT(*) FROM RELATIONSHIP WHERE (USER_FROM = ?1 OR USER_TO = ?1) AND " +
            "STATUS = 'FRIENDS'", nativeQuery = true)
    int getUserFriendsCount(long userId);

    @Query(value = "SELECT COUNT(*) FROM RELATIONSHIP WHERE USER_FROM = ?1 AND STATUS = 'REQUEST_SENT'",
            nativeQuery = true)
    int getOutcomeRequestsCount(long userId);

    @Query(value = "SELECT * FROM RELATIONSHIP WHERE (USER_FROM = ?1 AND USER_TO = ?2) OR (USER_FROM = ?2 AND " +
            "USER_TO = ?1)", nativeQuery = true)
    Relationship getRelationshipByUsersId(long userIdFrom, long userIdTo);

    @Query(value = "SELECT * FROM RELATIONSHIP WHERE (USER_FROM = ?1 AND USER_TO = ?2) OR " +
            "(USER_FROM = ?2 AND USER_TO = ?1) AND STATUS ='FRIENDS'", nativeQuery = true)
    Relationship getFriendRelationshipByUsersId(long firstUserId, long secondUserId);
}
