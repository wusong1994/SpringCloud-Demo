package com.goumang.sys.ctrl;

import cn.hutool.crypto.digest.DigestUtil;
import com.goumang.core.base.BaseCtrl;
import com.goumang.core.base.Pager;
import com.goumang.core.util.ErrorUtil;
import com.goumang.core.util.ParamUtil;
import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.po.SysUserPo;
import com.goumang.sys.api.vo.SysUserVo;
import com.goumang.sys.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class SysUserCtrl  extends BaseCtrl<SysUserPo> {

    @Autowired
    SysUserService sysUserService;

    @Override
    public SysUserPo preInsert(SysUserPo po) {
        ParamUtil.notBlank(po,"loginName","password");
        if(sysUserService.exist("loginName",po.getLoginName())) ErrorUtil.error("loginName is exists");

        po.setPassword(DigestUtil.md5Hex(po.getPassword()));
        po.setStatus("1");
        po.setCreateDate(new Date());
        po.setOperDate(new Date());
        return super.preInsert(po);
    }

    @Override
    public SysUserPo preUpdate(SysUserPo po) {
        po.setSelective(true);
        po.setCreateDate(null);
        po.setPassword(null);

        po.setOperDate(new Date());
        return super.preUpdate(po);
    }

    /** 登录 */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public WebResponse login(@RequestBody SysUserPo po){
        ParamUtil.notBlank(po,"loginName","password");
        SysUserPo user = sysUserService.get("loginName",po.getLoginName());
        if(user==null) ErrorUtil.error("loginName error");
        String pwd = DigestUtil.md5Hex(po.getPassword());
        if(!pwd.equals(user.getPassword())) ErrorUtil.error("password error");
        if("0".equals(user.getStatus())) ErrorUtil.error("forbidden login");

        return new WebResponse(user);
    }

    /** 更新密码 */
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public WebResponse updatePwd(@RequestBody Map<String,Object> map){
        ParamUtil.notBlank(map,"userId","oldPwd","newPwd","confirmPwd");
        SysUserPo user = sysUserService.get(map.get("userId"));
        String newPwd = (String)map.get("newPwd");
        String confirmPwd = (String)map.get("confirmPwd");
        if(!newPwd.equals(confirmPwd)) ErrorUtil.error("confirm password error");
        if(!user.getPassword().equals(DigestUtil.md5Hex((String)map.get("oldPwd")))) ErrorUtil.error("old password error");

        SysUserPo po = new SysUserPo();
        po.setUserId(user.getUserId());
        po.setPassword(DigestUtil.md5Hex(newPwd));
        po.setOperDate(new Date());

        sysUserService.updateSelective(po);
        return new WebResponse();
    }

    /** 重置密码 */
    @RequestMapping(value = "/resetPwd", method = RequestMethod.PUT)
    public WebResponse resetPwd(@RequestBody SysUserPo po){
        ParamUtil.notBlank(po, "userId");
        if(StringUtils.isBlank(po.getPassword())) po.setPassword("123456");
        ParamUtil.accept(po,"userId", "password");
        po.setPassword(DigestUtil.md5Hex(po.getPassword()));

        return new WebResponse(sysUserService.updateSelective(po));
    }

    /** 使用户无效 */
    @RequestMapping(value = "/disable/{userId}", method = RequestMethod.PUT)
    public WebResponse disable(@PathVariable("userId") Long userId){
        SysUserPo user = new SysUserPo();
        user.setUserId(userId);
        user.setStatus("0");
        return new WebResponse(sysUserService.updateSelective(user));
    }

    /** 使用户有效 */
    @RequestMapping(value = "/enable/{userId}", method = RequestMethod.PUT)
    public WebResponse enable(@PathVariable("userId") Long userId){
        SysUserPo user = new SysUserPo();
        user.setUserId(userId);
        user.setStatus("1");
        return new WebResponse(sysUserService.updateSelective(user));
    }

    /** 连接分页 */
    @RequestMapping(value = "/joinPage", method = RequestMethod.POST)
    public WebResponse joinPage(@RequestBody Map<String,Object> map){
        ParamUtil.notBlank(map,"roleId");
        Pager<SysUserVo> pager = sysUserService.selectForPage(map);
        return new WebResponse(pager);
    }

}
