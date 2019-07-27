package com.findme.controller.statusResponseController;

import com.findme.models.User;
import com.findme.service.RelationshipService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Log4j
public class RelationshipControllerStatus {
    private RelationshipService relationshipService;

    @Autowired
    public RelationshipControllerStatus(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @RequestMapping(path = "/add-relationship", method = RequestMethod.POST)
    public ResponseEntity<String> addRelationship(HttpSession session, @RequestParam String userIdTo) throws Exception {
        log.info("RelationshipController addRelationship method. Adding relationship");

        User loggedInUser = (User) session.getAttribute("user");

        relationshipService.save(loggedInUser.getId(), Long.parseLong(userIdTo));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/update-relationship", method = RequestMethod.PUT)
    public ResponseEntity<String> updateRelationship(HttpSession session, @RequestParam String userIdFrom,
                                                     @RequestParam String userIdTo, @RequestParam String status)
            throws Exception {
        log.info("RelationshipController updateRelationship method. Updating relationship");

        User loggedInUser = (User) session.getAttribute("user");

        relationshipService.update(Long.parseLong(userIdFrom), Long.parseLong(userIdTo), status, loggedInUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/get-relationship", method = RequestMethod.GET)
    public ResponseEntity<String> getRelationshipByIds(HttpSession session, @RequestParam String userId, Model model)
            throws Exception {
        User loggedInUser = (User) session.getAttribute("user");

        long userToId = Long.parseLong(userId);

        model.addAttribute("relationship", relationshipService.getRelationshipByUsersId(loggedInUser.getId(), userToId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}