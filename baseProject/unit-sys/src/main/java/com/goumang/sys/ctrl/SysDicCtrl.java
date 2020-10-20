package com.goumang.sys.ctrl;

import com.goumang.core.base.BaseCtrl;
import com.goumang.core.util.ErrorUtil;
import com.goumang.core.util.ParamUtil;
import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.po.SysDicPo;
import com.goumang.sys.api.vo.SysDicVo;
import com.goumang.sys.service.SysDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/dic")
public class SysDicCtrl  extends BaseCtrl<SysDicPo> {

    @Autowired
    SysDicService sysDicService;

    @Override
    public SysDicPo preInsert(SysDicPo po) {
        ParamUtil.notBlank(po, "type", "code", "name");
        SysDicPo param = new SysDicPo();
        param.setType(po.getType());
        param.setCode(po.getCode());
        if(!sysDicService.select(param).isEmpty()) ErrorUtil.error("code exists");

        SysDicPo params = new SysDicPo();
        params.setType(po.getType());
        params.setColumnName("sort");
        Integer maxSort = sysDicService.getMax(params,Integer.class);
        maxSort = maxSort == null ? 1: maxSort +1;
        po.setSort(maxSort.longValue());
        return super.preInsert(po);
    }

    @Override
    public SysDicPo preUpdate(SysDicPo po) {
        ParamUtil.noAccept(po, "type", "code","sort");
        return super.preUpdate(po);
    }

    /** 排序 */
    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    public WebResponse sort(@RequestBody SysDicVo vo) throws Exception {
        ParamUtil.notBlank(vo,"pk","fieldName","position");
        SysDicPo po = new SysDicPo();
        po.setPk(vo.getPk());
        po.setFieldName(vo.getFieldName());
        sysDicService.updateSort(po, vo.getPosition());

        return new WebResponse();
    }

    /** 缓存 */
    @RequestMapping(value = "/cache", method = RequestMethod.GET)
    public WebResponse cache(HttpServletRequest request){
        Map<String, List<SysDicPo>> map = sysDicService.cache(request.getServletContext());

        return new WebResponse(map);
    }

    /** 获取同一类型的常量 */
    @RequestMapping(value = "/list/{type}", method = RequestMethod.GET)
    public WebResponse list(@PathVariable("type") String type, HttpServletRequest request){
        List<SysDicPo> list = sysDicService.listByType(request.getServletContext(), type);
        return new WebResponse(list);
    }

    /** 获取常量 */
    @RequestMapping(value = "/get/{type}/{code}", method = RequestMethod.GET)
    public WebResponse get(@PathVariable("type") String type, @PathVariable("code") String code, HttpServletRequest request){
        SysDicPo cont = sysDicService.get(request.getServletContext(), type, code);
        return new WebResponse(cont);
    }

    /** 获取常量名称 */
    @RequestMapping(value = "/getName/{type}/{code}", method = RequestMethod.GET)
    public WebResponse getName(@PathVariable("type") String type, @PathVariable("code") String code, HttpServletRequest request){
        SysDicPo cont = sysDicService.get(request.getServletContext(), type, code);
        return new WebResponse(cont.getName());
    }


}
