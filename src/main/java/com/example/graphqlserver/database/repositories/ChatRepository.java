package com.example.graphqlserver.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.graphqlserver.database.entities.ChatEntity;

public interface ChatRepository extends JpaRepository<ChatEntity, String> {
  
}
