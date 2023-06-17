package com.example.graphqlserver.schemas.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
  private static List<User> users = Arrays.asList(
    new User("1", "dany@elice.com", "1234"),
    new User("2", "heidi@elice.com", "1234"),
    new User("3", "rookie@elice.com", "1234")
  );
  public User findById(String id) {
    return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
  }

  public User findByEmailAndPassword(String email, String password) {
    return users.stream().filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password)).findFirst().orElse(null);
  }

  public boolean includeInvalidUserId(String[] userIds) {
    return Arrays.stream(userIds).anyMatch(userId -> findById(userId) == null);
  }
}
