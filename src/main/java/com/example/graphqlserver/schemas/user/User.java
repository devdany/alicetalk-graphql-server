package com.example.graphqlserver.schemas.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
  private String id;
  private String email;
  private String password;
}
