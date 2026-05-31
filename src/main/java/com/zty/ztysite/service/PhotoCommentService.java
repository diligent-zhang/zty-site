package com.zty.ztysite.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zty.ztysite.entity.PhotoComment;
import com.zty.ztysite.mapper.PhotoCommentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoCommentService {

    private final PhotoCommentMapper mapper;

    public PhotoCommentService(PhotoCommentMapper mapper) {
        this.mapper = mapper;
    }

    public List<PhotoComment> listByPhotoId(Long photoId) {
        LambdaQueryWrapper<PhotoComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PhotoComment::getPhotoId, photoId)
               .orderByAsc(PhotoComment::getCreatedAt);
        return mapper.selectList(wrapper);
    }

    public PhotoComment create(PhotoComment comment) {
        mapper.insert(comment);
        return comment;
    }

    public void delete(Long id) {
        mapper.deleteById(id);
    }
}
