package Service;

import Model.Message;
import DAO.MessageDAO;
import Model.Account;

import java.sql.*;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import java.util.ArrayList;
import java.util.List;

public class MessageService {

    MessageDAO messageDAO;

    // No argument constructor
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    /**
     * Constructor for when accountDAO is provided
     * 
     * @param accountDAO
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message, Account account) {
        if(validMessage(message, account) == false){
            return null;
        }
        Message newMessage = messageDAO.insertMessage(message);
        return newMessage;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = messageDAO.getAllMessages();
        return messages;
    }

    public List<Message> getAllMessagesFromAccount(int posted_by) {
        List<Message> messages = messageDAO.getAllMessagesFromAccount(posted_by);
        return messages;
    }

    public Message getMessageFromId(int message_id) {
        if(message_id == 0){
            return null;
        }
        Message message = messageDAO.getMessageFromId(message_id);
        return message;
    }

    public void deleteMessage(Message message) {
        messageDAO.delete(message);
    }

    public Message updateMessage(int message_id, Message message) {
        Message updatedMessage = messageDAO.getMessageFromId(message_id);

        if(updatedMessage == null){
            return null;
        }
        if(message.getMessage_text().length() == 0 || message.getMessage_text() == null || message.getMessage_text().length() > 255){
            return null;
        }
        
        updatedMessage.setMessage_text(message.getMessage_text());
        return updatedMessage;
    }

    public boolean validMessage(Message message, Account account) {
        if(message.getMessage_text().length() == 0 || message.getMessage_text() == null || message.getMessage_text().length() > 255){
            return false;
        } else {
            return true;
        }
    }
    

}
