package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDAO extends GeneralDAO<User> {

    public UserDAO() {
        setTypeParameterOfClass(User.class);
    }

    @Override
    public User save(User obj) throws InternalServerError {
        return super.save(obj);
    }

    @Override
    public void delete(long id) throws InternalServerError {
        super.delete(id);
    }

    @Override
    public User update(User obj) throws InternalServerError {
        return super.update(obj);
    }

    @Override
    public User findById(long id) throws InternalServerError {
        return super.findById(id);
    }
}
