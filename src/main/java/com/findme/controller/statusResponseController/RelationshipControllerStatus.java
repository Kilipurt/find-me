package com.findme.controller.statusResponseController;

import com.findme.models.Relationship;
import com.findme.models.User;
import com.findme.service.RelationshipService;
import com.findme.util.JsonUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Log4j
public class RelationshipControllerStatus {
    private RelationshipService relationshipService;
    private JsonUtil jsonUtil;

    @Autowired
    public RelationshipControllerStatus(RelationshipService relationshipService, JsonUtil jsonUtil) {
        this.relationshipService = relationshipService;
        this.jsonUtil = jsonUtil;
    }

    @RequestMapping(path = "/add-relationship", method = RequestMethod.POST)
    public ResponseEntity<String> addRelationship(HttpSession session, @RequestParam String userIdTo) throws Exception {
        log.info("RelationshipController addRelationship method. Adding relationship");

        User loggedInUser = (User) session.getAttribute("user");

        relationshipService.save(loggedInUser.getId(), Long.parseLong(userIdTo));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/update-relationship", method = RequestMethod.PUT)
    public ResponseEntity<String> updateRelationship(@RequestParam String userIdTo, @RequestParam String status,
                                                     HttpSession session) throws Exception {
        log.info("RelationshipController updateRelationship method. Updating relationship");

        User loggedInUser = (User) session.getAttribute("user");

        relationshipService.update(loggedInUser.getId(), Long.parseLong(userIdTo), status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/get-relationship", method = RequestMethod.GET)
    public ResponseEntity<String> getRelationshipByIds(HttpSession session, @RequestParam String userId)
            throws Exception {
        User loggedInUser = (User) session.getAttribute("user");

        long userToId = Long.parseLong(userId);

        Relationship relationship = relationshipService.getRelationshipByUsersId(loggedInUser.getId(), userToId);
        return new ResponseEntity<>(jsonUtil.toJson(relationship), HttpStatus.OK);
    }
}