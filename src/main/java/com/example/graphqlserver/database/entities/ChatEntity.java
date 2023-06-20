package com.example.graphqlserver.database.entities;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chats")
@AllArgsConstructor
@Getter
@Setter
public class ChatEntity {
  @Id
  private String id;

  @ManyToMany(mappedBy = "chats")
  private List<UserEntity> members;

  @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
  private List<MessageEntity> messages;
}
