package com.zty.ztysite.controller;

import com.zty.ztysite.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class FileUploadController {

    private final AuthService authService;

    // 上传目录：项目根目录下的 uploads/
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    public FileUploadController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/upload")
    public Map<String, String> upload(@RequestParam("file") MultipartFile file,
                                      HttpSession session) throws IOException {
        checkLogin(session);

        // 安全检查：只允许图片和视频
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.startsWith("video/"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "只允许上传图片或视频文件");
        }

        // 生成唯一文件名
        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }
        String newName = UUID.randomUUID().toString() + ext;

        // 确保目录存在
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        // 保存文件
        File dest = new File(dir, newName);
        file.transferTo(dest);

        // 返回可访问的 URL
        return Map.of("url", "/uploads/" + newName, "filename", newName);
    }

    private void checkLogin(HttpSession session) {
        if (!authService.isLoggedIn(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "请先登录");
    }
}
