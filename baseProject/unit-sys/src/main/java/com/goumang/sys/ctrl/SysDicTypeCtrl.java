package com.goumang.sys.ctrl;

import com.goumang.core.base.BaseCtrl;
import com.goumang.core.util.ErrorUtil;
import com.goumang.core.util.ParamUtil;
import com.goumang.sys.api.po.SysDicPo;
import com.goumang.sys.api.po.SysDicTypePo;
import com.goumang.sys.service.SysDicService;
import com.goumang.sys.service.SysDicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sys/dicType")
public class SysDicTypeCtrl  extends BaseCtrl<SysDicTypePo> {

    @Autowired
    SysDicTypeService sysDicTypeService;
    @Autowired
    private SysDicService sysDicService;

    @Override
    public SysDicTypePo preInsert(SysDicTypePo po) {
        ParamUtil.notBlank(po, "code", "name");
        if(sysDicTypeService.exist("code", po.getCode())) ErrorUtil.error("code exists");
        return super.preInsert(po);
    }

    @Override
    public SysDicTypePo preUpdate(SysDicTypePo po) {
        po.setCode(null);
        return super.preUpdate(po);
    }

    @Override
    public Object preDelete(Object id) {
        SysDicTypePo dicType = sysDicTypeService.get(id);
        SysDicPo dic = new SysDicPo();
        dic.setType(dicType.getCode());
        List<SysDicPo> list = sysDicService.selectForList(dic);
        if(!list.isEmpty()) ErrorUtil.error("该类型下有值，不能删除");

        return super.preDelete(id);
    }
}
