
package com.zty.ztysite.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zty.ztysite.entity.User;
import com.zty.ztysite.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // 登录：查用户 → 验密码 → 写入 Session
    public boolean login(String username, String password, HttpSession session) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username)
        );
        // 用户不存在或密码不匹配
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            return false;
        }
        // 登录成功，Session 中保存用户信息
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        return true;
    }

    // 登出：销毁整个 Session
    public void logout(HttpSession session) {
        session.invalidate();
    }

    // 判断当前请求是否已登录
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }
}
