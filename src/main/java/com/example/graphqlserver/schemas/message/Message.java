package com.example.graphqlserver.schemas.message;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.graphqlserver.schemas.chat.Chat;
import com.example.graphqlserver.schemas.user.User;
import com.example.graphqlserver.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
  private String id;
  private Chat chat;
  private User sender;
  private String body;
  private String createdAt;

  public static Message create(Chat chat, User sender, String body) {
    Message message = new Message();
    message.setId("message-" + StringUtils.generateRandomString());
    message.setChat(chat);
    message.setSender(sender);
    message.setBody(body);
    message.setCreatedAt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    return message;
  }

}
