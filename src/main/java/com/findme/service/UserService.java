package com.findme.service;

import com.findme.dao.UserDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User save(User user) throws BadRequestException, InternalServerError {
        validate(user);
        user.setDateRegistered(new Date());
        return userDAO.save(user);
    }

    public User update(User user) throws InternalServerError, BadRequestException {
        if (user.getId() <= 0) {
            throw new BadRequestException("Wrong enter id " + user.getId());
        }

        return userDAO.update(user);
    }

    public void delete(long id) throws InternalServerError, BadRequestException {
        if (id <= 0) {
            throw new BadRequestException("Wrong enter id " + id);
        }

        userDAO.delete(id);
    }

    public User findById(long id) throws BadRequestException, InternalServerError {
        if (id <= 0) {
            throw new BadRequestException("Wrong enter id " + id);
        }

        return userDAO.findById(id);
    }

    private void validate(User user) throws BadRequestException, InternalServerError {
        if (userDAO.getUserByPhone(user.getPhone()) != null) {
            throw new BadRequestException("User with phone " + user.getPhone() + " already registered");
        }

        if (user.getFirstName().isEmpty()) {
            throw new BadRequestException("First name " + user.getFirstName() + " is wrong");
        }

        if (user.getLastName().isEmpty()) {
            throw new BadRequestException("Last name " + user.getLastName() + " is wrong");
        }

        if (user.getCountry().isEmpty()) {
            throw new BadRequestException("Country " + user.getCountry() + " is wrong");
        }

        if (user.getCity().isEmpty()) {
            throw new BadRequestException("City " + user.getCity() + " is wrong");
        }

        if (user.getAge() <= 0) {
            throw new BadRequestException("Age " + user.getAge() + " is wrong");
        }
    }
}
