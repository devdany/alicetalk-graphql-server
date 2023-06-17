package com.example.graphqlserver.schemas.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import com.example.graphqlserver.GraphQLException;
import com.example.graphqlserver.schemas.chat.Chat;
import com.example.graphqlserver.schemas.chat.ChatRepository;
import com.example.graphqlserver.schemas.user.User;
import com.example.graphqlserver.schemas.user.UserRepository;

@Controller
public class MessageController {
  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ChatRepository chatRepository;

  @MutationMapping
  public Message sendMessage(@Argument String chatId, @Argument String body, @ContextValue String currentUserId) {
    if (currentUserId == null) {
      throw new GraphQLException("UnAuthorized", ErrorType.UNAUTHORIZED);
    }

    User sender = userRepository.findById(currentUserId);
    
    if (sender == null) {
      throw new GraphQLException("SenderNotFound", ErrorType.NOT_FOUND);
    }

    Chat chat = chatRepository.findById(chatId);
    
    if (chat == null) {
      throw new GraphQLException("ChatNotFound", ErrorType.NOT_FOUND);
    }

    Message createdMessage = Message.create(chat, sender, body);
    messageRepository.create(createdMessage);

    return createdMessage;
  }

  @SchemaMapping
  public User sender(Message message) {
    User sender = userRepository.findById(message.getSender().getId());
    return sender;
  }

  @SchemaMapping
  public Chat chat(Message message) {
    Chat chat = chatRepository.findById(message.getChat().getId());
    return chat;
  }
}
