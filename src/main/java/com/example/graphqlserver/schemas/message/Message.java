package com.example.graphqlserver.schemas.message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.graphqlserver.schemas.chat.Chat;
import com.example.graphqlserver.schemas.user.User;
import com.example.graphqlserver.utils.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Message {
  private String id;
  private String body;
  private String createdAt;
  private Chat chat;
  private User sender;
  

  public static Message create(Chat chat, User sender, String body) {
    String messageId = "message-" + StringUtils.generateRandomString();
    String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    Message message = new Message(messageId, body, createdAt, chat, sender);

    return message;
  }

}