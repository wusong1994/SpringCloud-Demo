package com.goumang.sys.service;

import com.goumang.core.base.BaseService;
import com.goumang.core.base.Pager;
import com.goumang.sys.api.po.SysFuncPo;
import com.goumang.sys.api.vo.SysFuncVo;

import java.util.List;
import java.util.Map;

public interface SysFuncService extends BaseService<SysFuncPo> {

    /**
     * 通过参数查询
     * @param map
     * @return
     */
    List<SysFuncVo> selectByParams(Map<String, Object> map);

    Pager<SysFuncVo> selectForPage(Map<String, Object> map);

    /**
     * 保存所有api
     * @param map key--funcCode,value--action，第一个值为method，第二个值为actionCode,第三个值为0或1,1保存，0删除
     */
    void saveAllApi(Map<String, List<String[]>> map);
}
