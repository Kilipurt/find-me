package com.findme.service;

import com.findme.dao.PostDAO;
import com.findme.dao.RelationshipDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private PostDAO postDAO;
    private RelationshipDAO relationshipDAO;

    private Logger logger = Logger.getLogger(PostService.class);

    @Autowired
    public PostService(PostDAO postDAO, RelationshipDAO relationshipDAO) {
        this.postDAO = postDAO;
        this.relationshipDAO = relationshipDAO;
    }

    public List<Post> getFriendsPostsWithOffset(long loggedInUserId, long offset)
            throws InternalServerError, BadRequestException {

        logger.info("PostService getFriendsPostsWithOffset method. Selecting friends posts for user " + loggedInUserId
                + " with offset " + offset);

        if (offset < 0) {
            logger.error("PostService getFriendsPostsWithOffset method. Wrong offset " + offset);
            throw new BadRequestException("Wrong offset " + offset);
        }

        return postDAO.getFriendsPostWithOffset(loggedInUserId, offset);
    }

    public List<Post> getPostsByFilter(PostFilter postFilter)
            throws InternalServerError, BadRequestException {

        logger.info("PostService getPostsByFilter method. Selecting posts by filter");

        if (postFilter.getType().equals(PostsFiltrationType.BY_FRIENDS)) {
            return getPostsByFriends(postFilter.getLoggedInUserId(), postFilter.getUserPagePostedId());
        }

        if (postFilter.getType().equals(PostsFiltrationType.BY_PAGE_OWNER)) {
            return getPostsByPageOwner(postFilter.getUserPostedId());
        }

        if (postFilter.getType().equals(PostsFiltrationType.BY_USER)) {
            return getPostsByUserPosted(postFilter.getUserPostedId(), postFilter.getUserPagePostedId());
        }

        return getPostByPage(postFilter.getUserPagePostedId());
    }

    public List<Post> getPostByPage(long userPagePostedId) throws InternalServerError, BadRequestException {
        logger.info("PostService getPostByPage method. Selecting posts by page " + userPagePostedId);

        if (userPagePostedId <= 0) {
            logger.error("PostService getPostByPage method. Wrong id " + userPagePostedId);
            throw new BadRequestException("Wrong id " + userPagePostedId);
        }

        List<Post> posts = postDAO.getPostsByPage(userPagePostedId);
        posts.sort(new PostedDateComparator());
        Collections.reverse(posts);

        return posts;
    }

    public List<Post> getPostsByFriends(long loggedInUserId, long userPagePostedId)
            throws InternalServerError, BadRequestException {

        logger.info("PostService getPostsByFriends method. Selecting posts by friends of user " + loggedInUserId
                + " on page " + userPagePostedId);

        if (userPagePostedId <= 0) {
            logger.error("PostService getPostsByFriends method. Wrong id " + userPagePostedId);
            throw new BadRequestException("Wrong id " + userPagePostedId);
        }

        List<Post> posts = postDAO.getPostsByFriends(loggedInUserId, userPagePostedId);
        posts.sort(new PostedDateComparator());
        Collections.reverse(posts);

        return posts;
    }

    public List<Post> getPostsByPageOwner(long pageOwnerId) throws InternalServerError, BadRequestException {
        logger.info("PostService getPostsByPageOwner method. Selecting posts by page owner " + pageOwnerId);

        if (pageOwnerId <= 0) {
            logger.error("PostService getPostsByPageOwner method. Wrong id " + pageOwnerId);
            throw new BadRequestException("Wrong id " + pageOwnerId);
        }

        List<Post> posts = postDAO.getPostsByPageOwner(pageOwnerId);
        posts.sort(new PostedDateComparator());
        Collections.reverse(posts);

        return posts;
    }

    public List<Post> getPostsByUserPosted(long userPostedId, long userPagePostedId)
            throws InternalServerError, BadRequestException {
        logger.info("PostService getPostsByUserPosted method. Selecting posts by user posted " + userPostedId
                + "on page " + userPagePostedId);

        if (userPostedId <= 0) {
            logger.error("PostService getPostsByUserPosted method. Wrong id " + userPostedId);
            throw new BadRequestException("Wrong id " + userPostedId);
        }

        List<Post> posts = postDAO.getPostsByUserPosted(userPostedId, userPagePostedId);
        posts.sort(new PostedDateComparator());
        Collections.reverse(posts);

        return posts;
    }

    public Post save(Post post) throws InternalServerError, BadRequestException {
        logger.info("PostService save method. Saving post");
        validateForSave(post);
        post.setDatePosted(new Date());
        return postDAO.save(post);
    }

    public Post update(Post post) throws InternalServerError, BadRequestException {
        logger.info("PostService update method. Updating post");

        if (post.getId() == null || findById(post.getId()) == null) {
            logger.error("PostService update method. Post was not found");
            throw new BadRequestException("Post was not found");
        }

        if (post.getMessage().length() > 200) {
            logger.error("PostService update method. Max length for post is 200 characters");
            throw new BadRequestException("Max length for post is 200 characters");
        }

        return postDAO.update(post);
    }

    public void delete(long id) throws InternalServerError, BadRequestException {
        logger.info("PostService delete method. Deleting post " + id);

        if (id <= 0) {
            logger.error("PostService delete method. Wrong enter id " + id);
            throw new BadRequestException("Wrong enter id " + id);
        }

        postDAO.delete(id);
    }

    public Post findById(long id) throws BadRequestException, InternalServerError {
        logger.info("PostService findById method. Searching post " + id);

        if (id <= 0) {
            logger.error("PostService findById method. Wrong enter id " + id);
            throw new BadRequestException("Wrong enter id " + id);
        }

        return postDAO.findById(id);
    }

    private void validateForSave(Post post) throws BadRequestException, InternalServerError {
        logger.info("PostService validateForSave method. Validating for saving post");

        if (post.getMessage().length() > 200) {
            logger.error("PostService validateForSave method. Max length for post is 200 characters");
            throw new BadRequestException("Max length for post is 200 characters");
        }

        long firstUserId = post.getUserPosted().getId();
        long secondUserId = post.getUserPagePosted().getId();

        if (firstUserId != secondUserId) {

            Relationship relationship = relationshipDAO.getFriendRelationshipByUsersId(firstUserId, secondUserId);

            if (relationship == null) {
                logger.error("PostService validateForSave method. User can not write post on not friend user's page");
                throw new BadRequestException("User can not write post on not friend user's page");
            }
        }
    }
}
