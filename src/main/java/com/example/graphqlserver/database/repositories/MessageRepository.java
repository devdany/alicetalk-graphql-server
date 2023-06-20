package com.example.graphqlserver.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.graphqlserver.database.entities.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {
  
}
