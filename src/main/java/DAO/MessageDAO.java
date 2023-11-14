package DAO;

import Model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Util.ConnectionUtil;

public class MessageDAO {
       
    
    public Message createMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into message (posted_by,message_text,time_posted_epoch) values (?, ?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,message.getPosted_by());
            preparedStatement.setString(2,message.message_text);
            preparedStatement.setLong(3,message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                
                Message m = new Message(generated_message_id, message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
                return m;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
        public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
        String sql = "select * from message where message_id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);

        ResultSet rs = preparedStatement.executeQuery();
        while(rs.next()){
            Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                    rs.getString("message_text"),rs.getLong("time_posted_epoch"));
            return message;
        }
        }catch(SQLException e){
        System.out.println(e.getMessage());
        }
        return null;
    }
    public List<Message> getAllMessages() throws SQLException
    {
        List<Message> messages = new ArrayList<>();
        try
        {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "select * from message";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) 
            {
                int message_id = resultSet.getInt("message_id");
                int posted_by = resultSet.getInt("posted_by");
                String message_text = resultSet.getString("message_text");
                long time_posted_epoch = resultSet.getLong("time_posted_epoch");
                Message message = new Message(message_id,  posted_by, message_text,time_posted_epoch);
                messages.add(message);
            } 
        return messages;
        }
        catch(SQLException e){
        System.out.println(e.getMessage());
        }
        return null;
    }    
     public Message deleteMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "delete from message where message_id = ?";
            Message existingMessage = getMessageById(id);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return existingMessage;
        }catch(SQLException e){
        System.out.println(e.getMessage());
        }
        return null;
    }  
    public List<Message> getMessagesByAccountId(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            //Write SQL logic here
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setInt method here.
            preparedStatement.setInt(1, accountId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                int message_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String message_text = rs.getString("message_text");
                long time_posted_epoch = rs.getLong("time_posted_epoch");
                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    public void updateMessage(int message_id,String newMessageText)
    {
        try
        {
        Connection connection = ConnectionUtil.getConnection();
                String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, newMessageText);
                preparedStatement.setInt(2, message_id);
                preparedStatement.executeUpdate();
            }
        catch(Exception e){
        System.out.println(e.getMessage());
        }
    }
    public void updateMessageSuccessful(int message_id, String newMessageText) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newMessageText);
            preparedStatement.setInt(2, message_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateMessageMessageStringEmpty(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "");
            preparedStatement.setInt(2, message_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}