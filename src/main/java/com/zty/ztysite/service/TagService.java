package com.zty.ztysite.service;

import com.zty.ztysite.entity.Tag;
import com.zty.ztysite.mapper.TagMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagMapper tagMapper;

    public TagService(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    // 获取所有已被使用的标签
    public List<Tag> findAllUsed() {
        return tagMapper.findAllUsedTags();
    }
}