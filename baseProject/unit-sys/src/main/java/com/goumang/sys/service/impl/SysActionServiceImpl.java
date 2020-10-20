package com.goumang.sys.service.impl;

import com.goumang.core.base.BaseServiceImpl;
import com.goumang.core.base.Pager;
import com.goumang.core.util.MapperUtil;
import com.goumang.sys.api.vo.SysActionVo;
import com.goumang.sys.mapper.SysActionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;
import com.goumang.sys.api.po.SysActionPo;
import com.goumang.sys.service.SysActionService;

import java.util.List;
import java.util.Map;

@Service
public class SysActionServiceImpl extends BaseServiceImpl<SysActionPo> implements SysActionService {

    @Autowired
    private SysActionMapper sysActionMapper;

    @Override
    public List<SysActionPo> selectForList(SysActionPo po){
        Weekend wk = Weekend.of(SysActionPo.class);
        WeekendCriteria<SysActionPo,Object> criteria = wk.weekendCriteria();

        MapperUtil.setAndEqual(po,criteria,true);
        MapperUtil.setOrderBy(po,wk);

        List<SysActionPo> list = select(wk);
        return list;
    }

    @Override
    public int addDefault(Long funcId) {
        int result = 0;
        String[] codeArr = new String[]{"insert","delete","update","query"};
        String[] nameArr = new String[]{"增加","删除","修改","查询"};
        for(int i = 0; i < codeArr.length; i++){
            SysActionPo po = new SysActionPo();
            po.setFuncId(funcId);
            po.setActionCode(codeArr[i]);
            if(this.get(po)!=null) continue;
            po.setActionName(nameArr[i]);
            result += this.insertSelective(po);
        }
        return result;
    }

    @Override
    public List<SysActionVo> selectByParams(Map<String, Object> map) {
        return sysActionMapper.selectByParams(map);
    }

    @Override
    public Pager<SysActionVo> selectForPage(Map<String, Object> map) {
        return this.selectForPage(o -> selectByParams(o), map);
    }


}
