package com.goumang.core.base;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.github.pagehelper.PageInfo;
import com.goumang.core.annotation.Path;
import com.goumang.core.emun.PathEnum;
import com.goumang.core.template.OssTemplate;
import com.goumang.core.util.ErrorUtil;
import com.goumang.core.util.MapperUtil;
import com.goumang.core.util.PageUtil;
import com.goumang.core.util.ParamUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import javax.persistence.Id;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * <p>{@link BaseService}的实现类
 *
 * @author hrb
 * @param <T> 实体的类型，需继承{@link BasePo}
 * @since 1.0
 */
public abstract class BaseServiceImpl<T extends BasePo> implements BaseService<T> {

    @Autowired(required = false)
    private OssTemplate ossTemplate;

    private Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Autowired
    protected BaseMapper<T> mapper;

    @Override
    public int insert(T t) {
        return persistFile((o,u)->mapper.insert(o), t,null);
    }

    @Override
    public int insertSelective(T t) {
        return persistFile((o,u)->mapper.insertSelective(o), t,null);
    }

    @Override
    public int update(T t) {
        return persistFile((o,u)->mapper.updateByPrimaryKey(o), t,null);
    }

    @Override
    public int updateSelective(T t) {
        return persistFile((o,u)->mapper.updateByPrimaryKeySelective(o), t,null);
    }

    @Override
    public int updateByExample(T t, Example example) {
        return persistFile((p,u)->mapper.updateByExample(p, u),t,example);
    }

    @Override
    public int updateByExampleSelective(T t, Example example) {
        return  persistFile((p,u)->mapper.updateByExampleSelective(p, u), t, example);
    }

    @Override
    public void updateSort(T t, Integer position){
        try{
            ParamUtil.notBlank(t,"pk","fieldName");
            Object pk = t.getPk();
            t.setPk(null);
            String fieldName = t.getFieldName();
            T po = this.get(pk);
            if(po ==null) return;

            PropertyDescriptor pd = new PropertyDescriptor(fieldName,t.getClass());
            Method setMethod = pd.getWriteMethod();
            Method getMethod = pd.getReadMethod();

            //则比较设置的序号和数据库的序号，查询在俩者之间的列表，做+1或-1操作，并更新
            int currPosition = Integer.parseInt(getMethod.invoke(po).toString());

            if(currPosition == position) return;
            if(currPosition>position){
                Weekend wk = Weekend.of(t.getClass());
                WeekendCriteria<T,Object> criteria = wk.weekendCriteria();
                criteria.andGreaterThanOrEqualTo(fieldName,position);
                criteria.andLessThan(fieldName,currPosition);
                MapperUtil.setAndEqual(t,criteria);

                List<T> list = select(wk);
                list.parallelStream().forEach(o->{
                    try {
                        setMethod.invoke(o,(Long)getMethod.invoke(o) + 1);
                        mapper.updateByPrimaryKeySelective(o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }else{
                Weekend wk = Weekend.of(t.getClass());
                WeekendCriteria<T,Object> criteria = wk.weekendCriteria();
                criteria.andGreaterThan(fieldName,currPosition);
                criteria.andLessThanOrEqualTo(fieldName,position);
                MapperUtil.setAndEqual(t,criteria);

                List<T> list = select(wk);
                list.parallelStream().forEach(o->{
                    try {
                        setMethod.invoke(o,(Long)getMethod.invoke(o) -1);
                        mapper.updateByPrimaryKeySelective(o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            T p = (T) t.getClass().newInstance();
            p.setPk(pk);
            setMethod.invoke(p,position.longValue());
            mapper.updateByPrimaryKeySelective(p);
        }catch (Exception e){
            ErrorUtil.error(e.getMessage());
        }
    }

    @Override
    public int delete(Object id) {
        return delPersistFile(o->mapper.deleteByPrimaryKey(o),id);
    }

    @Override
    public int delete(T t) {
        return delPersistFile(o->mapper.delete(o), t);
    }

    @Override
    public int delete(Example example) {
        return delPersistFile(o->mapper.deleteByExample(o), example);
    }

    @Override
    public int deleteByIds(String ids) {
        return delPersistFile(o->mapper.deleteByIds(o), ids);
    }

    @Override
    public void deleteForTree(Object id, String filedName){
        try{
            T t = get(id);
            if(t == null) return;
            delete(id);

            T tParam = tClass.newInstance();
            PropertyDescriptor pd = new PropertyDescriptor(filedName, tClass);
            Method setMethod = pd.getWriteMethod();
            setMethod.invoke(tParam,id);
            List<T> subList = select(tParam);
            for(T sub : subList){
                deleteForTree(sub.getPk(), filedName);
            }
        } catch (Exception e){
            ErrorUtil.error(e.getMessage());
        }
    }

    @Override
    public T get(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public T get(T t) {
        return mapper.selectOne(t);
    }

    @Override
    public T get(String field, Object value) {
        List<T> list = select(field, value);
        if(!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    @Override
    public <F> F getMax(T t, Class<F> clazz) {
        ParamUtil.notBlank(t,"columnName");
        Object o = mapper.selectMax(t);
        if(o==null){
            if(clazz == Integer.class || clazz == Long.class) {
                o = 0;
            }
        }
        F f = clazz.cast(o);
        return f;
    }

    @Override
    public <F> F getMin(T t, Class<F> clazz) {
        ParamUtil.notBlank(t,"columnName");
        Object o = mapper.selectMin(t);
        if(o==null){
            if(clazz == Integer.class || clazz == Long.class) {
                o = 0;
            }
        }
        F f = clazz.cast(o);
        return f;
    }

    @Override
    public List<T> select() {
        return mapper.selectAll();
    }

    @Override
    public List<T> select(T t) {
        return mapper.select(t);
    }

    @Override
    public List<T> select(Example example) {
        return mapper.selectByExample(example);
    }

    @Override
    public List<T> select(String field, Object value){
        Weekend wk = Weekend.of(tClass);
        WeekendCriteria<T,Object> criteria = wk.weekendCriteria();
        criteria.andEqualTo(field,value);

        List<T> list = select(wk);
        return list;
    }

    @Override
    public List<T> selectByIds(String ids) {
        return mapper.selectByIds(ids);
    }

    @Override
    public Pager<T> selectForPage(T t) {
        return selectForPage(o->selectForList(o),t);
    }

    @Override
    public <P, R> Pager<R> selectForPage(PageFunc<P, R> function, P p) {
        PageUtil.startPage(p,true);
        PageInfo<R> pageInfo = new PageInfo(function.excute(p));
        Pager pager = PageUtil.getPager(pageInfo);
        return pager;
    }

    @Override
    public List<Map<String,Object>> selectForTree(T t, boolean lazy){
        List<Map<String,Object>> treeList = new ArrayList<>();
        try{
            ParamUtil.notBlank(t,"fieldName");
            PropertyDescriptor pd = new PropertyDescriptor(t.getFieldName(), tClass);
            Method setMethod = pd.getWriteMethod();

            List<T> tList = selectForList(t);
            for(T po : tList){
                Map<String,Object> map = new HashMap<>();
                BeanUtil.beanToMap(po, map, false, o->{
                    Field[] fields = ReflectUtil.getFields(BasePo.class);
                    for(Field field : fields){
                        if(o.equals(field.getName())){
                            return null;
                        }
                    }
                    return o;
                });
                map.put("pk", po.getPk());
                treeList.add(map);
                T tParam = tClass.newInstance();
                setMethod.invoke(tParam, po.getPk());
                tParam.setFieldName(t.getFieldName());
                tParam.setOrderBy(t.getOrderBy());
                if(lazy){
                    if(count(tParam) > 0) map.put("hasChildren",true);
                }else{
                    List<Map<String,Object>> subList = selectForTree(tParam, lazy);
                    if(!subList.isEmpty())map.put("children", subList);
                }
            }
        }catch (Exception e){
            ErrorUtil.error(e.getMessage());
        }
        return treeList;
    }

    @Override
    public int count(T t) {
        return mapper.selectCount(t);
    }

    @Override
    public int count(Example example) {
        return mapper.selectCountByExample(example);
    }

    @Override
    public List<Map> countGroup(T t) {
        return mapper.selectCountGroupBy(t);
    }

    @Override
    public boolean exist(String field, String value, Object id)  {
        try{
            T t = tClass.newInstance();
            PropertyDescriptor pd = new PropertyDescriptor(field, tClass);
            Method setMethod = pd.getWriteMethod();
            setMethod.invoke(t,value);
            t.setPk(id);

            return exist(t);
        }catch (Exception e){
            ErrorUtil.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean exist(String field, String value){
        return exist(field, value, null);
    }

    @Override
    public boolean exist(T t) {
        Weekend wk = Weekend.of(t.getClass());
        WeekendCriteria<T,Object> criteria = wk.weekendCriteria();

        Class tClass = t.getClass();
        if(t.getPk()!=null){
            Field[] fields = tClass.getDeclaredFields();
            String idName = "";
            Field f = null;
            for(int i = 0; i<fields.length; i++){
                f = fields[i];
                if(f.getAnnotation(Id.class)!=null){
                    idName = f.getName();
                    break;
                }
            }
            if(idName == null) ErrorUtil.error(tClass.getName()+" has not id!");
            criteria.andNotEqualTo(idName, t.getPk());
        }
        MapperUtil.setAndEqual(t,criteria,true);
        List<T> list = select(wk);
        if(!list.isEmpty()) return true;
        return false;
    }

    /**
     * 持久化文件，支持BaseService的增加、修改方法
     * @param func
     * @param t
     * @param u
     * @param <U>
     * @return
     */
    private <U> int persistFile(PersistFileFunc<T,U> func, T t, U u){
        if(ossTemplate==null){
            if(u==null) return func.applyAsInt(t);
            else return func.applyAsInt(t, u);
        }

        Field[] fields = ReflectUtil.getFieldsDirectly(tClass,false);
        List<Field> list = Arrays.stream(fields).filter(o->{
            if( o.isAnnotationPresent(Path.class)){
                String value = (String) ReflectUtil.getFieldValue(t,o);
                return StringUtils.isNotBlank(value) && value.startsWith("/" + PathEnum.TEMP.getPath() +"/");
            }
            return false;
        }).collect(Collectors.toList());
        if(list.isEmpty()){
            if(u==null) return func.applyAsInt(t);
            else return func.applyAsInt(t, u);
        }
        //编辑
        boolean editFlag = (t.getPk()!=null || u!=null) ? true : false;

        //替换
        List<String> pathList = new ArrayList<>();

        for(Field field : list){
            String path = ReflectUtil.getFieldValue(t,field).toString();
            Path pathAnnotation = field.getAnnotation(Path.class);
            String separator = pathAnnotation.separator();
            if(StringUtils.isBlank(separator)){
                if(!ossTemplate.exist(path)) continue;
                pathList.add(path);
                ReflectUtil.setFieldValue(t,field,path.replace("/" + PathEnum.TEMP.getPath(), "/" + PathEnum.PERM.getPath()));
            }else{
                for(String p : path.split(separator)){
                    if(!ossTemplate.exist(p)) continue;
                    pathList.add(p);
                }
                ReflectUtil.setFieldValue(t,field,path.replaceAll("/" + PathEnum.TEMP.getPath(), "/" + PathEnum.PERM.getPath()));
            }

        }

        List<T> poList = new ArrayList<>();
        if(editFlag){
            if(t.getPk()!=null){
                poList.add(mapper.selectByPrimaryKey(t.getPk()));
            } else{
                poList = mapper.selectByExample(u);
            }
        }

        //删除持久化文件
        if(editFlag){
            delPersistFile(poList,list);
        }

        int i = 0;
        if(u==null) i = func.applyAsInt(t);
        else i = func.applyAsInt(t, u);
        //持久并删除
        pathList.stream().forEach(o->{
            String persPath = o.replace("/" + PathEnum.TEMP.getPath(),"/" + PathEnum.PERM.getPath());
            String tempPath = o;
            ossTemplate.move(tempPath, persPath);
        });


        return i;
    }

    /**
     * 删除持久化文件，支持BaseService的删除方法
     * @param func
     * @param u
     * @param <U>
     * @return
     */
    private <U> int delPersistFile(ToIntFunction<U> func, U u){
        if(ossTemplate==null) return func.applyAsInt(u);

        Field[] fields = ReflectUtil.getFieldsDirectly(tClass,false);
        List<Field> list = Arrays.stream(fields).filter(o->o.isAnnotationPresent(Path.class)).collect(Collectors.toList());
        if(list.isEmpty()) return func.applyAsInt(u);

        List<T> dbList = new ArrayList<>();
        if(u.getClass().equals(tClass)){
            dbList = this.select((T) u);
        }else if(u.getClass().equals(Example.class)){
            dbList = this.select((Example) u);
        }else if(u.getClass().equals(String.class) ){
            dbList = this.selectByIds((String) u);
        }else{
            dbList.add(this.get(u));
        }
        int i = func.applyAsInt(u);
        //删除
        delPersistFile(dbList,list);
        return i;
    }

    /**
     * 删除持久化文件
     * @param poList
     * @param fieldsList
     */
    private void delPersistFile(List<T> poList, List<Field> fieldsList){
        if(ossTemplate==null || poList.isEmpty() || fieldsList.isEmpty()) return;
        poList.parallelStream().forEach(po->{
            fieldsList.stream().forEach(f->{
                Object obj=ReflectUtil.getFieldValue(po,f);
                if(obj==null) return;
                String path = obj.toString();
                Path pathAnnotation = f.getAnnotation(Path.class);
                String separator = pathAnnotation.separator();
                if(StringUtils.isBlank(separator)){
                    if(path.startsWith("/" + PathEnum.PERM.getPath() +"/")) ossTemplate.delete(path);
                }else{
                    for(String p : path.split(separator)){
                        if(p.startsWith("/" + PathEnum.PERM.getPath() +"/")) ossTemplate.delete(p);
                    }
                }

            });
        });
    }


}
