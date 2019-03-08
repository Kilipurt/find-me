package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.RelationshipStatus;
import com.findme.models.User;
import com.findme.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class RelationshipController {
    private RelationshipService relationshipService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @RequestMapping(path = "/add-relationship", method = RequestMethod.POST)
    public ResponseEntity<String> addRelationship(
            HttpSession session,
            @RequestParam(name = "userIdFrom") String userIdFrom,
            @RequestParam(name = "userIdTo") String userIdTo
    ) {
        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                throw new BadRequestException("User is not authorized");
            }

            if (Long.parseLong(userIdFrom) != loggedInUser.getId()) {
                throw new BadRequestException("User has not enough rights");
            }

            relationshipService.save(Long.parseLong(userIdFrom), Long.parseLong(userIdTo));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException | NumberFormatException | BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/update-relationship", method = RequestMethod.PUT)
    public ResponseEntity<String> updateRelationship(
            HttpSession session,
            @RequestParam(value = "userIdFrom") String userIdFrom,
            @RequestParam(value = "userIdTo") String userIdTo,
            @RequestParam(value = "status") String status
    ) {
        try {
            User loggedInUser = (User) session.getAttribute("user");

            long idFrom = Long.parseLong(userIdFrom);
            long idTo = Long.parseLong(userIdTo);

            validateLoggedInUser(loggedInUser, idFrom, idTo, status);

            relationshipService.update(idFrom, idTo, status);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException | BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateLoggedInUser(User loggedInUser, long userIdFrom, long userIdTo, String status) throws BadRequestException {
        if (loggedInUser == null) {
            throw new BadRequestException("User is not authorized");
        }

        if (loggedInUser.getId() == userIdFrom && !(status.equals(RelationshipStatus.DELETED.toString())
                || status.equals(RelationshipStatus.REQUEST_SENT.toString()))) {
            throw new BadRequestException("User " + loggedInUser.getId() + " has not enough rights");
        }

        if (loggedInUser.getId() == userIdTo && !(status.equals(RelationshipStatus.FRIENDS.toString())
                || status.equals(RelationshipStatus.REQUEST_DECLINED.toString()))) {
            throw new BadRequestException("User " + loggedInUser.getId() + " has not enough rights");
        }

        if (loggedInUser.getId() != userIdTo || loggedInUser.getId() != userIdFrom) {
            throw new BadRequestException("User " + loggedInUser.getId() + " has not enough rights");
        }
    }

}
