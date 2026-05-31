package com.zty.ztysite.controller;

import com.zty.ztysite.entity.Passion;
import com.zty.ztysite.service.AuthService;
import com.zty.ztysite.service.PassionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PassionController {

    private final PassionService passionService;
    private final AuthService authService;

    public PassionController(PassionService passionService, AuthService authService) {
        this.passionService = passionService;
        this.authService = authService;
    }

    @GetMapping("/passions")
    public List<Passion> list() {
        return passionService.listAll();
    }

    @PostMapping("/admin/passions")
    public Passion create(@RequestBody Passion passion, HttpSession session) {
        checkLogin(session);
        return passionService.create(passion);
    }

    @PutMapping("/admin/passions/{id}")
    public Passion update(@PathVariable Long id, @RequestBody Passion passion,
                          HttpSession session) {
        checkLogin(session);
        passion.setId(id);
        return passionService.update(passion);
    }

    @DeleteMapping("/admin/passions/{id}")
    public Map<String, Object> delete(@PathVariable Long id, HttpSession session) {
        checkLogin(session);
        passionService.delete(id);
        return Map.of("ok", true);
    }

    private void checkLogin(HttpSession session) {
        if (!authService.isLoggedIn(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "请先登录");
    }
}
