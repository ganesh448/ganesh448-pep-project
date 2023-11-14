package Controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.MessageService;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    MessageService messageService;
    SocialMediaService socialmediaservice;
    public SocialMediaController()
    {
        socialmediaservice = new SocialMediaService();
        messageService = new MessageService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
         // Register the /register endpoint
         app.post("/register", this::registerAccount);
         // Register the /login endpoint
         app.post("/login", this::loginUser);
         app.post("/messages", this::createMessage);
         app.get("/messages", this::getAllMessages);
         app.get("/messages/{message_id}", this::getMessageById);
         app.delete("/messages/{message_id}", this::deleteMessageHandler);
         app.get("/accounts/{account_id}/messages", this::getMessagesByUser);
         app.patch("messages/{message_id}", this::updateMessageHandler);
        return app;
    }
    public void registerAccount(Context ctx)throws JsonProcessingException 
    {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account createdAccount = socialmediaservice.registerAccount(account);
        if (createdAccount != null) {
            ctx.json(createdAccount);  // Return the created account with account_id
            ctx.status(200); // 200 OK
        } else {
            ctx.status(400); // 400 Bad Request
        }
    }
    public void loginUser(Context ctx)
    {
        Map<String,String> req = ctx.bodyAsClass(Map.class);
        String username = req.get("username");
        String password = req.get("password");
        Account account = socialmediaservice.login(username, password);

        if(account != null)
        {
            ctx.json(account);
            ctx.status(200);
        }
        else{
            ctx.status(401);
        }
    }
    public void createMessage(Context ctx) throws JsonMappingException, JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.creatMessage(message);
       if(createdMessage != null)
        {
            ctx.json(createdMessage);
            ctx.status(200);
        }
        else{
            ctx.status(400);
        }
    }
     public void getAllMessages(Context context) throws SQLException {
        List<Message> messages = messageService.getAllMessages();
        System.out.println(messages);
        
        if (messages != null) {
            context.json(messages);
            context.status(200);
        } else {
            context.status(500);
        }
    }
    public void getMessageById(Context context) throws JsonMappingException, JsonProcessingException
    {
        int message_id = context.pathParamAsClass("message_id", Integer.class).get();
        Message message = messageService.getMessageById(message_id);
        
        if (message != null) {
            context.json(message);
            context.status(200);
        } else {
            context.status(200);
            // Return an empty response body if no message is found
        }
    }
    private void deleteMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deletMessageById(messageId);
        if (deletedMessage != null) {
            context.json(deletedMessage);
            context.status(200); // OK
        } else {
            context.status(200); // OK
        }
    }
    public  void getMessagesByUser(Context ctx) throws SQLException {
        int accountId = ctx.pathParamAsClass("account_id", Integer.class).get();
        List<Message> userMessages = messageService.getMessagesByUser(accountId);
        ctx.json(userMessages);
    }
   public void updateMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
            ObjectMapper mapper = new ObjectMapper();
            int messageId = ctx.pathParamAsClass("message_id", Integer.class).get();
            String newMessageText = ctx.body();
            if (newMessageText != null && newMessageText.length() <= 255) {
            Message updatedMessage = messageService.updatMessage(messageId, newMessageText);
            if (updatedMessage != null) {
                ctx.status(200);
                ctx.json(mapper.writeValueAsString(updatedMessage));
            } else {
                ctx.status(400); 
            }
        }else{
            ctx.status(400);
        }
   }
}

        