package com.example.graphqlserver.database.entities;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chats")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatEntity {
  @Id
  private String id;
  private String creatorId;
  private boolean archived;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable
  (
    name = "chats_members",
    joinColumns = @JoinColumn(name = " chatId"),
    inverseJoinColumns = @JoinColumn(name = "userId")
  )
  private List<UserEntity> members = new ArrayList<UserEntity>();

  @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
  private List<MessageEntity> messages = new ArrayList<MessageEntity>();
}
