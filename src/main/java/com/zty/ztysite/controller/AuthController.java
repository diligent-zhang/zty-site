package com.zty.ztysite.controller;


import com.zty.ztysite.dto.LoginDTO;
import com.zty.ztysite.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController  //所有数据直接返回json数据  controller前后端接口不分离，而rest前后端接口分离，自带@responsebody
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDTO dto, HttpSession session) {
        boolean ok = authService.login(dto.getUsername(), dto.getPassword(), session);
        if (!ok) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
        }
        return Map.of("ok", true, "username", dto.getUsername());
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        authService.logout(session);
        return Map.of("ok", true);
    }

    // 前端刷新页面后调用此接口恢复登录态
    @GetMapping("/status")
    public Map<String, Object> status(HttpSession session) {
        boolean loggedIn = authService.isLoggedIn(session);
        Map<String, Object> result = new HashMap<>();
        result.put("loggedIn", loggedIn);
        result.put("username", loggedIn ? session.getAttribute("username") : null);
        return result;
    }

}
