package com.findme.service;

import com.findme.dao.PostDAO;
import com.findme.dao.RelationshipDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private PostDAO postDAO;
    private RelationshipDAO relationshipDAO;

    @Autowired
    public PostService(PostDAO postDAO, RelationshipDAO relationshipDAO) {
        this.postDAO = postDAO;
        this.relationshipDAO = relationshipDAO;
    }

    public List<Post> getPostByPage(long userPagePostedId) throws InternalServerError, BadRequestException {
        if (userPagePostedId <= 0) {
            throw new BadRequestException("Wrong id " + userPagePostedId);
        }

        List<Post> posts = postDAO.getPostsByPage(userPagePostedId);
        posts.sort(new PostedDateComparator());
        Collections.reverse(posts);

        return posts;
    }

    public List<Post> getPostsByFriends(long loggedInUserId, long userPagePostedId) throws InternalServerError, BadRequestException {
        if (userPagePostedId <= 0) {
            throw new BadRequestException("Wrong id " + userPagePostedId);
        }

        if (loggedInUserId <= 0) {
            throw new BadRequestException("Wrong id " + loggedInUserId);
        }

        List<Post> posts = postDAO.getPostsByFriends(loggedInUserId, userPagePostedId);
        posts.sort(new PostedDateComparator());
        Collections.reverse(posts);

        return posts;
    }

    public List<Post> getPostsByPageOwner(long pageOwnerId) throws InternalServerError, BadRequestException {
        if (pageOwnerId <= 0) {
            throw new BadRequestException("Wrong id " + pageOwnerId);
        }

        List<Post> posts = postDAO.getPostsByPageOwner(pageOwnerId);
        posts.sort(new PostedDateComparator());
        Collections.reverse(posts);

        return posts;
    }

    public List<Post> getPostsByUserPosted(long userPostedId, long userPagePostedId) throws InternalServerError, BadRequestException {
        if (userPostedId <= 0) {
            throw new BadRequestException("Wrong id " + userPostedId);
        }

        List<Post> posts = postDAO.getPostsByUserPosted(userPostedId, userPagePostedId);
        posts.sort(new PostedDateComparator());
        Collections.reverse(posts);

        return posts;
    }

    public Post save(Post post) throws InternalServerError, BadRequestException {
        validateForSave(post);
        post.setDatePosted(new Date());
        return postDAO.save(post);
    }

    public Post update(Post post) throws InternalServerError, BadRequestException {
        validateForUpdate(post);

        Post postForUpdating = findById(post.getId());
        post.setMessage(post.getMessage());
        post.setUsersTagged(post.getUsersTagged());
        post.setLocation(post.getLocation());

        return postDAO.update(postForUpdating);
    }

    public void delete(long id) throws InternalServerError, BadRequestException {
        if (id <= 0) {
            throw new BadRequestException("Wrong enter id " + id);
        }

        postDAO.delete(id);
    }

    public Post findById(long id) throws BadRequestException, InternalServerError {
        if (id <= 0) {
            throw new BadRequestException("Wrong enter id " + id);
        }

        return postDAO.findById(id);
    }

    private void validateForUpdate(Post post) throws BadRequestException, InternalServerError {
        if (post == null) {
            throw new BadRequestException("Post was not created");
        }

        if (post.getId() == null || findById(post.getId()) == null) {
            throw new BadRequestException("Post was not found");
        }

        if (post.getMessage().length() > 200) {
            throw new BadRequestException("Max length for post is 200 characters");
        }
    }

    private void validateForSave(Post post) throws BadRequestException, InternalServerError {
        if (post == null) {
            throw new BadRequestException("Post was not created");
        }

        if (post.getMessage().length() > 200) {
            throw new BadRequestException("Max length for post is 200 characters");
        }

        long firstUserId = post.getUserPosted().getId();
        long secondUserId = post.getUserPagePosted().getId();

        if (firstUserId != secondUserId) {

            Relationship relationship = relationshipDAO.getRelationshipByUsersId(firstUserId, secondUserId);
            relationship = relationship == null ? relationshipDAO.getRelationshipByUsersId(secondUserId, firstUserId) : relationship;

            if (relationship == null || !relationship.getStatus().equals(RelationshipStatus.FRIENDS.toString())) {
                throw new BadRequestException("User can not write post in not friend user's page");
            }
        }
    }
}
