package com.findme.service;

import com.findme.dao.PostDAO;
import com.findme.exception.BadRequestException;
import com.findme.exception.DbException;
import com.findme.exception.InternalServerError;
import com.findme.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private PostDAO postDAO;

    @Autowired
    public PostService(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    public Post save(Post post) throws InternalServerError, DbException {
        return postDAO.save(post);
    }

    public Post update(Post post) throws InternalServerError, BadRequestException, DbException {
        if (post.getId() <= 0) {
            throw new BadRequestException("Wrong enter id " + post.getId());
        }

        return postDAO.update(post);
    }

    public void delete(long id) throws InternalServerError, BadRequestException, DbException {
        if (id <= 0) {
            throw new BadRequestException("Wrong enter id " + id);
        }

        postDAO.delete(id);
    }

    public Post findById(long id) throws BadRequestException, DbException {
        if (id <= 0) {
            throw new BadRequestException("Wrong enter id " + id);
        }

        return postDAO.findById(id);
    }
}
