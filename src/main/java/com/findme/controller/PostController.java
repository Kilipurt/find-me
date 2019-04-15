package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.UnauthorizedException;
import com.findme.models.Post;
import com.findme.models.User;
import com.findme.service.PostService;
import com.findme.service.UserService;
import com.findme.util.PostJsonUtil;
import com.findme.util.UserParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class PostController {

    private PostService postService;
    private PostJsonUtil postJsonUtil;
    private UserParserUtil userParserUtil;
    private UserService userService;

    @Autowired
    public PostController(PostService postService, PostJsonUtil postJsonUtil, UserParserUtil userParserUtil, UserService userService) {
        this.postService = postService;
        this.postJsonUtil = postJsonUtil;
        this.userParserUtil = userParserUtil;
        this.userService = userService;
    }

    @RequestMapping(path = "/add-post", method = RequestMethod.POST)
    public ResponseEntity<String> addPost(
            HttpSession session,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "usersTagged") String usersTagged,
            @RequestParam(value = "userPosted") String userPosted,
            @RequestParam(value = "userPagePosted") String userPagePosted,
            @RequestParam(value = "location") String location
    ) {
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                throw new UnauthorizedException("User is not authorized");
            }

            Post post = new Post();
            post.setMessage(message);
            post.setUserPosted(userService.findById(Long.parseLong(userPosted)));
            post.setUsersTagged(userParserUtil.parseUsersFromString(usersTagged));
            post.setUserPagePosted(userService.findById(Long.parseLong(userPagePosted)));
            post.setLocation(location);

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

    @RequestMapping(path = "/get-posts-by-page", method = RequestMethod.GET)
    public ResponseEntity<String> getPosts(
            HttpSession session,
            @RequestParam(value = "userPagePostedId") String userPagePostedId
    ) {
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                throw new UnauthorizedException("User is not authorized");
            }

            List<Post> posts = postService.getPostByPage(Long.parseLong(userPagePostedId));
            return new ResponseEntity<>(postJsonUtil.getJsonFromList(posts), HttpStatus.OK);
        } catch (InternalServerError | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/get-posts-by-page-owner", method = RequestMethod.GET)
    public ResponseEntity<String> getPostsByPageOwner(
            HttpSession session,
            @RequestParam(value = "pageOwnerId") String pageOwnerId
    ) {
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                throw new UnauthorizedException("User is not authorized");
            }

            List<Post> posts = postService.getPostsByPageOwner(Long.parseLong(pageOwnerId));
            return new ResponseEntity<>(postJsonUtil.getJsonFromList(posts), HttpStatus.OK);
        } catch (InternalServerError | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/get-posts-by-user-posted", method = RequestMethod.GET)
    public ResponseEntity<String> getPostsByUser(
            HttpSession session,
            @RequestParam(value = "userPostedId") String userPostedId,
            @RequestParam(value = "userPagePostedId") String userPagePostedId
    ) {
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                throw new UnauthorizedException("User is not authorized");
            }

            List<Post> posts = postService.getPostsByUserPosted(Long.parseLong(userPostedId), Long.parseLong(userPagePostedId));
            return new ResponseEntity<>(postJsonUtil.getJsonFromList(posts), HttpStatus.OK);
        } catch (InternalServerError | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/get-posts-by-friends", method = RequestMethod.GET)
    public ResponseEntity<String> getPostsByFriends(
            HttpSession session,
            @RequestParam(value = "loggedInUserId") String loggedInUserId,
            @RequestParam(value = "userPagePostedId") String userPagePostedId
    ) {
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                throw new UnauthorizedException("User is not authorized");
            }

            if (loggedInUser.getId() != Long.parseLong(loggedInUserId)) {
                throw new BadRequestException("Logged in user's id " + loggedInUserId + " is wrong");
            }

            List<Post> posts = postService.getPostsByFriends(Long.parseLong(loggedInUserId), Long.parseLong(userPagePostedId));
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
            @RequestParam(value = "postId") String postId,
            @RequestParam(value = "message") String message,
            @RequestParam(value = "usersTagged") String usersTagged,
            @RequestParam(value = "userPosted") String userPosted,
            @RequestParam(value = "location") String location
    ) {
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                throw new UnauthorizedException("User is not authorized");
            }

            if (!loggedInUser.getId().equals(Long.parseLong(userPosted))) {
                throw new BadRequestException("Logged in user does not have enough rights");
            }

            Post post = new Post();
            post.setId(Long.parseLong(postId));
            post.setMessage(message);
            post.setUsersTagged(userParserUtil.parseUsersFromString(usersTagged));
            post.setLocation(location);

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
