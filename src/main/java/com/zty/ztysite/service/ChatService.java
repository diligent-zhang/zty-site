package com.zty.ztysite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ChatService {

    @Value("${deepseek.api-key}")
    private String apiKey;

    @Value("${deepseek.api-url}")
    private String apiUrl;

    @Value("${deepseek.model}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Object> knowledge;

    @PostConstruct
    public void loadKnowledge() {
        try {
            ClassPathResource resource = new ClassPathResource("knowledge.json");
            knowledge = objectMapper.readValue(resource.getInputStream(), Map.class);
        } catch (Exception e) {
            knowledge = Map.of("name", "AI助手");
        }
    }

    public void chat(List<Map<String, String>> messages, SseEmitter emitter) {
        try {
            String systemPrompt = buildSystemPrompt();

            List<Map<String, String>> fullMessages = new ArrayList<>();
            Map<String, String> systemMsg = new LinkedHashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", systemPrompt);
            fullMessages.add(systemMsg);
            fullMessages.addAll(messages);

            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("model", model);
            requestBody.put("messages", fullMessages);
            requestBody.put("stream", true);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 1000);

            String requestJson = objectMapper.writeValueAsString(requestBody);

            HttpURLConnection conn = (HttpURLConnection) URI.create(apiUrl).toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(60000);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestJson.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            if (status != 200) {
                emitter.send(SseEmitter.event()
                        .name("error")
                        .data("AI 服务返回错误，状态码: " + status));
                emitter.complete();
                return;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("data: ")) {
                        String data = line.substring(6).trim();
                        if ("[DONE]".equals(data)) break;
                        try {
                            Map<String, Object> chunk = objectMapper.readValue(data, Map.class);
                            List<Map<String, Object>> choices = (List<Map<String, Object>>) chunk.get("choices");
                            if (choices != null && !choices.isEmpty()) {
                                Map<String, Object> delta = (Map<String, Object>) choices.get(0).get("delta");
                                if (delta != null) {
                                    Object content = delta.get("content");
                                    if (content != null && !content.toString().isEmpty()) {
                                        emitter.send(SseEmitter.event()
                                                .name("token")
                                                .data(Map.of("content", content.toString())));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // 跳过解析失败的行
                        }
                    }
                }
            }

            emitter.send(SseEmitter.event().name("done").data("complete"));
            emitter.complete();

        } catch (Exception e) {
            try {
                emitter.send(SseEmitter.event().name("error").data("分身正在休息，请稍后再试"));
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private String buildSystemPrompt() {
        StringBuilder sb = new StringBuilder();
        String name = (String) knowledge.getOrDefault("name", "AI助手");

        sb.append("你是").append(name).append("的数字分身，请以第一人称'我'回答。\n\n");
        sb.append("## 你的基本信息\n");
        sb.append("- 姓名：").append(name).append("\n");
        sb.append("- 定位：").append(knowledge.getOrDefault("role", "")).append("\n");
        sb.append("- 自我介绍：").append(knowledge.getOrDefault("bio", "")).append("\n\n");

        List<String> skills = (List<String>) knowledge.get("skills");
        if (skills != null && !skills.isEmpty()) {
            sb.append("## 你的技能\n");
            for (String skill : skills) sb.append("- ").append(skill).append("\n");
            sb.append("\n");
        }

        List<Map<String, String>> education = (List<Map<String, String>>) knowledge.get("education");
        if (education != null && !education.isEmpty()) {
            sb.append("## 教育经历\n");
            for (Map<String, String> edu : education) {
                sb.append("- ").append(edu.get("school"))
                        .append(" / ").append(edu.get("major"))
                        .append(" / ").append(edu.get("year")).append("\n");
            }
            sb.append("\n");
        }

        List<String> interests = (List<String>) knowledge.get("interests");
        if (interests != null && !interests.isEmpty()) {
            sb.append("## 兴趣爱好\n");
            for (String i : interests) sb.append("- ").append(i).append("\n");
            sb.append("\n");
        }

        Map<String, String> contact = (Map<String, String>) knowledge.get("contact");
        if (contact != null) {
            sb.append("## 联系方式\n");
            sb.append("- 邮箱：").append(contact.getOrDefault("email", "")).append("\n");
            sb.append("- GitHub：").append(contact.getOrDefault("github", "")).append("\n\n");
        }

        sb.append("## 对话规则\n");
        sb.append("1. 以第一人称\"我\"回答，你就是").append(name).append("本人\n");
        sb.append("2. 说话风格：").append(knowledge.getOrDefault("speaking_style", "简洁直接")).append("\n");
        sb.append("3. 性格：").append(knowledge.getOrDefault("personality", "务实")).append("\n");
        sb.append("4. 遇到不知道的事情如实说不知道，不要编造\n");
        sb.append("5. 可以用 Markdown 格式化回复\n");
        sb.append("6. 每次回复控制在 200 字以内\n");

        return sb.toString();
    }
}