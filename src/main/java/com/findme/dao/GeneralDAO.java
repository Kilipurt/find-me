package com.findme.dao;

import com.findme.exception.InternalServerError;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public abstract class GeneralDAO<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private Logger logger = Logger.getLogger(GeneralDAO.class);

    private Class<T> typeParameterOfClass;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setTypeParameterOfClass(Class<T> typeParameterOfClass) {
        this.typeParameterOfClass = typeParameterOfClass;
    }

    public T save(T obj) throws InternalServerError {
        logger.info("GeneralDAO save() method. Saving " + typeParameterOfClass.toString());

        try {
            entityManager.persist(obj);
            return obj;
        } catch (Exception e) {
            logger.error("Saving is failed");
            throw new InternalServerError("Saving is failed");
        }
    }

    public void delete(long id) throws InternalServerError {
        logger.info("GeneralDAO updateDateDeleted() method. Deleting " + typeParameterOfClass.toString());

        try {
            entityManager.remove(entityManager.find(typeParameterOfClass, id));
        } catch (Exception e) {
            logger.error("Deleting obj " + id + " is failed");
            throw new InternalServerError("Deleting obj " + id + " is failed");
        }
    }

    public T update(T obj) throws InternalServerError {
        logger.info("GeneralDAO update() method. Updating " + typeParameterOfClass.toString());

        try {
            entityManager.merge(obj);
            return obj;
        } catch (Exception e) {
            logger.error("Updating is failed");
            throw new InternalServerError("Updating is failed");
        }
    }

    public T findById(long id) throws InternalServerError {
        logger.info("GeneralDAO findById method. Searching " + typeParameterOfClass.toString() + " by id " + id);

        try {
            return entityManager.find(typeParameterOfClass, id);
        } catch (Exception e) {
            logger.error("Searching is failed");
            throw new InternalServerError("Searching is failed");
        }
    }
}
