package com.example.graphqlserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

@Configuration
public class CustomExceptionResolver extends DataFetcherExceptionResolverAdapter {
  @Override
  protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
    if (ex instanceof GraphQLException) {
      GraphQLException graphqlException = (GraphQLException) ex;
      return GraphqlErrorBuilder.newError()
        .errorType(graphqlException.getErrorType())
        .message(graphqlException.getMessage())
        .path(env.getExecutionStepInfo().getPath())
        .location(env.getField().getSourceLocation())
        .build();
    } else if (ex instanceof RuntimeException) {
      return GraphqlErrorBuilder.newError()
        .errorType(ErrorType.INTERNAL_ERROR)
        .message(ex.getMessage())
        .path(env.getExecutionStepInfo().getPath())
        .location(env.getField().getSourceLocation())
        .build();
    } else {
      return null;
    }
  }
}