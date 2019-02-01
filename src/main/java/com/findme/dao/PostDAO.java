package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.models.Post;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PostDAO extends GeneralDAO<Post> {

    public PostDAO() {
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
    public Post findById(long id) throws InternalServerError {
        return super.findById(id);
    }
}
