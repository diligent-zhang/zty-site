package com.zty.ztysite.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zty.ztysite.entity.Passion;
import com.zty.ztysite.mapper.PassionMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PassionService {
    private final PassionMapper passionMapper;

    public PassionService(PassionMapper passionMapper) {
        this.passionMapper = passionMapper;
    }

    public List<Passion> listAll() {
        return passionMapper.selectList(
                new LambdaQueryWrapper<Passion>().orderByAsc(Passion::getSortOrder));
    }

    public Passion create(Passion passion) {
        passionMapper.insert(passion);
        return passion;
    }

    public Passion update(Passion passion) {
        passionMapper.updateById(passion);
        return passionMapper.selectById(passion.getId());
    }

    public void delete(Long id) {
        passionMapper.deleteById(id);
    }
}
