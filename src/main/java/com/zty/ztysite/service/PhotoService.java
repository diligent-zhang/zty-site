package com.zty.ztysite.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zty.ztysite.entity.Photo;
import com.zty.ztysite.mapper.PhotoMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PhotoService {
    private final PhotoMapper photoMapper;

    public PhotoService(PhotoMapper photoMapper) {
        this.photoMapper = photoMapper;
    }

    public List<Photo> listAll() {
        return photoMapper.selectList(
                new LambdaQueryWrapper<Photo>().orderByAsc(Photo::getSortOrder));
    }

    public Photo create(Photo photo) {
        photoMapper.insert(photo);
        return photo;
    }

    public Photo update(Photo photo) {
        photoMapper.updateById(photo);
        return photoMapper.selectById(photo.getId());
    }

    public void delete(Long id) {
        photoMapper.deleteById(id);
    }
}