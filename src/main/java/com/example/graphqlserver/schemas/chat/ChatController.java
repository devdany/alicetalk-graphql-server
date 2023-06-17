package com.example.graphqlserver.schemas.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import com.example.graphqlserver.GraphQLException;
import com.example.graphqlserver.schemas.user.User;
import com.example.graphqlserver.schemas.user.UserRepository;

@Controller
public class ChatController {

  @Autowired
  ChatRepository chatRepository;

  @Autowired
  UserRepository userRepository;

  @QueryMapping
  public Chat chat(@Argument String id) {
    return chatRepository.findById(id);
  }

  @MutationMapping
  public Chat createChat(@ContextValue String currentUserId, @Argument String[] memberIds) {
    User creator = userRepository.findById(currentUserId);

    if (creator == null) {
      throw new GraphQLException("CreatorNotFound", ErrorType.NOT_FOUND);
    }

    List<User> members = new ArrayList<User>();
    members.add(creator);
    if (memberIds != null) {
      for (String memberId : memberIds) {
        User member = userRepository.findById(memberId);
        if (member == null) {
          throw new GraphQLException("MemberNotFound", ErrorType.NOT_FOUND);
        }
        members.add(member);
      }
    }
   

    Chat chat = Chat.create(creator, members);
    chatRepository.create(chat);

    return chat;
  }

  @MutationMapping
  public Chat leaveChat(@Argument String chatId, @ContextValue String currentUserId) {
    User user = userRepository.findById(currentUserId);
    if (user == null) {
      throw new GraphQLException("UserNotFound", ErrorType.NOT_FOUND);
    }
    
    Chat chat = chatRepository.findById(chatId);
    if (chat == null) {
      throw new GraphQLException("ChatNotFound", ErrorType.NOT_FOUND);
    }
  
    chat.leaveChat(user);

    if (chat.getMembers().size() == 0) {
      chatRepository.archive(chat);
    }
    return chat;
  }

  @MutationMapping
  public Chat inviteToChat(@Argument String chatId, @Argument List<String> memberIds, @ContextValue String currentUserId) {
    User user = userRepository.findById(currentUserId);
    if (user == null) {
      throw new GraphQLException("UserNotFound", ErrorType.NOT_FOUND);
    }
    
    Chat chat = chatRepository.findById(chatId);
    if (chat == null) {
      throw new GraphQLException("ChatNotFound", ErrorType.NOT_FOUND);
    }
  
    if (!chat.isMember(user)) {
      throw new GraphQLException("NotMember", ErrorType.FORBIDDEN);
    }

    for (String memberId : memberIds) {
      User targetAsMember = userRepository.findById(memberId);
      if (targetAsMember == null) {
        throw new GraphQLException("MemberNotFound", ErrorType.NOT_FOUND);
      }

      chat.appendUsersToMembers(targetAsMember);
    }

    return chat;
  }

  @SchemaMapping
  public List<User> members(Chat chat) {
    return chat.getMembers();
  }
}
