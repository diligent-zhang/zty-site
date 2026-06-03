package com.zty.ztysite.controller;

import com.zty.ztysite.dto.ChatRequest;
import com.zty.ztysite.service.ChatService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    // 构造器注入：Spring 自动把 ChatService 传进来
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    /**
     * 聊天接口 — SSE 流式响应
     *
     * produces = TEXT_EVENT_STREAM_VALUE 告诉浏览器"这不是一次性 JSON，
     * 而是一个持续推送的事件流"，浏览器不会在收到数据后关闭连接
     */
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@RequestBody ChatRequest request) {
        // SseEmitter(60秒超时)：如果 60 秒内没数据，连接自动关闭
        SseEmitter emitter = new SseEmitter(60000L);
        // 把 DTO 的 ChatMessage 列表转成 Map 列表，方便 ChatService 处理
        List<Map<String, String>> messages = new ArrayList<>();
        for (ChatRequest.ChatMessage msg : request.getMessages()) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("role", msg.getRole());
            map.put("content", msg.getContent());
            messages.add(map);
        }

        // runAsync：在另一个线程里执行，主线程立即 return emitter
        // 这样 SSE 连接能马上建立，不会被 API 调用阻塞
        CompletableFuture.runAsync(() ->
                chatService.chat(messages, emitter)
        );

        // 立即返回 emitter — Spring 会保持 HTTP 连接打开
        return emitter;
    }
}