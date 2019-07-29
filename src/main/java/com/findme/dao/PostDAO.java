package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.models.Post;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
@Log4j
public class PostDAO extends GeneralDAO<Post> {

    private static final String POST_BY_USER_PAGE_POSTED
            = "SELECT * FROM POST WHERE USER_PAGE_POSTED = :userPagePostedId ORDER BY DATE_POSTED DESC";

    private static final String POSTS_BY_PAGE_OWNER = "SELECT * FROM POST WHERE USER_POSTED = USER_PAGE_POSTED AND " +
            "USER_PAGE_POSTED = :pageOwnerId AND USER_POSTED = :pageOwnerId ORDER BY DATE_POSTED DESC";

    private static final String POSTS_BY_USER_POSTED = "SELECT * FROM POST WHERE USER_POSTED = :userPostedId AND " +
            "USER_PAGE_POSTED = :userPagePostedId ORDER BY DATE_POSTED DESC";

    private static final String POSTS_BY_FRIENDS = "SELECT * FROM POST P WHERE P.USER_PAGE_POSTED = :userPagePostedId " +
            "AND P.USER_POSTED IN (SELECT R.USER_FROM FROM RELATIONSHIP R WHERE R.USER_TO = :loggedInUserId AND " +
            "R.STATUS = 'FRIENDS') OR P.USER_POSTED IN (SELECT R.USER_TO FROM RELATIONSHIP R WHERE " +
            "R.USER_FROM = :loggedInUserId AND R.STATUS = 'FRIENDS') ORDER BY P.DATE_POSTED DESC";

    private static final String OLDER_FRIENDS_POST = "SELECT * FROM POST P WHERE P.USER_PAGE_POSTED = P.USER_POSTED AND " +
            "P.USER_POSTED IN (SELECT R.USER_FROM FROM RELATIONSHIP R WHERE R.USER_TO = :loggedInUserId AND " +
            "R.STATUS = 'FRIENDS') OR P.USER_POSTED IN (SELECT R.USER_TO FROM RELATIONSHIP R WHERE " +
            "R.USER_FROM = :loggedInUserId AND R.STATUS = 'FRIENDS') ORDER BY P.DATE_POSTED DESC OFFSET :offset ROWS " +
            "FETCH NEXT :postsNumber ROWS ONLY";

    public PostDAO() {
        setTypeParameterOfClass(Post.class);
    }

    public List<Post> getFriendsPostWithOffset(long loggedInUserId, long offset) throws InternalServerError {
        log.info("PostDAO getFriendsPostWithOffset method. Selecting friends posts for user " + loggedInUserId
                + " with offset " + offset);

        try {
            Query query = getEntityManager().createNativeQuery(OLDER_FRIENDS_POST, Post.class);
            query.setParameter("loggedInUserId", loggedInUserId);
            query.setParameter("offset", offset);
            query.setParameter("postsNumber", offset + 50);

            return query.getResultList();
        } catch (Exception e) {
            log.error("Getting is failed");
            throw new InternalServerError("Getting is failed");
        }
    }

    public List<Post> getPostsByPage(long userPagePostedId) throws InternalServerError {
        log.info("PostDAO getPostsByPage method. Selecting posts by page " + userPagePostedId);

        try {
            Query query = getEntityManager().createNativeQuery(POST_BY_USER_PAGE_POSTED, Post.class);
            query.setParameter("userPagePostedId", userPagePostedId);

            return query.getResultList();
        } catch (Exception e) {
            log.error("Getting is failed");
            throw new InternalServerError("Getting is failed");
        }
    }

    public List<Post> getPostsByPageOwner(long pageOwnerId) throws InternalServerError {
        log.info("PostDAO getPostsByPageOwner method. Selecting posts by page owner " + pageOwnerId);

        try {
            Query query = getEntityManager().createNativeQuery(POSTS_BY_PAGE_OWNER, Post.class);
            query.setParameter("pageOwnerId", pageOwnerId);

            return query.getResultList();
        } catch (Exception e) {
            log.error("Getting is failed");
            throw new InternalServerError("Getting is failed");
        }
    }

    public List<Post> getPostsByUserPosted(long userPostedId, long userPagePostedId) throws InternalServerError {
        log.info("PostDAO getPostsByUserPosted method. Selecting posts by user posted " + userPostedId + " on page "
                + userPagePostedId);

        try {
            Query query = getEntityManager().createNativeQuery(POSTS_BY_USER_POSTED, Post.class);
            query.setParameter("userPostedId", userPostedId);
            query.setParameter("userPagePostedId", userPagePostedId);

            return query.getResultList();
        } catch (Exception e) {
            log.error("Getting is failed");
            throw new InternalServerError("Getting is failed");
        }
    }

    public List<Post> getPostsByFriends(long loggedInUserId, long userPagePostedId) throws InternalServerError {
        log.info("PostDAO getPostsByFriends method. Selecting posts by friends of user " + loggedInUserId
                + " on page " + userPagePostedId);

        try {
            Query query = getEntityManager().createNativeQuery(POSTS_BY_FRIENDS, Post.class);
            query.setParameter("loggedInUserId", loggedInUserId);
            query.setParameter("userPagePostedId", userPagePostedId);

            return query.getResultList();
        } catch (Exception e) {
            log.error("Getting is failed");
            throw new InternalServerError("Getting is failed");
        }
    }

    @Override
    public Post save(Post obj) throws InternalServerError {
        return super.save(obj);
    }

    @Override
    public void delete(long id) throws InternalServerError {
        super.delete(id);
    }

    @Override
    public Post update(Post obj) throws InternalServerError {
        return super.update(obj);
    }

    @Override
    public Post findById(long id) throws InternalServerError {
        return super.findById(id);
    }
}
