package com.example.graphqlserver.database.entities;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Setter
@Getter
public class UserEntity {
    @Id
    private String id;
    private String email;
    private String password;

    public UserEntity (String id, String email, String password) {
      this.id = id;
      this.email = email;
      this.password = password;
    }


    @ManyToMany(mappedBy = "members")
    private List<ChatEntity> chats = new ArrayList<ChatEntity>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<MessageEntity> messages = new ArrayList<MessageEntity>();
}