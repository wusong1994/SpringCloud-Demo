package com.goumang.sys.api.feign;

import com.goumang.core.web.WebResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name="unit-sys")
@RequestMapping("/sys/user")
public interface SysUserFeign {

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    WebResponse info(@PathVariable("id") Long id);

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    WebResponse info(@RequestBody Map<String,Object> map);

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    WebResponse list(@RequestBody Map<String,Object> map);

    @RequestMapping(value = "/page", method = RequestMethod.POST)
    WebResponse page(@RequestBody Map<String,Object> map);

    @RequestMapping(method = RequestMethod.POST)
    WebResponse insert(@RequestBody Map<String,Object> map);

    @RequestMapping(method = RequestMethod.PUT)
    WebResponse update(@RequestBody Map<String,Object> map);

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    WebResponse delete(@PathVariable("id") Long id);

    @RequestMapping(value = "/count", method = RequestMethod.POST)
    WebResponse count(@RequestBody Map<String,Object> map);

    @RequestMapping(value = "/exist", method = RequestMethod.POST)
    WebResponse exist(@RequestBody Map<String,Object> map);

    @RequestMapping(value = "/max", method = RequestMethod.POST)
    WebResponse max(@RequestBody Map<String,Object> map);

    @RequestMapping(value = "/min", method = RequestMethod.POST)
    WebResponse min(@RequestBody Map<String,Object> map);

    /** 登录 */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    WebResponse login(@RequestBody Map<String,Object> map);

    /** 更新密码 */
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    WebResponse updatePwd(@RequestBody Map<String,Object> map);

    /** 重置密码 */
    @RequestMapping(value = "/resetPwd", method = RequestMethod.PUT)
    WebResponse resetPwd(@RequestBody Map<String,Object> map);

    /** 使用户无效 */
    @RequestMapping(value = "/disable/{userId}", method = RequestMethod.PUT)
    WebResponse disable(@PathVariable("userId") Long userId);

    /** 使用户有效 */
    @RequestMapping(value = "/enable/{userId}", method = RequestMethod.PUT)
    WebResponse enable(@PathVariable("userId") Long userId);

    /** 连接分页 */
    @RequestMapping(value = "/joinPage", method = RequestMethod.POST)
    WebResponse joinPage(@RequestBody Map<String,Object> map);
}
