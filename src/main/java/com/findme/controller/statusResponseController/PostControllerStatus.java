package com.findme.controller.statusResponseController;

import com.findme.exception.BadRequestException;
import com.findme.models.Post;
import com.findme.models.PostFilter;
import com.findme.models.User;
import com.findme.service.PostService;
import com.findme.util.PostJsonUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Log4j
public class PostControllerStatus {

    private PostService postService;
    private PostJsonUtil postJsonUtil;

    @Autowired
    public PostControllerStatus(PostService postService, PostJsonUtil postJsonUtil) {
        this.postService = postService;
        this.postJsonUtil = postJsonUtil;
    }

    @RequestMapping(path = "/load", method = RequestMethod.GET)
    public ResponseEntity<String> getFeed(HttpSession session, @RequestParam(value = "offset") String offset)
            throws Exception {
        log.info("PostController getFeed method. Selecting news posts");

        User loggedInUser = (User) session.getAttribute("user");

        List<Post> posts = postService.getFriendsPostsWithOffset(loggedInUser.getId(), Long.parseLong(offset));
        return new ResponseEntity<>(postJsonUtil.getJsonFromList(posts), HttpStatus.OK);
    }

    @RequestMapping(path = "/add-post", method = RequestMethod.POST)
    public ResponseEntity<String> addPost(@RequestBody Post post) throws Exception {
        log.info("PostController addPost method. Adding new post");
        postService.save(post);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @RequestMapping(path = "/get-posts", method = RequestMethod.GET)
    public ResponseEntity<String> getPosts(@RequestBody PostFilter postFilter) throws Exception {
        log.info("PostController getPosts method. Selecting posts by filter");
        List<Post> posts = postService.getPostsByFilter(postFilter);
        return new ResponseEntity<>(postJsonUtil.getJsonFromList(posts), HttpStatus.OK);
    }

    @RequestMapping(path = "/update-post", method = RequestMethod.PUT)
    public ResponseEntity<String> updatePost(HttpSession session, @RequestBody Post post) throws Exception {
        log.info("PostController updatePost method. Updating post");

        User loggedInUser = (User) session.getAttribute("user");

        if (!loggedInUser.getId().equals(post.getUserPosted().getId())) {
            log.error("PostController updatePost method. Logged in user does not have enough rights");
            throw new BadRequestException("Logged in user does not have enough rights");
        }

        postService.update(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/delete-post", method = RequestMethod.DELETE)
    public ResponseEntity<String> deletePost(HttpSession session, @RequestParam String postId,
                                             @RequestParam String userPostedId) throws Exception {
        log.info("PostController deletePost method. Deleting post");

        User loggedInUser = (User) session.getAttribute("user");

        if (!loggedInUser.getId().equals(Long.parseLong(userPostedId))) {
            log.error("PostController deletePost method. Logged in user does not have enough rights");
            throw new BadRequestException("Logged in user does not have enough rights");
        }

        postService.delete(Long.parseLong(postId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}