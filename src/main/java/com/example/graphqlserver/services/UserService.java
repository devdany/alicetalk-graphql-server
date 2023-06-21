package com.example.graphqlserver.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.graphqlserver.database.repositories.UserRepository;
import com.example.graphqlserver.schemas.user.User;
import com.example.graphqlserver.translator.Translator;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private Translator translator;
  
  public User findById(String id) {
    return translator.userEntityToSchema(userRepository.findById(id).orElse(null));
  }

  public User findByEmailAndPassword(String email, String password) {
    return translator.userEntityToSchema(userRepository.findByEmailAndPassword(email, password));
  }

  public boolean includeInvalidUserId(String[] userIds) {
    return Arrays.stream(userIds).anyMatch(userId -> findById(userId) == null);
  }
}
