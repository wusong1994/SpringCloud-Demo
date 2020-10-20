package com.goumang.core.base;

import com.goumang.core.mapper.ExtMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T>, ExtMapper<T>, IdsMapper<T> {

}
