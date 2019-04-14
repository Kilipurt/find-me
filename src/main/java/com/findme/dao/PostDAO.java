package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.models.Post;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class PostDAO extends GeneralDAO<Post> {

    private static final String POST_BY_USER_PAGE_POSTED
            = "SELECT * FROM POST " +
            "WHERE POST.USER_PAGE_POSTED = :userPagePostedId";

    private static final String POSTS_BY_PAGE_OWNER
            = "SELECT * FROM POST " +
            "WHERE POST.USER_POSTED = POST.USER_PAGE_POSTED " +
            "AND POST.USER_PAGE_POSTED = :pageOwnerId " +
            "AND POST.USER_POSTED = :pageOwnerId";

    private static final String POSTS_BY_USER_POSTED
            = "SELECT * FROM POST " +
            "WHERE POST.USER_POSTED = :userPostedId " +
            "AND POST.USER_PAGE_POSTED = :userPagePostedId";

    private static final String POSTS_BY_FRIENDS
            = "SELECT * FROM POST P " +
            "WHERE P.USER_PAGE_POSTED = :userPagePostedId " +
            "AND P.USER_POSTED IN " +
            "(SELECT R.USER_FROM FROM RELATIONSHIP R " +
            "WHERE R.USER_TO = :loggedInUserId " +
            "AND R.STATUS = 'FRIENDS') " +
            "OR P.USER_POSTED IN " +
            "(SELECT R.USER_TO FROM RELATIONSHIP R " +
            "WHERE R.USER_FROM = :loggedInUserId " +
            "AND R.STATUS = 'FRIENDS')";

    public PostDAO() {
        setTypeParameterOfClass(Post.class);
    }

    public List<Post> getPostsByPage(long userPagePostedId) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(POST_BY_USER_PAGE_POSTED, Post.class);
            query.setParameter("userPagePostedId", userPagePostedId);

            return query.getResultList();
        } catch (Exception e) {
            throw new InternalServerError("Getting is failed");
        }
    }

    public List<Post> getPostsByPageOwner(long pageOwnerId) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(POSTS_BY_PAGE_OWNER, Post.class);
            query.setParameter("pageOwnerId", pageOwnerId);

            return query.getResultList();
        } catch (Exception e) {
            throw new InternalServerError("Getting is failed");
        }
    }

    public List<Post> getPostsByUserPosted(long userPostedId, long userPagePostedId) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(POSTS_BY_USER_POSTED, Post.class);
            query.setParameter("userPostedId", userPostedId);
            query.setParameter("userPagePostedId", userPagePostedId);

            return query.getResultList();
        } catch (Exception e) {
            throw new InternalServerError("Getting is failed");
        }
    }

    public List<Post> getPostsByFriends(long loggedInUserId, long userPagePostedId) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(POSTS_BY_FRIENDS, Post.class);
            query.setParameter("loggedInUserId", loggedInUserId);
            query.setParameter("userPagePostedId", userPagePostedId);

            return query.getResultList();
        } catch (Exception e) {
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
