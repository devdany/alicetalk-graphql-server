package com.example.graphqlserver.schemas.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.graphqlserver.annotations.authorization.Authorization;
import com.example.graphqlserver.annotations.authorization.CurrentUserId;

@Controller
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @QueryMapping
  @Authorization
  public User me(@CurrentUserId String userId) {
    User user = userRepository.findById(userId);
    if (user == null) {
      throw new RuntimeException("User not found");
    }
    return user;
  }
}
