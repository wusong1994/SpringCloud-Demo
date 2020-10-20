package com.goumang.core.base;

import com.goumang.core.util.ParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 基础的Ctrl，提供CURD方法。
 * <p>
 *     继承该类的Controller，需要注入继承{@link BaseService}的Service，且不能声明为私有，
 * 其中Controller的包命名格式xxx.xxx.ctrl，则Service的包命名格式为xxx.xxx.service
 * </p>
 *
 * <p>提供的CURD方法都有前置{@code "pre"}和后置{@code "post"}方法，参数的处理建议写在前置方法，结果的处理建议写在后置方法</p>
 *
 * @author hrb
 * @param <T> 实体的类型，需继承{@link BasePo}
 * @since 1.0
 */
public abstract class BaseCtrl<T extends BasePo> implements Serializable {

    /**
     * 日志
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 基础Service
     */
    private BaseService baseService;

    /**
     * 初始化{@link BaseService}。
     * <p>该方法会在每个方法执行前执行。</p>
     * @throws IllegalAccessException
     * @since 1.0
     */
    @ModelAttribute
    final public void initBaseService() throws IllegalAccessException {
        if(baseService!=null) return;

        Class ctrlClass = this.getClass();
        String ctrlName = ctrlClass.getName();
        String servName = ctrlName.replaceFirst(".ctrl.",".service.");
        servName = servName.substring(0,servName.length()-4) +"Service";
        Field[] fileds = ctrlClass.getDeclaredFields();
        Field field = null;
        for(int i=0;i<fileds.length;i++){
            field = fileds[i];
            field.setAccessible(true);
            if(field.getType().getName().equals(servName) && field.get(this) instanceof BaseService){
                baseService = (BaseService) field.get(this);
                break;
            }
        }
    }

    /**
     * 通过主键查询实体
     * @param id 主键
     * @return 实体
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    final public T info(@PathVariable("id") Object id){
        id = preInfo(id);
        T t = (T) baseService.get(id);
        t = postInfo(id,t);
        return t;
    }

    /**
     * 通过参数查询实体
     * @param po 参数
     * @return 实体
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    final public T info(@RequestBody T po){
        po = preInfo(po);
        T t = (T)baseService.get(po);
        t = postInfo(po,t);
        return t;
    }

    /**
     * 通过参数查询
     * @param po 参数
     * @return 实体的列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
        final public List<T> list(@RequestBody T po){
            po = preList(po);
        List<T> list = baseService.selectForList(po);
        list = postList(po,list);
        return list;
    }

    /**
     * 通过参数分页查询
     * @param po 参数
     * @return 分页对象
     */
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    final public Pager<T> page(@RequestBody T po){
        po = prePage(po);
        Pager<T> pager = baseService.selectForPage(po);
        pager = postPage(po,pager);
        return pager;
    }

    /**
     * 插入
     * @param po 参数
     * @return 持久化实体
     */
    @RequestMapping(method = RequestMethod.POST)
    final public T insert(@RequestBody T po ){
        po = preInsert(po);
        int i = 0;
        if(po.isSelective()) i = baseService.insertSelective(po);
        else i = baseService.insert(po);
        postInsert(po,i);
        return po;
    }

    /**
     * 更新
     * @param po 参数
     * @return 参数
     */
    @RequestMapping( method = RequestMethod.PUT)
    final public T update(@RequestBody T po ){
        ParamUtil.notBlank(po,"pk");
        po = preUpdate(po);
        int i = 0;
        if(po.isSelective()) i = baseService.updateSelective(po);
        else i = baseService.update(po);
        postUpdate(po,i);
        return po;
    }

    /**
     * 通过主键删除
     * @param id 主键
     * @return 1删除成功，0删除失败
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    final public Integer delete(@PathVariable("id") Object id){
        id = preDelete(id);
        int i = baseService.delete(id);
        i = postDelete(id,i);
        return i;
    }

    /**
     * 通过参数删除
     * @param po 实体
     * @return 删除成功数
     */
    @RequestMapping(method = RequestMethod.DELETE)
    final public Integer delete(@RequestBody T po ){
        po = preDelete(po);
        int i = baseService.delete(po);
        i = postDelete(po,i);
        return i;
    }

    /**
     * 通过参数统计
     * @param po
     * @return
     */
    @RequestMapping(value = "/count", method = RequestMethod.POST)
    final public Integer count(@RequestBody T po){
        po = preCount(po);
        int i = baseService.count(po);
        i = postCount(po,i);
        return i;
    }

    /**
     * 查询columnName的最大值，columnName为数据库表的字段名称
     * @param po 参数
     * @return 最大值
     */
    @RequestMapping(value = "/max", method = RequestMethod.POST)
    final public Object max(@RequestBody T po){
        ParamUtil.notBlank(po,"columnName");
        po = preMax(po);
        Object o = baseService.getMax(po,Object.class);
        o = postMax(po,o);
        return o;
    }

    /**
     * 查询columnName的最小值，columnName为数据库表的字段名称
     * @param po
     * @return
     */
    @RequestMapping(value = "/min", method = RequestMethod.POST)
    final public Object min(@RequestBody T po){
        ParamUtil.notBlank(po,"columnName");
        po = preMin(po);
        Object o = baseService.getMin(po,Object.class);
        o = postMin(po,o);
        return o;
    }

    /**
     * 判断是否存在，忽略当前记录
     * @param t
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exist", method = RequestMethod.POST)
    final public Boolean exist(@RequestBody T t) throws Exception{
        return baseService.exist(t);
    }

    //前置方法
    public Object preInfo(Object id){return id;}
    public T preInfo(T po){return po;}
    public T preList(T po){return po;}
    public T prePage(T po){return po;}
    public T preInsert(T po){return po;}
    public T preUpdate(T po){return po;}
    public Object preDelete(Object id){return id;}
    public T preDelete(T po){return po;}
    public T preCount(T po){return po;}
    public T preMax(T po){return po;}
    public T preMin(T po){return po;}
    //后置方法
    public T postInfo(Object id, T result){return result;}
    public T postInfo(T po, T result){return result;}
    public List<T> postList(T po, List<T> result){return result;}
    public Pager<T> postPage(T po, Pager<T> result){return result;}
    public void postInsert(T po, int i){}
    public void postUpdate(T po, int i){}
    public int postDelete(Object id, int result){return result;}
    public int postDelete(T po, int result){return result;}
    public int postCount(T po, int result){return  result;}
    public Object postMax(T po, Object result){return  result;}
    public Object postMin(T po, Object result){return  result;}

}
