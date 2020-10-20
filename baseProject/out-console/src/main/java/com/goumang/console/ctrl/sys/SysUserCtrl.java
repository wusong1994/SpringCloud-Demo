package com.goumang.console.ctrl.sys;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.goumang.core.annotation.NoLogin;
import com.goumang.core.annotation.NoPermit;
import com.goumang.core.util.ParamUtil;
import com.goumang.core.util.SessionUtil;
import com.goumang.core.web.WebResponse;
import com.goumang.sys.api.feign.SysUserFeign;
import com.goumang.sys.api.feign.SysUserRoleFeign;
import com.goumang.sys.api.po.SysUserPo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * By huang.rb on 2019/8/7
 */
@RestController
@RequestMapping("/console/sys/user")
public class SysUserCtrl {

    @Autowired
    private SysUserFeign sysUserFeign;
    @Autowired
    private SysUserRoleFeign sysUserRoleFeign;

    /** 信息 */
    @NoPermit
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public WebResponse info(@PathVariable("id") Long id){
        return sysUserFeign.info(id);
    }

    /** 分页 */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public WebResponse page(@RequestBody Map<String,Object> po){
        return sysUserFeign.page(po);
    }

    /** 注册 */
    @NoLogin
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public WebResponse register(@RequestBody Map<String,Object> map){
        return sysUserFeign.insert(map);
    }
    
    /** 获取验证码 */
    @NoLogin
    @RequestMapping(value = "/getCaptcha", method = RequestMethod.GET)
    public void getCaptcha(HttpServletResponse response) throws Exception {
        try {
            ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200,100,4,3);
            captcha.write(response.getOutputStream());

            SessionUtil.setAttribute("_captcha:image",captcha.getCode());
        } finally {
            response.getOutputStream().close();
        }
    }
    
    /** 登录 */
    @NoLogin
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public WebResponse login(@RequestBody Map<String,Object> map){
        ParamUtil.notBlank(map,"loginName","password");

        WebResponse webResponse = sysUserFeign.login(map);
        SysUserPo user = webResponse.getData(SysUserPo.class);
        if(user!=null){
            SessionUtil.setUser(user);
            Map<String, List<String>> permissions = (Map<String, List<String>>) sysUserRoleFeign.permission(user.getUserId()).getData();
            SessionUtil.setPermissions(permissions);
        }

        return webResponse;
    }

    /** 获取登录用户 */
    @NoPermit
    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public WebResponse getUser(){
        return new WebResponse(SessionUtil.getUser());
    }

    /** 获取登录用户权限 */
    @NoPermit
    @RequestMapping(value = "/getPermissions", method = RequestMethod.GET)
    public WebResponse getPermissions(){
        return new WebResponse(SessionUtil.getPermissions());
    }

    /** 新增 */
    @RequestMapping(method = RequestMethod.POST)
    public WebResponse insert(@RequestBody Map<String,Object> map){
        String password = (String) map.get("password");
        if(StringUtils.isBlank(password)) map.put("password","123456");

        WebResponse wr = sysUserFeign.insert(map);
        if(!"success".equals(wr.getCode())) return wr;
        SysUserPo user = wr.getData(SysUserPo.class);

        Map<String,Object> params = new HashMap<>();
        params.put("userId",user.getUserId());
        params.put("roleIds",map.get("roleIds"));
        sysUserRoleFeign.batchSave(params);

        return wr;
    }

    /** 更新 */
    @RequestMapping(method = RequestMethod.PUT)
    public WebResponse update(@RequestBody Map<String,Object> map){
        WebResponse wr = sysUserFeign.update(map);
        if(!"success".equals(wr.getCode())) return wr;

        SysUserPo user = wr.getData(SysUserPo.class);
        Map<String,Object> params = new HashMap<>();
        params.put("userId",user.getUserId());
        params.put("roleIds",map.get("roleIds"));
        sysUserRoleFeign.batchSave(params);
        return wr;
    }

    /** 更新个人信息 */
    @NoPermit
    @RequestMapping(value = "/updateMyself", method = RequestMethod.PUT)
    public WebResponse updateMyself(@RequestBody Map<String,Object> map){
        map.put("userId",SessionUtil.getUserId(Long.class));
        sysUserFeign.update(map);
        SysUserPo user = sysUserFeign.info(SessionUtil.getUserId(Long.class)).getData(SysUserPo.class);
        SessionUtil.setUser(user);

        return new WebResponse(user);
    }

    /** 更新密码 */
    @NoPermit
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public WebResponse updatePwd(@RequestBody Map<String,Object> map){
        map.put("userId",SessionUtil.getUserId());
        return sysUserFeign.updatePwd(map);
    }

    /** 重置密码 */
    @RequestMapping(value = "/resetPwd", method = RequestMethod.PUT)
    public WebResponse resetPwd(@RequestBody Map<String,Object> map){
        return sysUserFeign.resetPwd(map);
    }

    /** 使用户无效 */
    @RequestMapping(value = "/disable/{userId}", method = RequestMethod.PUT)
    public WebResponse disable(@PathVariable("userId") Long userId){
        return sysUserFeign.disable(userId);
    }

    /** 使用户有效 */
    @RequestMapping(value = "/enable/{userId}", method = RequestMethod.PUT)
    public WebResponse enable(@PathVariable("userId") Long userId){
        return sysUserFeign.enable(userId);
    }

    /** 登出 */
    @NoPermit
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public WebResponse logout(){
        SessionUtil.logout();
        return new WebResponse();
    }

    /** 是否存在 */
    @NoPermit
    @RequestMapping(value = "/exist", method = RequestMethod.POST)
    public WebResponse exist(@RequestBody Map<String,Object> map){
        return sysUserFeign.exist(map);
    }

    /** 连接分页 */
    @RequestMapping(value = "/joinPage", method = RequestMethod.POST)
    public WebResponse joinPage(@RequestBody Map<String,Object> map){
        return sysUserFeign.joinPage(map);
    }
}
