package com.example.graphqlserver.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.graphqlserver.database.repositories.MessageRepository;
import com.example.graphqlserver.schemas.message.Message;
import com.example.graphqlserver.translator.Translator;

@Service
public class MessageService {
  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private Translator translator;

  public List<Message> findByChatId(String chatId) {
    return messageRepository.findByChatIdOrderByCreatedAtDesc(chatId).stream().map(message -> translator.messageEntityToSchema(message)).collect(java.util.stream.Collectors.toList());
  }

  public Message save(Message message) {
    return translator.messageEntityToSchema(messageRepository.save(translator.messageSchemaToEntity(message)));
  }
}
