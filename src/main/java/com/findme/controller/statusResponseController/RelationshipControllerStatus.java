package com.findme.controller.statusResponseController;

import com.findme.exception.BadRequestException;
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
public class RelationshipControllerStatus {
    private RelationshipService relationshipService;
    private Logger logger = Logger.getLogger(RelationshipControllerStatus.class);

    @Autowired
    public RelationshipControllerStatus(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @RequestMapping(path = "/add-relationship", method = RequestMethod.POST)
    public ResponseEntity<String> addRelationship(HttpSession session, @RequestParam(name = "userIdFrom")
            String userIdFrom, @RequestParam(name = "userIdTo") String userIdTo) throws Exception {
        logger.info("RelationshipController addRelationship method. Adding relationship");

        User loggedInUser = (User) session.getAttribute("user");

        if (Long.parseLong(userIdFrom) != loggedInUser.getId()) {
            logger.error("RelationshipController addRelationship method. User has not enough rights");
            throw new BadRequestException("User has not enough rights");
        }

        relationshipService.save(Long.parseLong(userIdFrom), Long.parseLong(userIdTo));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/update-relationship", method = RequestMethod.PUT)
    public ResponseEntity<String> updateRelationship(HttpSession session, @RequestParam(value = "userIdFrom")
            String userIdFrom, @RequestParam(value = "userIdTo") String userIdTo, @RequestParam(value = "status")
            String status) throws Exception {
        logger.info("RelationshipController updateRelationship method. Updating relationship");

        User loggedInUser = (User) session.getAttribute("user");

        relationshipService.update(Long.parseLong(userIdFrom), Long.parseLong(userIdTo), status, loggedInUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
