package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.UnauthorizedException;
import com.findme.models.Post;
import com.findme.models.PostFilter;
import com.findme.models.User;
import com.findme.service.PostService;
import com.findme.util.PostJsonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class PostControllerStatusResponse {

    private PostService postService;
    private PostJsonUtil postJsonUtil;
    private Logger logger = Logger.getLogger(PostControllerStatusResponse.class);

    @Autowired
    public PostControllerStatusResponse(PostService postService, PostJsonUtil postJsonUtil) {
        this.postService = postService;
        this.postJsonUtil = postJsonUtil;
    }

    @RequestMapping(path = "/load", method = RequestMethod.GET)
    public ResponseEntity<String> getFeed(
            HttpSession session,
            @RequestParam(value = "offset") String offset
    ) throws Exception {
        logger.info("PostController getFeed method. Selecting news posts");

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            logger.error("PostController getFeed method. User is not authorized");
            throw new UnauthorizedException("User is not authorized");
        }

        List<Post> posts = postService.getFriendsPostsWithOffset(loggedInUser.getId(), Long.parseLong(offset));
        return new ResponseEntity<>(postJsonUtil.getJsonFromList(posts), HttpStatus.OK);
    }

    @RequestMapping(path = "/add-post", method = RequestMethod.POST)
    public ResponseEntity<String> addPost(
            HttpSession session,
            @ModelAttribute Post post
    ) throws Exception {
        logger.info("PostController addPost method. Adding new post");

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            logger.error("PostController addPost method. User is not authorized");
            throw new UnauthorizedException("User is not authorized");
        }

        postService.save(post);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @RequestMapping(path = "/get-posts", method = RequestMethod.GET)
    public ResponseEntity<String> getPosts(
            HttpSession session,
            @ModelAttribute PostFilter postFilter
    ) throws Exception {
        logger.info("PostController getPosts method. Selecting posts by filter");

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            logger.error("PostController getPosts method. User is not authorized");
            throw new UnauthorizedException("User is not authorized");
        }

        List<Post> posts = postService.getPostsByFilter(postFilter);
        return new ResponseEntity<>(postJsonUtil.getJsonFromList(posts), HttpStatus.OK);
    }

    @RequestMapping(path = "/update-post", method = RequestMethod.PUT)
    public ResponseEntity<String> updatePost(
            HttpSession session,
            @ModelAttribute Post post
    ) throws Exception {
        logger.info("PostController updatePost method. Updating post");

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            logger.error("PostController updatePost method. User is not authorized");
            throw new UnauthorizedException("User is not authorized");
        }

        if (!loggedInUser.getId().equals(post.getUserPosted().getId())) {
            logger.error("PostController updatePost method. Logged in user does not have enough rights");
            throw new BadRequestException("Logged in user does not have enough rights");
        }

        postService.update(post);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/delete-post", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePost(
            HttpSession session,
            @RequestParam(value = "postId") String postId,
            @RequestParam(value = "userPostedId") String userPostedId
    ) throws Exception {
        logger.info("PostController deletePost method. Deleting post");

        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            logger.error("PostController deletePost method. User is not authorized");
            throw new UnauthorizedException("User is not authorized");
        }

        if (!loggedInUser.getId().equals(Long.parseLong(userPostedId))) {
            logger.error("PostController deletePost method. Logged in user does not have enough rights");
            throw new BadRequestException("Logged in user does not have enough rights");
        }

        postService.delete(Long.parseLong(postId));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
