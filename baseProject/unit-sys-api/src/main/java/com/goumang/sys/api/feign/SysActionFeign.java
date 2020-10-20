package com.goumang.sys.api.feign;

import com.goumang.core.web.WebResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.Map;

@FeignClient(name="unit-sys")
@RequestMapping("/sys/action")
public interface SysActionFeign {

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

    /** 添加默认的操作点 */
    @RequestMapping(value = "/addDefault/{funcId}", method = RequestMethod.POST)
    WebResponse addDefault(@PathVariable("funcId") Long funcId);

    /** 分页连接角色 */
    @RequestMapping(value = "/pageJoinRole", method = RequestMethod.POST)
    WebResponse pageJoinRole(@RequestBody Map<String,Object> map);
}
