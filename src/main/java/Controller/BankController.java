package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.*;
import Service.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

// Defining endpoints and handlers for controller
public class BankController {

    BankUserService bankUserService;
    // AccountService accountService; (match account service from Mahlet

    public BankController() {
        this.bankUserService = new BankUserService();
        // this.accountService = new AccountService();
    }

    /**
     * Endpoints in the startAPI() method, as the test suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //1. Process registration - POST localhost:8080/register
        app.post("/register", this::registerHandler);

        //2. Process logins- POST localhost:8080/login
        app.post("/login", this::loginHandler);

        /**
        //3. Creation of new messages - POST localhost:8080/messages
        app.post("/messages", this::messagesHandler);

        //4. Retrieve all messages - GET localhost:8080/messages
        app.get("/messages", this::getAllMessagesHandler);

        //5. Get a message by its Id - GET localhost:8080/messages/{message_id}
        app.get("/messages/{message_id}", this::getMessageByIdHandler);

        //6. Delete a message - DELETE localhost:8080/messages/{message_id}
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        //7. Patch message by Id - PATCH localhost:8080/messages/{message_id}
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);

        //8. Get a message by its account Id - GET localhost:8080/accounts/{account_id}/messages.
        app.get("/accounts/{account_id}/messages", this::getMessageByAccIdHandler);
        */

        return app;
    }


    /**
     * 1. Handler to POST and new registration endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     *
     * The body will contain a representation of a JSON Account, but will not contain an user_id.
     *  - The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
     *  - If the registration is not successful, the response status should be 400. (Client error)
     *
     */
    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        BankUser user = mapper.readValue(context.body(), BankUser.class);
        BankUser addedUser = bankUserService.addAccount(user);

        // if new unique account return JSON Account
        if (addedUser != null){
            context.json(mapper.writeValueAsString(addedUser));
            context.status(200);
        } else {
            // else not successful registration
            context.status(400);
        }
    }

    /**
     * 2: API should be able to process User logins.
     * Verify login on the endpoint POST localhost:8080/login.
     * The request body will contain a JSON representation of a BankUser, not containing an user_id. In the future, this action may generate a Session token to allow the user to securely use the site. We will not worry about this for now.
     *   - The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database.
     *     If successful, the response body should contain a JSON of the account in the response body, including its account_id.
     *     The response status should be 200 OK, which is the default.
     *   - If the login is not successful, the response status should be 401. (Unauthorized)
     *
     * @param context
     * @throws JsonProcessingException
     */
    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        BankUser user = mapper.readValue(context.body(), BankUser.class);
        BankUser loginUser = bankUserService.addAccount(user);

        // if unique account return JSON Account
        if (loginUser != null){
            context.json(mapper.writeValueAsString(loginUser));
            context.status(200);
        } else {
            // unauthorized login
            context.status(401);
        }
    }

}
