package com.zty.ztysite.dto;

import java.util.List;

public class ChatRequest {
    private List<ChatMessage> messages;

    public List<ChatMessage> getMessages() { return messages; }
    public void setMessages(List<ChatMessage> messages) { this.messages = messages; }

    public static class ChatMessage {
        private String role;
        private String content;

        public String getRole() { return role; }
        public String getContent() { return content; }
        public void setRole(String role) { this.role = role; }
        public void setContent(String content) { this.content = content; }
    }
}
