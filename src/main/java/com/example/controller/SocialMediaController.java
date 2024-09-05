package com.example.controller;

import com.example.entity.*;
import com.example.service.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    AccountService accountService;

    @Autowired
    MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<Account> createAccountHandler(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        if(createdAccount == null) {
            return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST); 
        }
        else if(createdAccount.getAccountId() == 409) { // find a better way to do this lmao
            return new ResponseEntity<Account>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<Account>(createdAccount, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> loginHandler(@RequestBody Account account) {
        Account loginAccount = accountService.getAccountByUsernameAndPassword(account);
        if(loginAccount == null) {
            return new ResponseEntity<Account>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<Account>(loginAccount, HttpStatus.OK);
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessageHandler(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message, accountService.checkAccountExists(message.getPostedBy()));
        if(createdMessage == null) {
            return new ResponseEntity<Message>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Message>(createdMessage, HttpStatus.OK);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessagesHandler() {
        List<Message> messages = messageService.getMessages();
        return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity getMessageHandler(@PathVariable int message_id) {
        Message obtainedMessage = messageService.findMessage(message_id);
        return new ResponseEntity<Message>(obtainedMessage, HttpStatus.OK);
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageHandler(@PathVariable int message_id) {
        Integer deletedMessage = messageService.deleteMessage(message_id);
        return new ResponseEntity<Integer>(deletedMessage, HttpStatus.OK);
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessageHandler(@PathVariable int message_id, @RequestBody Message message) {
        Integer updatedMessage = messageService.updateMessage(message_id, message.getMessageText());
        if(updatedMessage == null) {
            return new ResponseEntity<Integer>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Integer>(updatedMessage, HttpStatus.OK);
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesFromUserHandler(@PathVariable int account_id) {
        List<Message> usersMessages = messageService.getMessagesFromUser(account_id);
        return new ResponseEntity<List<Message>>(usersMessages, HttpStatus.OK);
    }
}
