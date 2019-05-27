package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.UnauthorizedException;
import com.findme.models.User;
import com.findme.service.RelationshipService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class RelationshipController {
    private RelationshipService relationshipService;
    private Logger logger = Logger.getLogger(RelationshipController.class);

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
        logger.info("RelationshipController addRelationship method. Adding relationship");

        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                logger.error("RelationshipController addRelationship method. User is not authorized");
                throw new UnauthorizedException("User is not authorized");
            }

            if (Long.parseLong(userIdFrom) != loggedInUser.getId()) {
                logger.error("RelationshipController addRelationship method. User has not enough rights");
                throw new BadRequestException("User has not enough rights");
            }

            relationshipService.save(Long.parseLong(userIdFrom), Long.parseLong(userIdTo));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException | NumberFormatException | BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/update-relationship", method = RequestMethod.PUT)
    public ResponseEntity<String> updateRelationship(
            HttpSession session,
            @RequestParam(value = "userIdFrom") String userIdFrom,
            @RequestParam(value = "userIdTo") String userIdTo,
            @RequestParam(value = "status") String status
    ) {
        logger.info("RelationshipController updateRelationship method. Updating relationship");

        try {
            User loggedInUser = (User) session.getAttribute("user");

            if (loggedInUser == null) {
                logger.error("RelationshipController updateRelationship method. User is not authorized");
                throw new UnauthorizedException("User is not authorized");
            }

            relationshipService.update(Long.parseLong(userIdFrom), Long.parseLong(userIdTo), status, loggedInUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException | BadRequestException | NumberFormatException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InternalServerError e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
