package com.findme.service;

import com.findme.dao.UserDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.DbException;
import com.findme.exception.InternalServerError;
import com.findme.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User save(User user) throws InternalServerError, DbException {
        return userDAO.save(user);
    }

    public User update(User user) throws InternalServerError, BadRequestException, DbException {
        if (user.getId() <= 0) {
            throw new BadRequestException("Wrong enter id " + user.getId());
        }

        return userDAO.update(user);
    }

    public void delete(long id) throws InternalServerError, BadRequestException, DbException {
        if (id <= 0) {
            throw new BadRequestException("Wrong enter id " + id);
        }

        userDAO.delete(id);
    }

    public User findById(long id) throws BadRequestException, DbException {
        if (id <= 0) {
            throw new BadRequestException("Wrong enter id " + id);
        }

        return userDAO.findById(id);
    }
}
