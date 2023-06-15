package com.example.graphqlserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import graphql.scalars.ExtendedScalars;

@Configuration
public class GraphQLConfiguration {
  @Bean
  public RuntimeWiringConfigurer runtimeWiringConfigurer() {
      return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.DateTime);
  }
}
