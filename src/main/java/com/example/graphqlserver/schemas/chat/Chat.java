package com.example.graphqlserver.schemas.chat;

import java.util.ArrayList;
import java.util.List;

import com.example.graphqlserver.schemas.message.Message;
import com.example.graphqlserver.schemas.user.User;
import com.example.graphqlserver.utils.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Chat {
  private String id;
  private String creatorId;
  private boolean archived;
  private List<User> members;
  private List<Message> messages;

  public void appendUsersAsMembers(User user) {
    this.members.add(user);
  }

  public void leaveChat(User user) {
    this.members = this.members.stream().filter(member -> member.getId() != user.getId()).toList();
    if (this.members.size() == 0) {
      this.archived = true;
    }
  }

  public static Chat create(User creator, List<User> initMembers) {
    String chatId = "chat-" + StringUtils.generateRandomString();
    List<Message> messages = new ArrayList<Message>();
    Chat chat = new Chat(chatId, creator.getId(), false, initMembers, messages);
    return chat;
  }

  public boolean isMember(User user) {
    return this.members.stream().anyMatch(member -> member.getId() == user.getId());
  }
}
