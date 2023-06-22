package com.example.graphqlserver;

import java.util.Collections;

import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;

import com.example.graphqlserver.utils.TokenHelper;

import reactor.core.publisher.Mono;

@Configuration
public class AuthorizationInterceptor implements WebGraphQlInterceptor {
  @Override
  public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
    String token = request.getHeaders().getFirst("Authorization");
    if (token != null) {
      request.configureExecutionInput((executionInput, builder) -> 
        builder.graphQLContext(Collections.singletonMap("currentUserId", TokenHelper.decodeAccessToken(token))).build());
    }
    
    return chain.next(request);
  }
}
