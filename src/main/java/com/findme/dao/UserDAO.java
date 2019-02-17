package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
@Transactional
public class UserDAO extends GeneralDAO<User> {

    private static final String GET_USER_BY_PHONE = "SELECT * FROM USERS WHERE PHONE = :phone";

    public UserDAO() {
        setTypeParameterOfClass(User.class);
    }

    public User getUserByPhone(String phone) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(GET_USER_BY_PHONE, User.class);
            query.setParameter("phone", phone);

            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new InternalServerError("Getting is failed");
        }
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
