package DAO;

import Model.Message;
import Util.ConnectionUtil;

import static org.mockito.Mockito.lenient;

import java.sql.*;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    /**
     * 
     * @param message
     * @return
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();
            ResultSet pKeyRS = ps.getGeneratedKeys();
            if (pKeyRS.next()) {
                int id = pKeyRS.getInt(1);
                return new Message(id, message.getPosted_by(), message.getMessage_text(),
                        message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message tempMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(tempMessage);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * 
     * @param posted_by
     * @param message
     * @return
     */
    public List<Message> getAllMessagesFromAccount(int posted_by) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, posted_by);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message tempMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(tempMessage);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Return one message by message ID
     * 
     * @param id
     * @return
     */
    public Message getMessageFromId(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message where message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e){
            throw new RuntimeException("Error with SQL", e);
        }
        return null;
    }

    /**
     * 
     * @param message
     * @return
     */
    public void delete(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message.getMessage_id());
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * I feel like this is how I am suppose to update it but for some reason the other way is the only way I've been able to make work.
     * @param message_id
     * @param message
     * @return
     */
    // public void update(int message_id, Message message) {
    //     Connection connection = ConnectionUtil.getConnection();
    //     try{
    //         String sql = "UPDATE message SET posted_by = ?, message_text = ?, time_posted_epoch = ? WHERE message_id = ?";
    //         PreparedStatement ps = connection.prepareStatement(sql);
    //         ps.setInt(1, message.posted_by);
    //         ps.setString(2, message.message_text);
    //         ps.setLong(3, message.time_posted_epoch);
    //         ps.setInt(4, message_id);

    //         ps.executeUpdate();
    //     } catch (SQLException e){
    //         System.out.println(e.getMessage());
    //     }
    // }

}
