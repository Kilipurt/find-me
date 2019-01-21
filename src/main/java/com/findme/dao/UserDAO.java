package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class UserDAO extends GeneralDAO<User> {

    @PersistenceContext
    private EntityManager entityManager;

    public UserDAO() {
        setEntityManager(entityManager);
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
    public User findById(long id) {
        return super.findById(id);
    }
}
