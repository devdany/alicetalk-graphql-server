package com.example.graphqlserver.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.graphqlserver.database.entities.ChatEntity;
import java.util.List;


public interface ChatRepository extends JpaRepository<ChatEntity, String> {
  @Query("SELECT c FROM ChatEntity c JOIN c.members m WHERE m.id = :memberId")
   List<ChatEntity> findChatsWithMember(@Param("memberId") String memberId);
}