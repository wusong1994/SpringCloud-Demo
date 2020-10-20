package com.goumang.core.mapper;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;
import java.util.Map;


@RegisterMapper
public interface ExtMapper<T> {

    /**
     * 查询最大值
     * @param t 实体
     * @return
     */
    @SelectProvider(type = ExtMapperProvider.class, method = "dynamicSQL")
    Object selectMax(T t);

    /**
     * 查询最小值
     * @param t 实体
     * @return
     */
    @SelectProvider(type = ExtMapperProvider.class, method = "dynamicSQL")
    Object selectMin(T t);

    /**
     * 按组统计
     * @param t
     * @return
     */
    @SelectProvider(type = ExtMapperProvider.class, method = "dynamicSQL")
    List<Map> selectCountGroupBy(T t);
}
