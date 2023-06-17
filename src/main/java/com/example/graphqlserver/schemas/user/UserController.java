package com.example.graphqlserver.schemas.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import com.example.graphqlserver.GraphQLException;
import com.example.graphqlserver.utils.TokenHelper;


@Controller
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @QueryMapping
  public User me(@ContextValue String currentUserId) {
    if (currentUserId == null) {
      throw new GraphQLException("UnAuthorized", ErrorType.UNAUTHORIZED);
    }
    User user = userRepository.findById(currentUserId);
    if (user == null) {
      throw new GraphQLException("UserNotFound", ErrorType.NOT_FOUND);
    }
    return user;
  }

  @QueryMapping
  public String login(@Argument String email, @Argument String password) {
    User user = userRepository.findByEmailAndPassword(email, password);
    if (user == null) {
      throw new GraphQLException("NotMatchEmailAndPassword", ErrorType.FORBIDDEN);
    }

    return TokenHelper.generateAccessToken(user.getId());
  }
}
