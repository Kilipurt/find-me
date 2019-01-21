package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.models.Post;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class PostDAO extends GeneralDAO<Post> {

    @PersistenceContext
    private EntityManager entityManager;

    public PostDAO() {
        setEntityManager(entityManager);
        setTypeParameterOfClass(Post.class);
    }

    @Override
    public Post save(Post obj) throws InternalServerError {
        return super.save(obj);
    }

    @Override
    public void delete(long id) throws InternalServerError {
        super.delete(id);
    }

    @Override
    public Post update(Post obj) throws InternalServerError {
        return super.update(obj);
    }

    @Override
    public Post findById(long id) {
        return super.findById(id);
    }
}
