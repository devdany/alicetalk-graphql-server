package com.example.graphqlserver.database.entities;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "messages")
@AllArgsConstructor
@Setter
@Getter
public class MessageEntity {
  @Id
  private String id;
  private String body;

  @CreatedDate
  private String createdAt;

  @ManyToOne
  @JoinColumn(name = "chatId")
  private ChatEntity chat;

  @ManyToOne
  @JoinColumn(name = "senderId")
  private UserEntity sender;
}
