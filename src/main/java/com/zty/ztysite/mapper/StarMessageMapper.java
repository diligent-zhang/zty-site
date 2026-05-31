package com.zty.ztysite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zty.ztysite.entity.StarMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface StarMessageMapper extends BaseMapper<StarMessage> {

    // 按地图视野范围查询已审核的留言
    //经纬度实现视图视野
    // #{lng1} 等是 MyBatis 参数占位符，@Param 把方法参数绑定到占位符上
    @Select("SELECT * FROM star_messages WHERE is_approved = 1 " +
            "AND longitude BETWEEN #{lng1} AND #{lng2} " +
            "AND latitude BETWEEN #{lat1} AND #{lat2}")
    List<StarMessage> findByBounds(@Param("lng1") Double lng1,
                                   @Param("lat1") Double lat1,
                                   @Param("lng2") Double lng2,
                                   @Param("lat2") Double lat2);
}

// - extends BaseMapper<StarMessage>：MyBatis-Plus 的核心接口，继承后自动获得 insert()、selectById()、selectList()、deleteById()
//等常用方法，不需要写 SQL
//  - @Select(...)：这个查询比较特殊（按地理范围查），所以手动写 SQL。MyBatis 会把 SELECT * 的列自动映射到 StarMessage 的字段上
//  - #{lng1}：MyBatis 的参数占位符，会被替换成方法参数值，同时自动防 SQL 注入（底层是 PreparedStatement）
//        - @Param("lng1")：把方法参数名绑定到 SQL 占位符。Java 编译后不保留参数名，所以必须用这个注解显式指定