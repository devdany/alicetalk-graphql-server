package com.example.graphqlserver.schemas.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class ChatRepository {
  private static List<Chat> chats = new ArrayList<Chat>();
  private static List<Chat> archivedChats = new ArrayList<Chat>();

  public Chat findById(String id) {
    return chats.stream().filter(chat -> chat.getId().equals(id)).findFirst().orElse(null);
  }

  public Chat create(Chat chat) {
    chats.add(chat);
    return chat;
  }

  public List<Chat> findByUserId(String userId) {
    List<Chat> result = new ArrayList<Chat>();
    for (Chat chat : chats) {
      chat.getMembers().stream().filter(member -> member.getId().equals(userId)).findFirst().ifPresent(member -> result.add(chat));
    }

    return result;
  }

  public Chat archive(Chat chat) {
    chats.remove(chat);
    archivedChats.add(chat);
    return chat;
  }
}
