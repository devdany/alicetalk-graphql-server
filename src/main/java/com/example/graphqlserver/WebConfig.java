package com.example.graphqlserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.graphqlserver.annotations.authorization.AuthorizationInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(new AuthorizationInterceptor()).addPathPatterns("/graphql");
  }
}
