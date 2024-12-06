package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.security.Provider.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;

import java.util.List;
import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {

    // handle account and message operations
    private final AccountService accountService;
    private final MessageService messageService;
    // private final MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::register);
        app.post("/login", this::login);
        app.post("/messages", this::createMessages);
        app.get("/messages/{message_id}", this::getMessage_id);
        app.get("/messages", this::getAllMessages);
        app.delete("messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getMessage_Account);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void register(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount == null){
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void login(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.validLogin(account);
        if(addedAccount == null){
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }
    }

    private void createMessages(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        Account account = accountService.getAccountId(message.getPosted_by());
        Message addedMessage = messageService.addMessage(message, account);
        if(addedMessage == null){
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }
    }

    private void getAllMessages(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    private void getMessage_id(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageFromId(id);

        if(message == null){
            ctx.status(200);
        } else {
            ctx.json(message);
        }
        


    }

    private void getMessage_Account(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("account_id"));

        ctx.json(messageService.getAllMessagesFromAccount(id));

        

        
    }

    private void deleteMessage(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageFromId(id);

        if(message==null){
            ctx.status(200);
        } else {
            messageService.deleteMessage(message);
            ctx.json(message);
        }
    }

    private void updateMessage(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);

        if(updatedMessage == null){
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }

}