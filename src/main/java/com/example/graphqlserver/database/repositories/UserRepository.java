package com.example.graphqlserver.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.graphqlserver.database.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
  UserEntity findByEmailAndPassword(String email, String password);
}