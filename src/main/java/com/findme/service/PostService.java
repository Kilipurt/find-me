package com.findme.service;

import com.findme.dao.PostDAO;
import com.findme.dao.RelationshipDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Log4j
public class PostService {

    private PostDAO postDAO;
    private RelationshipDAO relationshipDAO;

    @Autowired
    public PostService(PostDAO postDAO, RelationshipDAO relationshipDAO) {
        this.postDAO = postDAO;
        this.relationshipDAO = relationshipDAO;
    }

    public List<Post> getFriendsPostsWithOffset(long loggedInUserId, long offset)
            throws InternalServerError, BadRequestException {

        log.info("PostService getFriendsPostsWithOffset method. Selecting friends posts for user " + loggedInUserId
                + " with offset " + offset);

        if (offset < 0) {
            log.error("PostService getFriendsPostsWithOffset method. Wrong offset " + offset);
            throw new BadRequestException("Wrong offset " + offset);
        }

        return postDAO.getFriendsPostWithOffset(loggedInUserId, offset);
    }

    public List<Post> getPostsByFilter(PostFilter postFilter)
            throws InternalServerError, BadRequestException {

        log.info("PostService getPostsByFilter method. Selecting posts by filter");

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
        log.info("PostService getPostByPage method. Selecting posts by page " + userPagePostedId);

        if (userPagePostedId <= 0) {
            log.error("PostService getPostByPage method. Wrong id " + userPagePostedId);
            throw new BadRequestException("Wrong id " + userPagePostedId);
        }

        return postDAO.getPostsByPage(userPagePostedId);
    }

    public List<Post> getPostsByFriends(long loggedInUserId, long userPagePostedId)
            throws InternalServerError, BadRequestException {

        log.info("PostService getPostsByFriends method. Selecting posts by friends of user " + loggedInUserId
                + " on page " + userPagePostedId);

        if (userPagePostedId <= 0) {
            log.error("PostService getPostsByFriends method. Wrong id " + userPagePostedId);
            throw new BadRequestException("Wrong id " + userPagePostedId);
        }

        return postDAO.getPostsByFriends(loggedInUserId, userPagePostedId);
    }

    public List<Post> getPostsByPageOwner(long pageOwnerId) throws InternalServerError, BadRequestException {
        log.info("PostService getPostsByPageOwner method. Selecting posts by page owner " + pageOwnerId);

        if (pageOwnerId <= 0) {
            log.error("PostService getPostsByPageOwner method. Wrong id " + pageOwnerId);
            throw new BadRequestException("Wrong id " + pageOwnerId);
        }

        return postDAO.getPostsByPageOwner(pageOwnerId);
    }

    public List<Post> getPostsByUserPosted(long userPostedId, long userPagePostedId)
            throws InternalServerError, BadRequestException {
        log.info("PostService getPostsByUserPosted method. Selecting posts by user posted " + userPostedId
                + "on page " + userPagePostedId);

        if (userPostedId <= 0) {
            log.error("PostService getPostsByUserPosted method. Wrong id " + userPostedId);
            throw new BadRequestException("Wrong id " + userPostedId);
        }

        return postDAO.getPostsByUserPosted(userPostedId, userPagePostedId);
    }

    public Post save(Post post) throws InternalServerError, BadRequestException {
        log.info("PostService save method. Saving post");
        validateForSave(post);
        post.setDatePosted(new Date());
        return postDAO.save(post);
    }

    public Post update(Post post) throws InternalServerError, BadRequestException {
        log.info("PostService update method. Updating post");

        if (post.getId() == null || findById(post.getId()) == null) {
            log.error("PostService update method. Post was not found");
            throw new BadRequestException("Post was not found");
        }

        if (post.getMessage().length() > 200) {
            log.error("PostService update method. Max length for post is 200 characters");
            throw new BadRequestException("Max length for post is 200 characters");
        }

        return postDAO.update(post);
    }

    public void delete(long id) throws InternalServerError, BadRequestException {
        log.info("PostService deleteSingleMessage method. Deleting post " + id);

        if (id <= 0) {
            log.error("PostService deleteSingleMessage method. Wrong enter id " + id);
            throw new BadRequestException("Wrong enter id " + id);
        }

        postDAO.delete(id);
    }

    public Post findById(long id) throws BadRequestException, InternalServerError {
        log.info("PostService findById method. Searching post " + id);

        if (id <= 0) {
            log.error("PostService findById method. Wrong enter id " + id);
            throw new BadRequestException("Wrong enter id " + id);
        }

        return postDAO.findById(id);
    }

    private void validateForSave(Post post) throws BadRequestException, InternalServerError {
        log.info("PostService validateForSave method. Validating for saving post");

        if (post.getMessage().length() > 200) {
            log.error("PostService validateForSave method. Max length for post is 200 characters");
            throw new BadRequestException("Max length for post is 200 characters");
        }

        long firstUserId = post.getUserPosted().getId();
        long secondUserId = post.getUserPagePosted().getId();

        if (firstUserId != secondUserId) {

            Relationship relationship = relationshipDAO.getFriendRelationshipByUsersId(firstUserId, secondUserId);

            if (relationship == null) {
                log.error("PostService validateForSave method. User can not write post on not friend user's page");
                throw new BadRequestException("User can not write post on not friend user's page");
            }
        }
    }
}
