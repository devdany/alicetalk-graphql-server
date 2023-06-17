package com.example.graphqlserver.schemas.chat;

import java.util.List;

import com.example.graphqlserver.schemas.user.User;
import com.example.graphqlserver.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Chat {
  private String id;
  private User creator;
  private List<User> members;

  public void appendUsersToMembers(User user) {
    this.members.add(user);
  }

  public void leaveChat(User user) {
    this.members.remove(user);
  }

  public static Chat create(User creator, List<User> initMembers) {
    Chat chat = new Chat();
    // generate Random String for chatId 18 number and alphabet

    chat.setId(StringUtils.generateRandomString());
    chat.setCreator(creator);
    chat.setMembers(initMembers);

    return chat;
  }

  public boolean isMember(User user) {
    return this.members.contains(user);
  }
}
