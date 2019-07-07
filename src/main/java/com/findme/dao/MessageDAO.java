package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.models.Message;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MessageDAO extends GeneralDAO<Message> {

    public MessageDAO() {
        setTypeParameterOfClass(Message.class);
    }

    @Override
    public Message save(Message obj) throws InternalServerError {
        return super.save(obj);
    }

    @Override
    public void delete(long id) throws InternalServerError {
        super.delete(id);
    }

    @Override
    public Message update(Message obj) throws InternalServerError {
        return super.update(obj);
    }

    @Override
    public Message findById(long id) throws InternalServerError {
        return super.findById(id);
    }
}
