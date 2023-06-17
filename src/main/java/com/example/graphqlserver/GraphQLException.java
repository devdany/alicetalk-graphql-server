package com.example.graphqlserver;

import org.springframework.graphql.execution.ErrorType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GraphQLException extends RuntimeException {
  private String message;
  private ErrorType errorType;
  public GraphQLException(String message, ErrorType errorType) {
    super(message);
    this.message = message;
    this.errorType = errorType;
  }
}
