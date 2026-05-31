package com.zty.ztysite.controller;

import com.zty.ztysite.entity.PhotoComment;
import com.zty.ztysite.service.PhotoCommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PhotoCommentController {

    private final PhotoCommentService service;

    public PhotoCommentController(PhotoCommentService service) {
        this.service = service;
    }

    @GetMapping("/api/photos/{photoId}/comments")
    public List<PhotoComment> list(@PathVariable Long photoId) {
        return service.listByPhotoId(photoId);
    }

    @PostMapping("/api/photos/{photoId}/comments")
    public PhotoComment create(@PathVariable Long photoId,
                               @RequestBody PhotoComment comment,
                               HttpServletRequest request) {
        comment.setPhotoId(photoId);
        if (comment.getNickname() == null || comment.getNickname().isBlank()) {
            comment.setNickname("访客");
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        comment.setIp(ip);
        return service.create(comment);
    }

    @DeleteMapping("/api/admin/comments/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "ok";
    }
}
