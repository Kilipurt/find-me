package com.findme.service;

import com.findme.dao.UserDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private UserDAO userDAO;
    private Logger logger = Logger.getLogger(UserService.class);

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User save(User user) throws BadRequestException, InternalServerError {
        logger.info("UserService save method. Saving user");
        validateUser(user);
        user.setDateRegistered(new Date());
        return userDAO.save(user);
    }

    public User update(User user) throws InternalServerError, BadRequestException {
        logger.info("UserService update method. Updating user");

        if (user.getId() <= 0) {
            logger.error("UserService update method. Wrong enter id " + user.getId());
            throw new BadRequestException("Wrong enter id " + user.getId());
        }

        return userDAO.update(user);
    }

    public void delete(long id) throws InternalServerError, BadRequestException {
        logger.info("UserService updateDateDeleted method. Deleting user");

        if (id <= 0) {
            logger.error("UserService updateDateDeleted method. Wrong enter id " + id);
            throw new BadRequestException("Wrong enter id " + id);
        }

        userDAO.delete(id);
    }

    public User findById(long id) throws BadRequestException, InternalServerError {
        logger.info("UserService findById method. Searching user " + id);

        if (id <= 0) {
            logger.error("UserService findById method. Wrong enter id " + id);
            throw new BadRequestException("Wrong enter id " + id);
        }

        return userDAO.findById(id);
    }

    public User login(String phone, String password) throws InternalServerError, BadRequestException {
        logger.info("UserService login method. Login user with phone " + phone);

        User user = userDAO.getUserByPhone(phone);

        if (user == null) {
            logger.error("UserService login method. User with phone " + phone + " was not found");
            throw new BadRequestException("User with phone " + phone + " was not found");
        }

        if (!user.getPassword().equals(password)) {
            logger.error("UserService login method. Password is wrong");
            throw new BadRequestException("Password is wrong");
        }

        return user;
    }

    public List<User> getIncomeRequests(long userId) throws InternalServerError, BadRequestException {
        logger.info("UserService getIncomeRequests method. Selecting income requests for user " + userId);

        if (userId <= 0) {
            logger.error("UserService getIncomeRequests method. Wrong user's id " + userId);
            throw new BadRequestException("Wrong user's id " + userId);
        }

        return userDAO.getIncomeRequests(userId);
    }

    public List<User> getOutcomeRequests(long userId) throws InternalServerError, BadRequestException {
        logger.info("UserService getOutcomeRequests method. Selecting outcome requests for user " + userId);

        if (userId <= 0) {
            logger.error("UserService getOutcomeRequests method. Wrong user's id " + userId);
            throw new BadRequestException("Wrong user's id " + userId);
        }

        return userDAO.getOutcomeRequests(userId);
    }

    private void validateUser(User user) throws BadRequestException, InternalServerError {
        logger.info("UserService validateUser method. User validating");

        if (userDAO.getUserByPhone(user.getPhone()) != null) {
            logger.error("UserService validateUser method. User with phone " + user.getPhone() + " already registered");
            throw new BadRequestException("User with phone " + user.getPhone() + " already registered");
        }

        if (user.getFirstName().isEmpty()) {
            logger.error("UserService validateUser method. First name " + user.getFirstName() + " is wrong");
            throw new BadRequestException("First name " + user.getFirstName() + " is wrong");
        }

        if (user.getLastName().isEmpty()) {
            logger.error("UserService validateUser method. Last name " + user.getLastName() + " is wrong");
            throw new BadRequestException("Last name " + user.getLastName() + " is wrong");
        }

        if (user.getCountry().isEmpty()) {
            logger.error("UserService validateUser method. Country " + user.getCountry() + " is wrong");
            throw new BadRequestException("Country " + user.getCountry() + " is wrong");
        }

        if (user.getCity().isEmpty()) {
            logger.error("UserService validateUser method. City " + user.getCity() + " is wrong");
            throw new BadRequestException("City " + user.getCity() + " is wrong");
        }

        if (user.getAge() <= 0) {
            logger.error("UserService validateUser method. Age " + user.getAge() + " is wrong");
            throw new BadRequestException("Age " + user.getAge() + " is wrong");
        }
    }
}
