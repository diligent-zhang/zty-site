package com.zty.ztysite.controller;

import com.zty.ztysite.entity.StarMessage;
import com.zty.ztysite.service.AuthService;
import com.zty.ztysite.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {
    private  final MessageService messageService;
    private  final AuthService authService;
    public MessageController(MessageService messageService, AuthService authService) {
        this.messageService = messageService;
        this.authService = authService;
    }
    // 公开接口：按视野范围获取留言（GET /api/messages?bounds=lng1,lat1,lng2,lat2）
    @GetMapping("/messages")
    public List<StarMessage> list(
            @RequestParam Double lng1,   // @RequestParam 从 URL 查询参数中取值
            @RequestParam Double lat1,   // 例：/api/messages?lng1=116.3&lat1=39.9&lng2=116.4&lat2=40.0
            @RequestParam Double lng2,
            @RequestParam Double lat2) {
        return messageService.findByBounds(lng1, lat1, lng2, lat2);
    }

    // 公开接口：创建留言
    @PostMapping("/messages")
    public StarMessage create(@RequestBody StarMessage msg,      // @RequestBody 把 JSON 请求体自动转为 Java 对象
                              HttpServletRequest request) {      // HttpServletRequest 代表当前 HTTP 请求
        // 获取客户端真实 IP（考虑了反向代理的情况）
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return messageService.create(msg, ip);
    }

    // 管理端接口：获取所有留言（需登录）
    @GetMapping("/admin/messages")
    public List<StarMessage> adminList(HttpSession session) {
        checkLogin(session);
        return messageService.findAll();
    }

    // 管理端接口：删除留言（需登录）
    @DeleteMapping("/admin/messages/{id}")
    public Map<String, Object> adminDelete(@PathVariable Long id, HttpSession session) {
        // @PathVariable 从 URL 路径中取值，/admin/messages/5 → id=5
        checkLogin(session);
        messageService.delete(id);
        return Map.of("ok", true);
    }

    // 管理端接口：审核通过（需登录）
    @PatchMapping("/admin/messages/{id}/approve")
    public Map<String, Object> approve(@PathVariable Long id, HttpSession session) {
        checkLogin(session);
        messageService.approve(id);
        return Map.of("ok", true);
    }

    // 校验登录态，未登录抛 401
    private void checkLogin(HttpSession session) {
        // 复用 Phase 2 已有的登录校验逻辑
        if (!authService.isLoggedIn(session)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "请先登录");
        }
    }

}


//- @RequestParam vs @PathVariable：@RequestParam 取 URL ? 后面的查询参数（如 ?lng1=116.3），@PathVariable 取 URL 路径中的变量（如
//  /messages/5 中的 5）
//        - @RequestBody：Spring 自动把请求的 JSON 字符串（如 {"nickname":"张三","message":"hello"}）解析成 Java 的 StarMessage 对象，用的是
//Jackson 库
//  - HttpServletRequest：Spring 自动注入当前 HTTP 请求对象，可以从中读取客户端 IP、请求头等信息
//  - X-Forwarded-For：如果有反向代理（Nginx 等），客户端真实 IP 在这个请求头里；没有则从 getRemoteAddr() 取
//  - Map.of("ok", true)：Java 9+ 的快捷创建 Map 方式，返回 {"ok": true} JSON 给前端