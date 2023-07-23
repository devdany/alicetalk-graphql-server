package com.example.graphqlserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class GraphqlServerApplicationTests {

		/*
		 * ----------------------------------------------------------------------------------
		 * [프로젝트1] SDL 작성하기
		*/
		@Test
		@DisplayName("User 타입이 제대로 작성되었는가?")
		void testUserType() {
				// Introspection 쿼리
				String query = """
					query {
							__type(name: "User") {
									name
									fields {
											name
											type {
													name
													kind
													ofType {
															kind
															name
															ofType {
																	kind
																	name
																	ofType {
																			kind
																			name
																	}
															}
													}
											}
									}
							}
					}
					""";
	
			// 개행과 탭 문자를 공백으로 변경
			String cleanedQuery = query.replaceAll("[\n\t]", " ");
	
			// 이스케이프 및 JSON 포매팅
			String formattedQuery = """
			{
					"query": "%s"
			}
			""".formatted(cleanedQuery.replace("\"", "\\\""));
			
			WebClient webClient = WebClient.create("http://localhost:8080");
	
			String response = webClient.post()
									.uri("/graphql")
									.contentType(MediaType.APPLICATION_JSON)
									.bodyValue(formattedQuery)
									.retrieve()
									.bodyToMono(String.class)
									.block();
	
			try {
					// response의 본문을 JSON으로 파싱
					JSONObject root = new JSONObject(response);
	
					// User 타입 정보를 가져옴
					JSONObject userType = root.getJSONObject("data").getJSONObject("__type");
					if (userType == null) {
							Assertions.fail("User 타입이 존재하지 않습니다.");
					}
	
					// 필드 정보를 확인
					JSONArray fields = userType.getJSONArray("fields");
					if (fields == null) {
							Assertions.fail("User 타입에 필드가 존재하지 않습니다.");
					}
	
					for (int i = 0; i < fields.length(); i++) {
							JSONObject field = fields.getJSONObject(i);
							String fieldName = field.getString("name");
							JSONObject type = field.getJSONObject("type");
	
							if (fieldName.equals("id")) {
									Assertions.assertEquals("ID", type.getJSONObject("ofType").getString("name"));
									Assertions.assertEquals("NON_NULL", type.getString("kind"));
							} else if (fieldName.equals("email")) {
									Assertions.assertEquals("String", type.getJSONObject("ofType").getString("name"));
									Assertions.assertEquals("NON_NULL", type.getString("kind"));
							} else if (fieldName.equals("chats")) {
									JSONObject ofType = type.getJSONObject("ofType");
									Assertions.assertEquals("LIST", ofType.getString("kind"));
									Assertions.assertEquals("Chat", ofType.getJSONObject("ofType").getJSONObject("ofType").getString("name"));
							}
					}
			} catch (JSONException e) {
					Assertions.fail(e.getMessage());
			}
		}

    @Test
    @DisplayName("Chat 타입이 제대로 작성되었는가?")
    void testChatType() {
        // Introspection 쿼리
        String query = """
					query {
						__type(name: "Chat") {
								name
								fields {
										name
										type {
												name
												kind
												ofType {
														kind
														name
														ofType {
																kind
																name
																ofType {
																		kind
																		name
																}
														}
												}
										}
								}
						}
				}
        """;

        String cleanedQuery = query.replaceAll("[\n\t]", " ");
        String formattedQuery = String.format("{ \"query\": \"%s\" }", cleanedQuery.replace("\"", "\\\""));

        WebClient webClient = WebClient.create("http://localhost:8080");
        String response = webClient.post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(formattedQuery)
            .retrieve()
            .bodyToMono(String.class)
            .block();

        try {
            JSONObject root = new JSONObject(response);
            JSONObject chatType = root.getJSONObject("data").getJSONObject("__type");

            JSONArray fields = chatType.getJSONArray("fields");
						for (int i = 0; i < fields.length(); i++) {
							JSONObject field = fields.getJSONObject(i);
					
							String fieldName = field.getString("name");
							JSONObject type = field.getJSONObject("type");
							JSONObject firstOfType = type.optJSONObject("ofType");
							JSONObject secondOfType = firstOfType != null ? firstOfType.optJSONObject("ofType") : null;
							JSONObject thirdOfType = secondOfType != null ? secondOfType.optJSONObject("ofType") : null;
							
							if (fieldName.equals("id")) {
									String idTypeName = firstOfType.getString("name");
									Assertions.assertEquals("ID", idTypeName);
									Assertions.assertEquals("NON_NULL", type.getString("kind"));
							} else if (fieldName.equals("members")) {
									String membersTypeName = thirdOfType.getString("name");
									Assertions.assertEquals("User", membersTypeName);
									Assertions.assertEquals("LIST", firstOfType.getString("kind"));
							} else if (fieldName.equals("messages")) {
									String messagesTypeName = thirdOfType.getString("name");
									Assertions.assertEquals("Message", messagesTypeName);
									Assertions.assertEquals("LIST", firstOfType.getString("kind"));
							}
					}
        } catch (JSONException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Message 타입이 제대로 작성되었는가?")
    void testMessageType() {
        // Introspection 쿼리
        String query = """
            query {
                __type(name: "Message") {
                    name
                    fields {
                        name
                        type {
                            name
                            kind
                            ofType {
                                kind
                                name
                            }
                        }
                    }
                }
            }
            """;

        String cleanedQuery = query.replaceAll("[\n\t]", " ");
        String formattedQuery = String.format("{ \"query\": \"%s\" }", cleanedQuery.replace("\"", "\\\""));

        WebClient webClient = WebClient.create("http://localhost:8080");
        String response = webClient.post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(formattedQuery)
            .retrieve()
            .bodyToMono(String.class)
            .block();

        try {
            JSONObject root = new JSONObject(response);
            JSONObject messageType = root.getJSONObject("data").getJSONObject("__type");

            JSONArray fields = messageType.getJSONArray("fields");

            for (int i = 0; i < fields.length(); i++) {
                JSONObject field = fields.getJSONObject(i);
                String fieldName = field.getString("name");
                JSONObject type = field.getJSONObject("type");
                String typeName = type.isNull("ofType") ? type.getString("name") : type.getJSONObject("ofType").getString("name");
                String kind = type.getString("kind");

                switch (fieldName) {
                    case "id":
                        Assertions.assertEquals("ID", typeName);
                        Assertions.assertEquals("NON_NULL", kind);
                        break;
                    case "chat":
                        Assertions.assertEquals("Chat", typeName);
                        Assertions.assertEquals("OBJECT", kind);
                        break;
                    case "sender":
                        Assertions.assertEquals("User", typeName);
                        Assertions.assertEquals("OBJECT", kind);
                        break;
                    case "body":
                        Assertions.assertEquals("String", typeName);
                        Assertions.assertEquals("NON_NULL", kind);
                        break;
                    case "createdAt":
                        Assertions.assertEquals("DateTime", typeName);
                        Assertions.assertEquals("NON_NULL", kind);
                        break;
                }
            }
        } catch (JSONException e) {
            Assertions.fail(e.getMessage());
        }
    }

		@Test
    @DisplayName("Query 타입이 제대로 작성되었는가?")
    void testQueryType() {
        // Introspection 쿼리
        String query = """
            query {
                __type(name: "Query") {
                    name
                    fields {
                        name
                        args {
                            name
                            type {
                                kind
                                name
                                ofType {
                                    kind
                                    name
                                }
                            }
                        }
                        type {
                            name
                            kind
                            ofType {
                                kind
                                name
                            }
                        }
                    }
                }
            }
            """;

        String cleanedQuery = query.replaceAll("[\n\t]", " ");
        String formattedQuery = String.format("{ \"query\": \"%s\" }", cleanedQuery.replace("\"", "\\\""));

        WebClient webClient = WebClient.create("http://localhost:8080");
        String response = webClient.post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(formattedQuery)
            .retrieve()
            .bodyToMono(String.class)
            .block();

        try {
            JSONObject root = new JSONObject(response);
            JSONObject queryType = root.getJSONObject("data").getJSONObject("__type");
            JSONArray fields = queryType.getJSONArray("fields");

            for (int i = 0; i < fields.length(); i++) {
                JSONObject field = fields.getJSONObject(i);
                String fieldName = field.getString("name");
                JSONObject returnType = field.getJSONObject("type");
                String returnTypeName = returnType.isNull("ofType") ? returnType.getString("name") : returnType.getJSONObject("ofType").getString("name");
                String returnKind = returnType.getString("kind");

                switch (fieldName) {
                    case "user":
                        Assertions.assertEquals("User", returnTypeName);
                        Assertions.assertEquals("OBJECT", returnKind);

                        JSONArray userArgs = field.getJSONArray("args");
                        for (int j = 0; j < userArgs.length(); j++) {
                            JSONObject arg = userArgs.getJSONObject(j);
                            if ("id".equals(arg.getString("name"))) {
                                JSONObject argType = arg.getJSONObject("type");
                                Assertions.assertEquals("ID", argType.getJSONObject("ofType").getString("name"));
                                Assertions.assertEquals("NON_NULL", argType.getString("kind"));
                            }
                        }
                        break;

                    case "login":
                        Assertions.assertEquals("String", returnTypeName);
                        Assertions.assertEquals("SCALAR", returnKind);

                        JSONArray loginArgs = field.getJSONArray("args");
                        for (int j = 0; j < loginArgs.length(); j++) {
                            JSONObject arg = loginArgs.getJSONObject(j);
                            if ("email".equals(arg.getString("name")) || "password".equals(arg.getString("name"))) {
                                JSONObject argType = arg.getJSONObject("type");
                                Assertions.assertEquals("String", argType.getJSONObject("ofType").getString("name"));
                                Assertions.assertEquals("NON_NULL", argType.getString("kind"));
                            }
                        }
                        break;

                    case "me":
                        Assertions.assertEquals("User", returnTypeName);
                        Assertions.assertEquals("OBJECT", returnKind);
                        break;

                    case "chat":
                        Assertions.assertEquals("Chat", returnTypeName);
                        Assertions.assertEquals("OBJECT", returnKind);

                        JSONArray chatArgs = field.getJSONArray("args");
                        for (int j = 0; j < chatArgs.length(); j++) {
                            JSONObject arg = chatArgs.getJSONObject(j);
                            if ("id".equals(arg.getString("name"))) {
                                JSONObject argType = arg.getJSONObject("type");
                                Assertions.assertEquals("ID", argType.getJSONObject("ofType").getString("name"));
                                Assertions.assertEquals("NON_NULL", argType.getString("kind"));
                            }
                        }
                        break;

                    default:
                        Assertions.fail("Unexpected field in Query: " + fieldName);
                        break;
                }
            }
        } catch (JSONException e) {
            Assertions.fail(e.getMessage());
        }
    }

		@Test
    @DisplayName("Mutation 타입이 제대로 작성되었는가?")
    void testMutationType() {
        // Introspection 쿼리
        String query = """
					query {
						__schema {
								mutationType {
										fields {
												name
												args {
														name
														type {
																kind
																name
																ofType {
																		kind
																		name
																		ofType {
																				kind
																				name
																				ofType {
																						kind
																						name
																				}
																		}
																}
														}
												}
												type {
														name
												}
										}
								}
						}
				}
            """;

        String cleanedQuery = query.replaceAll("[\n\t]", " ");
        String formattedQuery = String.format("{ \"query\": \"%s\" }", cleanedQuery.replace("\"", "\\\""));

        WebClient webClient = WebClient.create("http://localhost:8080");
        String response = webClient.post()
            .uri("/graphql")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(formattedQuery)
            .retrieve()
            .bodyToMono(String.class)
            .block();

						try {
							JSONObject root = new JSONObject(response);
							JSONArray fields = root.getJSONObject("data").getJSONObject("__schema").getJSONObject("mutationType").getJSONArray("fields");
			
							for (int i = 0; i < fields.length(); i++) {
									JSONObject field = fields.getJSONObject(i);
									String fieldName = field.getString("name");
									JSONArray args = field.getJSONArray("args");
			
									for (int j = 0; j < args.length(); j++) {
											JSONObject arg = args.getJSONObject(j);
											String argName = arg.getString("name");
											JSONObject argType = arg.getJSONObject("type");
			
											// 여러 ofType 레이어를 통과하며 실제 타입 이름을 가져옴
											String argTypeName = argType.isNull("name") ? null : argType.getString("name");
											while (argType != null && argTypeName == null) {
													argType = argType.isNull("ofType") ? null : argType.getJSONObject("ofType");
													if (argType != null) {
															argTypeName = argType.isNull("name") ? null : argType.getString("name");
													}
											}
			
											switch (fieldName) {
													case "createChat":
															if ("memberIds".equals(argName)) {
																	Assertions.assertEquals("ID", argTypeName); // memberIds는 ID![] 형태이므로
															}
															break;
													case "inviteToChat":
															if ("chatId".equals(argName)) {
																	Assertions.assertEquals("ID", argTypeName);
															} else if ("memberIds".equals(argName)) {
																	Assertions.assertEquals("ID", argTypeName); // memberIds는 ID![] 형태이므로
															}
															break;
													case "leaveChat":
															if ("chatId".equals(argName)) {
																	Assertions.assertEquals("ID", argTypeName);
															}
															break;
													case "sendMessage":
															if ("chatId".equals(argName)) {
																	Assertions.assertEquals("ID", argTypeName);
															} else if ("body".equals(argName)) {
																	Assertions.assertEquals("String", argTypeName);
															}
															break;
											}
									}
							}
					} catch (JSONException e) {
							Assertions.fail(e.getMessage());
					}
    }
	

		// ----------------------------------------------------------------------------------
		/*
		 * ----------------------------------------------------------------------------------
		 * [프로젝트 2-3] Schema 작성하기 - Controller 작성하기
		*/


		@Test
		@DisplayName("Login Query 테스트")
		void loginQueryTest() {
			String query = """
					query {
							login(email: "tester@elice.com", password: "1234")
					}
					""";

			String cleanedQuery = query.replaceAll("[\n\t]", " ");
			String formattedQuery = String.format("{ \"query\": \"%s\" }", cleanedQuery.replace("\"", "\\\""));

			WebClient webClient = WebClient.create("http://localhost:8080");
			String response = webClient.post()
									.uri("/graphql")
									.contentType(MediaType.APPLICATION_JSON)
									.bodyValue(formattedQuery)
									.retrieve()
									.bodyToMono(String.class)
									.block();

			try {
					JSONObject root = new JSONObject(response);
					String token = root.getJSONObject("data").getString("login");
					Assertions.assertNotNull(token);
			} catch (JSONException e) {
					Assertions.fail(e.getMessage());
			}
		}

		@Test
		@DisplayName("Chat Query 테스트")
		void chatQueryTest() {
			String query = """
					query {
							chat(id: "chat-fortest") {
									id
									members {
											id
											email
									}
									messages {
											body
									}
							}
					}
					""";

			String cleanedQuery = query.replaceAll("[\n\t]", " ");
			String formattedQuery = String.format("{ \"query\": \"%s\" }", cleanedQuery.replace("\"", "\\\""));

			WebClient webClient = WebClient.create("http://localhost:8080");
			String response = webClient.post()
									.uri("/graphql")
									.contentType(MediaType.APPLICATION_JSON)
									.bodyValue(formattedQuery)
									.retrieve()
									.bodyToMono(String.class)
									.block();

			try {
					JSONObject root = new JSONObject(response);
					String chatId = root.getJSONObject("data").getJSONObject("chat").getString("id");

					// chat ID가 올바른지 확인
					Assertions.assertEquals("chat-fortest", chatId);
			} catch (JSONException e) {
					Assertions.fail(e.getMessage());
			}
		}

	@Test
	@DisplayName("채팅 관련 Mutation 테스트")
	void chatMutationTest() {
		WebClient webClient = WebClient.builder()
			.baseUrl("http://localhost:8080")
			.defaultHeader("Authorization", "elicetalk-access-token:4")
			.build();

		try {
			// 1. createChat 호출
			String createChatMutation = """
				mutation {
						createChat {
								id
						}
				}
				""";
			String cleanedCreateChatMutation = createChatMutation.replaceAll("[\n\t]", " ");
			String formattedCreateChatMutation = String.format("{ \"query\": \"%s\" }", cleanedCreateChatMutation.replace("\"", "\\\""));
			String response = webClient.post()
						.uri("/graphql")
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(formattedCreateChatMutation)
						.retrieve()
						.bodyToMono(String.class)
						.block();

			JSONObject root = new JSONObject(response);
			String chatId = root.getJSONObject("data").getJSONObject("createChat").getString("id");
			Assertions.assertNotNull(chatId);

			// 2. inviteToChat 호출
			String inviteToChatMutation = String.format("""
				mutation {
						inviteToChat(chatId: "%s", memberIds: ["5"]) {
								members {
										id
								}
						}
				}
				""", chatId);
			String cleanedInviteToChatMutation = inviteToChatMutation.replaceAll("[\n\t]", " ");
			String formattedInviteToChatMutation = String.format("{ \"query\": \"%s\" }", cleanedInviteToChatMutation.replace("\"", "\\\""));
			response = webClient.post()
						.uri("/graphql")
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(formattedInviteToChatMutation)
						.retrieve()
						.bodyToMono(String.class)
						.block();

			root = new JSONObject(response);
			JSONArray members = root.getJSONObject("data").getJSONObject("inviteToChat").getJSONArray("members");
			Assertions.assertEquals(2, members.length());
			Assertions.assertTrue(members.toString().contains("4"));
			Assertions.assertTrue(members.toString().contains("5"));

			// 3. sendMessage 호출
			String sendMessageMutation = String.format("""
				mutation {
						sendMessage(chatId: "%s", body: "Hello, World!") {
								body
						}
				}
				""", chatId);
			String cleanedSendMessageMutation = sendMessageMutation.replaceAll("[\n\t]", " ");
			String formattedSendMessageMutation = String.format("{ \"query\": \"%s\" }", cleanedSendMessageMutation.replace("\"", "\\\""));
			response = webClient.post()
						.uri("/graphql")
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(formattedSendMessageMutation)
						.retrieve()
						.bodyToMono(String.class)
						.block();

			root = new JSONObject(response);
			String messageBody = root.getJSONObject("data").getJSONObject("sendMessage").getString("body");
			Assertions.assertEquals("Hello, World!", messageBody);

			// 4. leaveChat 호출
			String leaveChatMutation = String.format("""
				mutation {
						leaveChat(chatId: "%s") {
								members {
										id
								}
						}
				}
				""", chatId);
			String cleanedLeaveChatMutation = leaveChatMutation.replaceAll("[\n\t]", " ");
			String formattedLeaveChatMutation = String.format("{ \"query\": \"%s\" }", cleanedLeaveChatMutation.replace("\"", "\\\""));
			response = webClient.post()
						.uri("/graphql")
						.contentType(MediaType.APPLICATION_JSON)
						.bodyValue(formattedLeaveChatMutation)
						.retrieve()
						.bodyToMono(String.class)
						.block();

			root = new JSONObject(response);
			members = root.getJSONObject("data").getJSONObject("leaveChat").getJSONArray("members");
			Assertions.assertEquals(1, members.length());
			Assertions.assertTrue(members.toString().contains("5"));
			Assertions.assertFalse(members.toString().contains("4"));
		} catch (JSONException e) {
			Assertions.fail(e.getMessage());
		}
	}
}