package com.example.graphqlserver.services;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.graphqlserver.database.repositories.ChatRepository;
import com.example.graphqlserver.schemas.chat.Chat;
import com.example.graphqlserver.translator.Translator;

@Service
public class ChatService {

  @Autowired
  private ChatRepository chatRepository;

  @Autowired
  private Translator translator;
  

  public Chat findById(String id) {
    return translator.chatEntityToSchema(chatRepository.findById(id).orElse(null));
  }

  public List<Chat> findByUserId(String userId) {
    return chatRepository.findChatsWithMember(userId).stream().map(chat -> translator.chatEntityToSchema(chat)).collect(java.util.stream.Collectors.toList());
  }

  public Chat save(Chat chat) {
    return translator.chatEntityToSchema(chatRepository.save(translator.chatSchemaToEntity(chat)));
  }
}
