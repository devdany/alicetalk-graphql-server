package com.example.graphqlserver.schemas.message;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {
  private static List<Message> messages = new ArrayList<Message>();

  public List<Message> findByChatId(String chatId) {
    List<Message> result = new ArrayList<Message>();
    for (Message message : messages) {
      if (message.getChat().getId().equals(chatId)) {
        result.add(message);
      }
    }

    return result;
  }

  public Message create(Message message) {
    messages.add(message);
    return message;
  }

}
