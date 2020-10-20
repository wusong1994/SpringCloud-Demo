package com.goumang.sys.service;

import com.goumang.core.base.BaseService;
import com.goumang.sys.api.po.SysDicPo;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

public interface SysDicService extends BaseService<SysDicPo> {

    /**
     * 将常量以map的形式缓存并返回
     * @param sc 上下文
     * @return 所有常量
     */
    Map<String, List<SysDicPo>> cache(ServletContext sc);

    /**
     * 从缓存中获取指定类型常量集合
     * @param sc 上下文
     * @param type 类型
     * @return 定类型常量集合
     */
    List<SysDicPo> listByType(ServletContext sc, String type);

    /**
     * 从缓存中获取常量
     * @param sc 上下文
     * @param type 类型
     * @param code 编码
     * @return 常量
     */
    SysDicPo get(ServletContext sc, String type, String code);
}
