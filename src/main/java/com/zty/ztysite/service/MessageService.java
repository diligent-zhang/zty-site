package com.zty.ztysite.service;

import com.zty.ztysite.entity.StarMessage;
import com.zty.ztysite.mapper.StarMessageMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service    // 标记为 Spring 管理的业务 Bean，会被自动创建并注入到需要它的地方
public class MessageService {

    private final StarMessageMapper messageMapper;

    // 构造器注入：Spring 发现构造函数需要 StarMessageMapper，自动传入
    public MessageService(StarMessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    private static final String[] ANON_NAMES = {
            "流浪星尘", "深空旅人", "银河拾荒者", "猎户座访客", "暗物质",
            "彗星尾巴", "星云漫步者", "月光游民", "北极星使", "天鹰座过客",
            "冥王星的秘密", "玫瑰星云", "暴风星旋", "寂夜观测者", "天琴座旅人"
    };

    // 按视野范围查询
    public List<StarMessage> findByBounds(Double lng1, Double lat1,
                                          Double lng2, Double lat2) {
        return messageMapper.findByBounds(lng1, lat1, lng2, lat2);
    }

    // 创建留言
    // ipAddress 从 request 中获取，由 Controller 传入
    public StarMessage create(StarMessage msg, String ipAddress) {
        // 匿名留言：随机生成昵称，用系统随机色相
        if (msg.getIsAnonymous() == 1) {
            msg.setNickname(generateNickname());
            // 匿名 = 冷色系，色相随机 200~280（蓝→紫范围）
            msg.setColorHue(200 + new Random().nextInt(81));
        } else {
            // 实名 = 暖色系，色相随机 20~50（金→橙范围）
            msg.setColorHue(20 + new Random().nextInt(31));
        }
        msg.setIsApproved(1);     // 先发后审，默认审核通过
        msg.setIpAddress(ipAddress);
        messageMapper.insert(msg); // MyBatis-Plus 提供的插入方法
        return msg;               // 返回带 id 的完整对象给前端
    }

    // 删除留言
    public void delete(Long id) {
        messageMapper.deleteById(id);
    }

    // 审核通过
    public void approve(Long id) {
        StarMessage msg = messageMapper.selectById(id);
        if (msg != null) {
            msg.setIsApproved(1);
            messageMapper.updateById(msg);
        }
    }

    // 管理端：查看所有留言（含未审核）
    public List<StarMessage> findAll() {
        return messageMapper.selectList(null); // null = 无过滤条件，查全表
    }

    // 随机生成匿名昵称
    private String generateNickname() {
        Random r = new Random();
        String base = ANON_NAMES[r.nextInt(ANON_NAMES.length)];
        // 加三位随机数字后缀，避免重名：如 "深空旅人#742"
        return base + "#" + (100 + r.nextInt(900));
    }
}
