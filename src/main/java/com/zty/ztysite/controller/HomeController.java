package com.zty.ztysite.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    @GetMapping("/profile")
    public Map<String,Object> getProfile(){
        Map<String,Object> profile = new LinkedHashMap<>();
        profile.put("name","zty");
        profile.put("title","网络工程 @南京工程学院");
        profile.put("subtitle","Java后端开发 | 目前主攻java开发，同时向AI Agent开发倾斜");
        profile.put("description","信奉实用主义，渴望以技术为杠杆放大产能");
        return profile;

    }
    @GetMapping("/education")
    public List<Map<String,Object>> getEducation(){
        List<Map<String,Object>> timeline = new ArrayList<>();
        Map<String,Object> primary = new LinkedHashMap<>();
        primary.put("school","苇子园小学");
        primary.put("period","小学阶段");
        primary.put("description","少年的起点");
        timeline.add(primary);

        Map<String, Object> junior = new LinkedHashMap<>();
        junior.put("school", "欢口育英初级中学");
        junior.put("period", "2017 — 2020");
        junior.put("type", "初中阶段");
        junior.put("description", "人生轨迹开始向上，从落后的差生走向了好学生的标榜"
                + "逐步掌握科学的学习方法，夯实各科知识基础，"
                + "养成了按时完成任务、主动思考问题的学习习惯。");
        timeline.add(junior);
        Map<String, Object> senior = new LinkedHashMap<>();
        senior.put("school", "丰县民族中学");
        senior.put("period", "2020 — 2023");
        senior.put("type", "高中阶段");
        senior.put("description", "3年与疫情为伴，从一开始就歇斯底里的努力，只为高考"
                + "学习氛围中不断突破自我，深化知识体系，"
                + "提升自主学习与时间管理能力。");
        timeline.add(senior);

        Map<String, Object> university = new LinkedHashMap<>();
        university.put("school", "南京工程学院");
        university.put("period", "2023 — 至今");
        university.put("type", "网络工程 (本科)");
        university.put("description", "迷茫的挣扎，向上的思想与堕落的行为相重合的阶段"
                );
        timeline.add(university);

        return timeline;
    }


    @GetMapping("/skills")
    public Map<String, Object> getSkills() {
        Map<String, Object> skills = new LinkedHashMap<>();

        skills.put("coreTech", List.of(
                "JavaWeb", "springboot", "redis",
                "区块链开发(web3j)", "LLM应用开发"
        ));

        skills.put("tools", List.of(
                "影刀 RPA", "八爪鱼", "Claude API", "Kimi",
                "NotebookLM", "飞书多维表格"
        ));
        //均为盗用
        skills.put("values", List.of(
                "技术真诚主义", "长期主义者", "视觉逻辑控"
        ));

        return skills;
    }



}
