package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class PostController {

    private PostService postService;
    private PostJsonUtil postJsonUtil;
    private Logger logger = Logger.getLogger(PostController.class);

    @Autowired
    public PostController(PostService postService, PostJsonUtil postJsonUtil) {
        this.postService = postService;
        this.postJsonUtil = postJsonUtil;
    }

    @RequestMapping(path = "/feed/{loggedInUserId}", method = RequestMethod.GET)
    public String getFeed(Model model, HttpSession session, @PathVariable() String loggedInUserId) {
        logger.info("PostController getFeed method. Moving to feed page");

        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                logger.error("PostController getFeed method. User is not authorized");
                throw new UnauthorizedException("User is not authorized");
            }

            if (loggedInUser.getId() != Long.parseLong(loggedInUserId)) {
                logger.error("PostController getFeed method. User does not have enough rights");
                throw new BadRequestException("User does not have enough rights");
            }

            return "feed";
        } catch (UnauthorizedException e) {
            model.addAttribute("exception", e);
            return "unauthorizedException";
        } catch (BadRequestException | NumberFormatException e) {
            model.addAttribute("exception", e);
            return "badRequestException";
        }
    }

    @RequestMapping(path = "/load", method = RequestMethod.GET)
    public ResponseEntity<String> getFeed(
            HttpSession session,
            @RequestParam(value = "offset") String offset
    ) {
        logger.info("PostController getFeed method. Selecting news posts");

        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                logger.error("PostController getFeed method. User is not authorized");
                throw new UnauthorizedException("User is not authorized");
            }

            List<Post> posts = postService.getFriendsPostsWithOffset(loggedInUser.getId(), Long.parseLong(offset));
            return new ResponseEntity<>(postJsonUtil.getJsonFromList(posts), HttpStatus.OK);
        } catch (InternalServerError | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/add-post", method = RequestMethod.POST)
    public ResponseEntity<String> addPost(
            HttpSession session,
            @ModelAttribute Post post
    ) {
        logger.info("PostController addPost method. Adding new post");

        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                logger.error("PostController addPost method. User is not authorized");
                throw new UnauthorizedException("User is not authorized");
            }

            postService.save(post);

            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/get-posts", method = RequestMethod.GET)
    public ResponseEntity<String> getPosts(
            HttpSession session,
            @ModelAttribute PostFilter postFilter
    ) {
        logger.info("PostController getPosts method. Selecting posts by filter");

        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                logger.error("PostController getPosts method. User is not authorized");
                throw new UnauthorizedException("User is not authorized");
            }

            List<Post> posts = postService.getPostsByFilter(postFilter);
            return new ResponseEntity<>(postJsonUtil.getJsonFromList(posts), HttpStatus.OK);
        } catch (InternalServerError | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/update-post", method = RequestMethod.PUT)
    public ResponseEntity<String> updatePost(
            HttpSession session,
            @ModelAttribute Post post
    ) {
        logger.info("PostController updatePost method. Updating post");

        try {
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
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/delete-post", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePost(
            HttpSession session,
            @RequestParam(value = "postId") String postId,
            @RequestParam(value = "userPostedId") String userPostedId
    ) {
        logger.info("PostController deletePost method. Deleting post");

        try {
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
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
