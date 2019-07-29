package com.findme.dao;

import com.findme.exception.InternalServerError;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Log4j
@Transactional
public abstract class GeneralDAO<T> {

    @PersistenceContext
    @Getter
    private EntityManager entityManager;

    @Setter
    private Class<T> typeParameterOfClass;

    public T save(T obj) throws InternalServerError {
        log.info("GeneralDAO save() method. Saving " + typeParameterOfClass.toString());

        try {
            entityManager.persist(obj);
            return obj;
        } catch (Exception e) {
            log.error("Saving is failed");
            throw new InternalServerError("Saving is failed");
        }
    }

    public void delete(long id) throws InternalServerError {
        log.info("GeneralDAO deleteSingleMessage() method. Deleting " + typeParameterOfClass.toString());

        try {
            entityManager.remove(entityManager.find(typeParameterOfClass, id));
        } catch (Exception e) {
            log.error("Deleting obj " + id + " is failed");
            throw new InternalServerError("Deleting obj " + id + " is failed");
        }
    }

    public T update(T obj) throws InternalServerError {
        log.info("GeneralDAO update() method. Updating " + typeParameterOfClass.toString());

        try {
            entityManager.merge(obj);
            return obj;
        } catch (Exception e) {
            log.error("Updating is failed");
            throw new InternalServerError("Updating is failed");
        }
    }

    public T findById(long id) throws InternalServerError {
        log.info("GeneralDAO findById method. Searching " + typeParameterOfClass.toString() + " by id " + id);

        try {
            return entityManager.find(typeParameterOfClass, id);
        } catch (Exception e) {
            log.error("Searching is failed");
            throw new InternalServerError("Searching is failed");
        }
    }
}
