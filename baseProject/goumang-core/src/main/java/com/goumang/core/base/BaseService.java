package com.goumang.core.base;

import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

public interface BaseService<T extends BasePo> {
    /**
     * 插入，支持持久文件
     * @param t 实体
     * @return
     */
    int insert(T t);

    /**
     * 选择性插入，支持持久文件
     * @param t 实体
     * @return
     */
    int insertSelective(T t);

    /**
     * 通过主键更新，支持持久文件
     * @param t 实体
     * @return
     */
    int update(T t);

    /**
     * 通过主键选择性更新，支持持久文件
     * @param t 实体
     * @return
     */
    int updateSelective(T t);

    /**
     * 通过Example更新，支持持久文件
     * @param t 实体
     * @param example 条件
     * @return
     */
    int updateByExample(T t, Example example);

    /**
     * 通过Example选择性更新，支持持久文件
     * @param t 实体
     * @param example 条件
     * @return
     */
    int updateByExampleSelective(T t, Example example);

    /**
     * 更新排序
     * @param t 实体
     * @param position 想要移动位置上
     * @throws Exception
     */
    void updateSort(T t, Integer position);

    /**
     * 通过主键删除，支持删除持久文件
     * @param id 主键
     * @return
     */
    int delete(Object id);

    /**
     * 通过参数删除，支持删除持久文件
     * @param t 实体
     * @return
     */
    int delete(T t);

    /**
     * 通过example删除，支持删除持久文件
     * @param example 条件
     * @return
     */
    int delete(Example example);

    /**
     * 通过多个主键删除，支持删除持久文件
     * @param ids 主键，多个逗号分隔
     * @return
     */
    int deleteByIds(String ids);

    /**
     * 树形删除，通过主键删除，并删除所有子节点
     * @param id 主键
     * @param filedName 父ID的属性名称
     */
    void deleteForTree(Object id, String filedName);

    /**
     * 通过主键获取实体
     * @param id 主键
     * @return
     */
    T get(Object id);

    /**
     * 通过参数获取实体
     * @param t 参数
     * @return
     */
    T get(T t);

    /**
     * 通过某个字段的值获取
     * @param field 字段名称
     * @param value 字段值
     * @return
     */
    T get(String field, Object value);

    /**
     * 通过参数获取最大值
     * @param t 实体
     * @param clazz 返回的结果类型
     * @param <F> 结果泛型
     * @return
     */
    <F> F getMax(T t, Class<F> clazz);

    /**
     * 通过参数获取最小值
     * @param t 实体
     * @param clazz 返回的结果类型
     * @param <F> 结果泛型
     * @return
     */
    <F> F getMin(T t, Class<F> clazz);

    /**
     * 查询
     * @return
     */
    List<T> select();

    /**
     * 通过参数查询
     * @param t 参数
     * @return
     */
    List<T> select(T t);

    /**
     * 通过example查询
     * @param example 条件
     * @return
     */
    List<T> select(Example example);

    /**
     * 通过某个字段的值查询
     * @param field 字段名称
     * @param value 字段值
     * @return
     */
    List<T> select(String field, Object value);

    /**
     * 通过ids查询
     * @param ids 主键，多个逗号隔开
     * @return
     */
    List<T> selectByIds(String ids);

    /**
     * 列表查询，基于{@link BaseService#select(Example)}查询
     * @param t
     * @return
     */
    List<T> selectForList(T t);

    /**
     * 分页查询
     * @param t
     * @return
     */
    Pager<T> selectForPage(T t);

    /**
     * 分页查询
     * @param function 列表查询方法
     * @param p  查询参数
     * @param <P> 列表查询方法的参数
     * @param <R> 列表查询方法返回的实体
     * @return
     */
    <P,R> Pager<R> selectForPage(PageFunc<P, R> function, P p);

    /**
     * 树形查询
     *
     * <pre>
     *   其中Map的key<code>children</code>为子列表，<code>hasChildren</code>为是否含有子列表
     * </pre>
     * @param t 参数
     * @param lazy 懒加载
     * @return
     */
    List<Map<String,Object>> selectForTree(T t, boolean lazy);

    /**
     * 通过参数统计
     * @param t 实体
     * @return
     */
    int count(T t);

    /**
     * 通过example统计
     * @param example 条件
     * @return
     */
    int count(Example example);

    /**
     * 通过参数分组统计
     * @param t 实体
     * @return
     */
    List<Map> countGroup(T t);

    /**
     * 判断值是否存在，不包括当前记录
     * @param field 字段名称
     * @param value 值
     * @param id 主键
     * @return true-存在，false-不存在
     */
    boolean exist(String field, String value, Object id);

    /**
     * 断值是否存在
     * @param field 字段名称
     * @param value 值
     * @return true-存在，false-不存在
     */
    boolean exist(String field, String value);

    /**
     * 判断是否存在
     * @param t
     * @return
     */
    boolean exist(T t);
}
