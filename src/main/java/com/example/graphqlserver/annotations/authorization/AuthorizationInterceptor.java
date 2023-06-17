package com.example.graphqlserver.annotations.authorization;

import org.springframework.web.servlet.HandlerInterceptor;

import com.example.graphqlserver.utils.TokenHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationInterceptor implements HandlerInterceptor {
  // 요청 스레드에서 userId 홀드
  private static final InheritableThreadLocal<String> userIdHolder = new InheritableThreadLocal<>();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = request.getHeader("Authorization");
    if (token != null && isValidToken(token)) {
      String userId = TokenHelper.decodeAccessToken(token);
      userIdHolder.set(userId); 
      return true;
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }
  }

  private boolean isValidToken(String token) {
    return TokenHelper.isValidAccessToken(token);
  }

  public static String getCurrentUserId() {
    return userIdHolder.get(); // 현재 스레드의 사용자 ID 반환
  }

  public static void clearCurrentUserId() {
    userIdHolder.remove(); // 현재 스레드의 사용자 ID 제거
  }
}
