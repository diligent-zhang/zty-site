package com.zty.ztysite.controller;

import com.zty.ztysite.entity.Photo;
import com.zty.ztysite.service.AuthService;
import com.zty.ztysite.service.PhotoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PhotoController {

    private final PhotoService photoService;
    private final AuthService authService;

    public PhotoController(PhotoService photoService, AuthService authService) {
        this.photoService = photoService;
        this.authService = authService;
    }

    // 公开：获取所有照片
    @GetMapping("/photos")
    public List<Photo> list() {
        return photoService.listAll();
    }

    // 管理端：新增
    @PostMapping("/admin/photos")
    public Photo create(@RequestBody Photo photo, HttpSession session) {
        checkLogin(session);
        return photoService.create(photo);
    }

    // 管理端：更新
    @PutMapping("/admin/photos/{id}")
    public Photo update(@PathVariable Long id, @RequestBody Photo photo,
                        HttpSession session) {
        checkLogin(session);
        photo.setId(id);
        return photoService.update(photo);
    }

    // 管理端：删除
    @DeleteMapping("/admin/photos/{id}")
    public Map<String, Object> delete(@PathVariable Long id, HttpSession session) {
        checkLogin(session);
        photoService.delete(id);
        return Map.of("ok", true);
    }

    private void checkLogin(HttpSession session) {
        if (!authService.isLoggedIn(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "请先登录");
    }
}