package com.zty.ztysite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zty.ztysite.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper  //告诉spring这是一个Mybatis Mapper
//mybatis-plus自带基础的增删改查，只需要额外定义不存在的即可
public interface UserMapper extends BaseMapper<User> {
    // BaseMapper 自带的方法已够用，无需额外定义
}