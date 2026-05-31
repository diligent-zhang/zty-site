package com.zty.ztysite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zty.ztysite.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    // 只查被文章使用过的标签（DISTINCT 去重）
    @Select("SELECT DISTINCT t.* FROM tags t INNER JOIN article_tags at ON t.id = at.tag_id")
    List<Tag> findAllUsedTags();
}