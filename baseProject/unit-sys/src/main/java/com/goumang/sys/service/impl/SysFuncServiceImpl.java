package com.goumang.sys.service.impl;

import com.goumang.core.base.BaseServiceImpl;
import com.goumang.core.base.Pager;
import com.goumang.core.util.MapperUtil;
import com.goumang.sys.api.po.SysActionPo;
import com.goumang.sys.api.po.SysFuncPo;
import com.goumang.sys.api.vo.SysFuncVo;
import com.goumang.sys.mapper.SysFuncMapper;
import com.goumang.sys.service.SysActionService;
import com.goumang.sys.service.SysFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.List;
import java.util.Map;

@Service
public class SysFuncServiceImpl extends BaseServiceImpl<SysFuncPo> implements SysFuncService {

    @Autowired
    private SysFuncMapper sysFuncMapper;
    @Autowired
    private SysActionService sysActionService;
    @Override
    public List<SysFuncPo> selectForList(SysFuncPo po){
        Weekend wk = Weekend.of(SysFuncPo.class);
        WeekendCriteria<SysFuncPo,Object> criteria = wk.weekendCriteria();

        MapperUtil.andLike(po,criteria,"funcName","funcCode","funcValue");
        MapperUtil.setAndEqual(po,criteria,true);
        MapperUtil.setOrderBy(po,wk);

        List<SysFuncPo> list = select(wk);
        return list;
    }

    @Override
    public List<SysFuncVo> selectByParams(Map<String, Object> map) {
        return sysFuncMapper.selectByParams(map);
    }

    @Override
    public Pager<SysFuncVo> selectForPage(Map<String, Object> map) {
        return selectForPage(o -> selectByParams(o), map);
    }

    @Override
    public void saveAllApi(Map<String, List<String[]>> map) {
        for(String funcCode : map.keySet()){
            SysFuncPo funcPo = get("funcCode",funcCode);
            if(funcPo==null){
                funcPo = new SysFuncPo();
                funcPo.setFuncCode(funcCode);
                insert(funcPo);
            }
            List<String[]> list = map.get(funcCode);
            for(String[] actionArr : list){

                SysActionPo actionPo = new SysActionPo();
                actionPo.setFuncId(funcPo.getFuncId());
                actionPo.setMethod(actionArr[0]);
                actionPo.setActionCode(actionArr[1]);
                if("0".equals(actionArr[2])){
                    sysActionService.delete(actionPo);
                }else{
                    if(sysActionService.get(actionPo)==null) sysActionService.insert(actionPo);
                }
            }
        }
    }

    @Override
    public int delete(Object id) {
        SysActionPo actionPo = new SysActionPo();
        actionPo.setFuncId(Long.parseLong(id.toString()));
        sysActionService.delete(actionPo);
        return super.delete(id);
    }
}
