package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.models.Message;
import com.findme.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class MessageDAO extends GeneralDAO<Message> {

    private static final String GET_CHATS_BY_USER_ID = "SELECT U.* FROM USERS U WHERE U.ID IN (SELECT M.USER_TO " +
            "FROM MESSAGE M WHERE M.USER_FROM = :loggedInUserId AND M.DATE_DELETED IS NULL AND M.ID NOT IN " +
            "(SELECT DM.MESSAGE_ID FROM DELETED_MESSAGES DM WHERE DM.USER_ID = :loggedInUserId)) OR U.ID IN " +
            "(SELECT M.USER_FROM FROM MESSAGE M WHERE M.USER_TO = :loggedInUserId AND M.DATE_DELETED IS NULL " +
            "AND M.ID NOT IN (SELECT DM.MESSAGE_ID FROM DELETED_MESSAGES DM WHERE DM.USER_ID = :loggedInUserId))";

    private static final String GET_MESSAGES_BY_USERS_ID = "SELECT * FROM MESSAGE M WHERE " +
            "((M.USER_FROM = :firstUserId AND M.USER_TO = :secondUserId) OR (M.USER_FROM = :secondUserId AND " +
            "M.USER_TO = :firstUserId)) AND M.DATE_DELETED IS NULL AND M.ID NOT IN (SELECT DM.MESSAGE_ID " +
            "FROM DELETED_MESSAGES DM WHERE DM.USER_ID = :firstUserId) ORDER BY DATE_SENT ASC OFFSET :offset ROWS " +
            "FETCH NEXT :messagesAmount ROWS ONLY";

    private static final String GET_ALL_MESSAGES_BY_USERS_ID = "SELECT * FROM MESSAGE M WHERE " +
            "((M.USER_FROM = :firstUserId AND M.USER_TO = :secondUserId) OR (M.USER_FROM = :secondUserId AND " +
            "M.USER_TO = :firstUserId)) AND M.DATE_DELETED IS NULL AND M.ID NOT IN (SELECT DM.MESSAGE_ID " +
            "FROM DELETED_MESSAGES DM WHERE DM.USER_ID = :firstUserId) ORDER BY DATE_SENT ASC";

    private static final String DELETE_MESSAGE_FOR_USER = "INSERT INTO DELETED_MESSAGES VALUES(:userId, :messageId)";

    public MessageDAO() {
        setTypeParameterOfClass(Message.class);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteChat(long loggedInUserId, long userId) throws InternalServerError {
        try {
            List<Message> messages = getAllMessageByUsersId(loggedInUserId, userId);

            for (Message message : messages) {
                Query query = getEntityManager().createNativeQuery(DELETE_MESSAGE_FOR_USER);
                query.setParameter("userId", loggedInUserId);
                query.setParameter("messageId", message.getId());
                query.executeUpdate();
            }
        } catch (Exception e) {
            throw new InternalServerError("Deleting is failed");
        }
    }

    private List<Message> getAllMessageByUsersId(long loggedInUserId, long userId) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(GET_ALL_MESSAGES_BY_USERS_ID, Message.class);
            query.setParameter("firstUserId", loggedInUserId);
            query.setParameter("secondUserId", userId);
            return query.getResultList();
        } catch (Exception e) {
            throw new InternalServerError("Getting is failed");
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteMessageForUser(long userId, List<Long> messagesId) throws InternalServerError {
        try {
            for (long id : messagesId) {
                Query query = getEntityManager().createNativeQuery(DELETE_MESSAGE_FOR_USER);
                query.setParameter("userId", userId);
                query.setParameter("messageId", id);
                query.executeUpdate();
            }
        } catch (Exception e) {
            throw new InternalServerError("Deleting is failed");
        }
    }

    public List<User> getChatsByUserId(long loggedInUserId) throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(GET_CHATS_BY_USER_ID, User.class);
            query.setParameter("loggedInUserId", loggedInUserId);
            return query.getResultList();
        } catch (Exception e) {
            throw new InternalServerError("Getting is failed");
        }
    }

    public List<Message> getMessagesByUsersId(long firstUserId, long secondUserId, long offset)
            throws InternalServerError {
        try {
            Query query = getEntityManager().createNativeQuery(GET_MESSAGES_BY_USERS_ID, Message.class);
            query.setParameter("firstUserId", firstUserId);
            query.setParameter("secondUserId", secondUserId);
            query.setParameter("offset", offset);
            query.setParameter("messagesAmount", offset + 20);
            return query.getResultList();
        } catch (Exception e) {
            throw new InternalServerError("Getting is failed");
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateDateDeletedForList(List<Message> messages) throws InternalServerError {
        try {
            for (Message message : messages) {
                update(message);
            }
        } catch (Exception e) {
            throw new InternalServerError("Updating is failed");
        }
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
