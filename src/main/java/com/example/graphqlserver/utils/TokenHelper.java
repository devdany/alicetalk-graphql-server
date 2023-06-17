package com.example.graphqlserver.utils;

public class TokenHelper {
  public static String generateAccessToken(String userId) {
    return "elicetalk-access-token:" + userId;
  }

  public static String decodeAccessToken(String accessToken) {
    return accessToken.replace("elicetalk-access-token:", "");
  }

  public static Boolean isValidAccessToken(String accessToken) {
    return accessToken.startsWith("elicetalk-access-token:");
  }
}
