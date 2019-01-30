package com.findme.dao;

import com.findme.exception.DbException;
import com.findme.exception.InternalServerError;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public abstract class GeneralDAO<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private Class<T> typeParameterOfClass;

    public void setTypeParameterOfClass(Class<T> typeParameterOfClass) {
        this.typeParameterOfClass = typeParameterOfClass;
    }

    public T save(T obj) throws InternalServerError, DbException {
        try {
            entityManager.persist(obj);
            return obj;
        } catch (RuntimeException e) {
            throw new InternalServerError("Saving is failed");
        } catch (Exception e) {
            throw new DbException("Database was unavailable now. Try again later");
        }
    }

    public void delete(long id) throws InternalServerError, DbException {
        try {
            entityManager.remove(entityManager.find(typeParameterOfClass, id));
        } catch (RuntimeException e) {
            throw new InternalServerError("Deleting obj " + id + " is failed");
        } catch (Exception e) {
            throw new DbException("Database was unavailable now. Try again later");
        }
    }

    public T update(T obj) throws InternalServerError, DbException {
        try {
            entityManager.merge(obj);
            return obj;
        } catch (RuntimeException e) {
            throw new InternalServerError("Updating is failed");
        } catch (Exception e) {
            throw new DbException("Database was unavailable now. Try again later");
        }
    }

    public T findById(long id) throws DbException {
        try {
            return entityManager.find(typeParameterOfClass, id);
        } catch (Exception e) {
            throw new DbException("Database was unavailable now. Try again later");
        }
    }
}
