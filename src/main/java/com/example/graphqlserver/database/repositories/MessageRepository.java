package com.example.graphqlserver.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.graphqlserver.database.entities.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {
  List<MessageEntity> findByChatId(String chatId);
}
