package com.example.graphqlserver.schemas.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import com.example.graphqlserver.GraphQLException;
import com.example.graphqlserver.schemas.chat.Chat;
import com.example.graphqlserver.services.ChatService;
import com.example.graphqlserver.services.UserService;
import com.example.graphqlserver.utils.TokenHelper;


@Controller
public class UserController {
  @Autowired
  private UserService userService;

  @Autowired
  private ChatService chatService;

  @QueryMapping
  public User me(@ContextValue String currentUserId) {
    if (currentUserId == null) {
      throw new GraphQLException("UnAuthorized", ErrorType.UNAUTHORIZED);
    }

    User user = userService.findById(currentUserId);
    if (user == null) {
      throw new GraphQLException("UserNotFound", ErrorType.NOT_FOUND);
    }
    return user;
  }

  @QueryMapping
  public String login(@Argument String email, @Argument String password) {
    User user = userService.findByEmailAndPassword(email, password);
    if (user == null) {
      throw new GraphQLException("NotMatchEmailAndPassword", ErrorType.FORBIDDEN);
    }

    return TokenHelper.generateAccessToken(user.getId());
  }

  @SchemaMapping
  public List<Chat> chats(User user) {
    List<Chat> chats = chatService.findByUserId(user.getId());
    return chats;
  }
}
