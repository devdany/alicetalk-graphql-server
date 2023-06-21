package com.example.graphqlserver.translator;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.graphqlserver.database.entities.ChatEntity;
import com.example.graphqlserver.database.entities.MessageEntity;
import com.example.graphqlserver.database.entities.UserEntity;
import com.example.graphqlserver.schemas.chat.Chat;
import com.example.graphqlserver.schemas.message.Message;
import com.example.graphqlserver.schemas.user.User;

@Component
public class Translator {
  public Chat chatEntityToSchema(ChatEntity entity) {
    if (entity == null) return null;
    List<User> members = new ArrayList<User>(entity.getMembers().stream().map(member -> userEntityToSchema(member)).collect(Collectors.toList()));
    List<Message> messages = new ArrayList<Message>(entity.getMessages().stream().map(message -> messageEntityToSchema(message)).collect(Collectors.toList()));
    return new Chat(entity.getId(), entity.getCreatorId(), entity.isArchived(), members, messages);
  }

  public ChatEntity chatSchemaToEntity(Chat schema) {
    if (schema == null) return null;
    List<UserEntity> members = new ArrayList<UserEntity>(schema.getMembers().stream().map(member -> userSchemaToEntity(member)).collect(Collectors.toList()));
    List<MessageEntity> messages = new ArrayList<MessageEntity>(schema.getMessages().stream().map(message -> messageSchemaToEntity(message)).collect(Collectors.toList()));
    return new ChatEntity(schema.getId(), schema.getCreatorId(), schema.isArchived(), members, messages);
  }

  public Message messageEntityToSchema(MessageEntity entity) {
    if (entity == null) return null;
    ChatEntity chat = entity.getChat();
    Chat messageChat = new Chat(chat.getId(), chat.getCreatorId(), chat.isArchived(), chat.getMembers().stream().map(member -> userEntityToSchema(member)).collect(Collectors.toList()), new ArrayList<Message>());
    
    return new Message(entity.getId(), entity.getBody(), entity.getCreatedAt(), messageChat, userEntityToSchema(entity.getSender()));
  }

  public MessageEntity messageSchemaToEntity(Message schema) {
    if (schema == null) return null;
    Chat chat = schema.getChat();
    ChatEntity messageChat = new ChatEntity();
    messageChat.setId(chat.getId());
    messageChat.setCreatorId(chat.getCreatorId());
    messageChat.setArchived(chat.isArchived());
    messageChat.setMembers(chat.getMembers().stream().map(member -> userSchemaToEntity(member)).collect(Collectors.toList()));
    return new MessageEntity(schema.getId(), schema.getBody(), schema.getCreatedAt(), messageChat, userSchemaToEntity(schema.getSender()));
  }

  public User userEntityToSchema(UserEntity entity) {
    if (entity == null) return null;
    return new User(entity.getId(), entity.getEmail(), entity.getPassword());
  }

  public UserEntity userSchemaToEntity(User schema) {
    if (schema == null) return null;
    return new UserEntity(schema.getId(), schema.getEmail(), schema.getPassword());
  }
}