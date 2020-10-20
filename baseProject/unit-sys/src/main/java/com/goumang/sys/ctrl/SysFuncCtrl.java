package com.goumang.sys.ctrl;

import com.goumang.core.base.BaseCtrl;
import com.goumang.core.util.ParamUtil;
import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.po.SysFuncPo;
import com.goumang.sys.service.SysFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/func")
public class SysFuncCtrl  extends BaseCtrl<SysFuncPo> {

    @Autowired
    SysFuncService sysFuncService;

    /** 分页连接角色 */
    @RequestMapping(value = "/pageJoinRole", method = RequestMethod.POST)
    public WebResponse pageJoin(@RequestBody Map<String,Object> map){
        ParamUtil.notBlank(map,"roleId");
        return new WebResponse(sysFuncService.selectForPage(map));
    }

    /** 批量操作 */
    @RequestMapping(value = "/saveAllApi", method = RequestMethod.POST)
    public WebResponse saveAllApi(@RequestBody Map<String, List<String[]>> map){
        sysFuncService.saveAllApi(map);
        return new WebResponse();
    }


}
