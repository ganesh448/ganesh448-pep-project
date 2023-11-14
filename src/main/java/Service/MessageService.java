package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.MessageDAO;
import Model.Message;
public class MessageService 
{
    MessageDAO messageDAO;
    public MessageService()
    {
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO)
    {
        this.messageDAO = messageDAO;
    }

    public Message creatMessage(Message message)
    {
        if(message.getMessage_text() == null || message.getMessage_text().trim().isEmpty() || message.getMessage_text().length() > 255 )
        {
            return null;
        }
        Message postedBy = messageDAO.getMessageById(message.getPosted_by());
        if(postedBy == null)
        {
            return null;
        }
        Message newMessage = messageDAO.createMessage(message);
        return newMessage;
    }
    public List<Message> getAllMessages() throws SQLException
    {
        return messageDAO.getAllMessages();
    }
    public Message getMessageById(int message_id)
    {
        return messageDAO.getMessageById(message_id);
    }
    public Message deletMessageById(int id)
    {
        return messageDAO.deleteMessageById(id);
    }
    public List<Message> getMessagesByUser(int accountId) throws SQLException {
        return messageDAO.getMessagesByAccountId(accountId);
    }
    public Message updatMessage(int message_id, String newMessageText)
    {
        Message exisistingMessage = messageDAO.getMessageById(message_id);
        if(exisistingMessage == null )
        {
            return null;
        }
        else if(newMessageText != null && !(newMessageText.trim().isEmpty()) && newMessageText.length() <= 255)
        {
            exisistingMessage.setMessage_text(newMessageText);
            messageDAO.updateMessage(message_id, newMessageText);
            System.out.println(messageDAO.getMessageById(message_id));
            return messageDAO.getMessageById(message_id);
        }
        return null;
    }
    }