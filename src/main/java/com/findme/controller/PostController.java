package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.UnauthorizedException;
import com.findme.models.Post;
import com.findme.models.PostFilter;
import com.findme.models.User;
import com.findme.service.PostService;
import com.findme.util.PostJsonUtil;
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

    @Autowired
    public PostController(PostService postService, PostJsonUtil postJsonUtil) {
        this.postService = postService;
        this.postJsonUtil = postJsonUtil;
    }

    @RequestMapping(path = "/feed/{loggedInUserId}", method = RequestMethod.GET)
    public String getFeed(Model model, HttpSession session, @PathVariable() String loggedInUserId) {
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                throw new UnauthorizedException("User is not authorized");
            }

            if (loggedInUser.getId() != Long.parseLong(loggedInUserId)) {
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
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
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
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
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
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
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
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                throw new UnauthorizedException("User is not authorized");
            }

            if (!loggedInUser.getId().equals(post.getUserPosted().getId())) {
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
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                throw new UnauthorizedException("User is not authorized");
            }

            if (!loggedInUser.getId().equals(Long.parseLong(userPostedId))) {
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
