package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.repository.MessageRepository;
import com.example.entity.Message;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional
public class MessageService {

    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message, boolean accountExists) {
        if((!message.getMessageText().equals("")) && (message.getMessageText().length() <= 255) && (accountExists)) {
            return messageRepository.save(message);
        }
        return null;
    }

    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    public Message findMessage(int messageId) {
        try {
            return messageRepository.findById(messageId).get();
        }
        catch (NoSuchElementException e) {
            return null;
        }
    }

    public Integer deleteMessage(int messageId) {
        if(messageRepository.deleteByMessageId(messageId) == 1) {
            return 1;
        }
        return null;
    }

    public Integer updateMessage (int messageId, String messageText) {
        Message oldMessage = findMessage(messageId);
        if(oldMessage != null) {
            if((messageText != "") && (messageText.length() <= 255)) {
                oldMessage.setMessageText(messageText);
                messageRepository.save(oldMessage);
                return 1;
            }
        }
        return null;
    }

    public List<Message> getMessagesFromUser (int accountId) {
        return messageRepository.findMessagesByPostedBy(accountId);
    }
}
