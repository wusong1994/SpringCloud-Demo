package com.goumang.core.base;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class BasePo implements Serializable {

    @Transient
    protected String ids;
    /** 每页大小 */
    @Transient
    protected int pageSize;
    /** 页码 */
    @Transient
    protected int pageNum;
    /** 实体的属性名称 */
    @Transient
    protected String fieldName;
    /** 排序 */
    @Transient
    protected String orderBy;
    /** 分组 */
    @Transient
    protected String groupBy;
    /** 表的字段名称 */
    @Transient
    protected String columnName;
    /** 扩展传参 */
    @Transient
    protected Map<String,Object> ext;
    /**
     * 选择性处理，作用于{@link BaseCtrl#insert(BasePo)}和{@link BaseCtrl#update(BasePo)}
     * true为选择性插入或更新，false为非选择性插入或更新，默认为true
     */
    @Transient
    protected boolean selective = true;

    public BasePo() {
        this.ext = new HashMap<>();
    }

    public abstract Object getPk();
    public abstract void setPk(Object pk);

    @JsonIgnore
    public String getIds() {
        return ids;
    }
    @JsonProperty
    public void setIds(String ids) {
        this.ids = ids; new Date();
    }
    @JsonIgnore
    public int getPageSize() {
        return pageSize;
    }
    @JsonProperty
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    @JsonIgnore
    public int getPageNum() {
        return pageNum;
    }
    @JsonProperty
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    @JsonIgnore
    public String getFieldName() {
        return fieldName;
    }
    @JsonProperty
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    @JsonIgnore
    public String getOrderBy() {
        return orderBy;
    }
    @JsonProperty
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    @JsonIgnore
    public String getGroupBy() {
        return groupBy;
    }
    @JsonProperty
    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }
    @JsonIgnore
    public Map<String, Object> getExt() {
        return ext;
    }
    @JsonProperty
    public void setExt(Map<String, Object> ext) {
        this.ext = ext;
    }
    @JsonIgnore
    public Object getExtValue(String key){return this.ext.get(key);}
    @JsonProperty
    public void setExtValue(String key,Object value){this.ext.put(key,value);}
    @JsonIgnore
    public String getColumnName() {
        return columnName;
    }
    @JsonProperty
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    @JsonIgnore
    public boolean isSelective() {
        return selective;
    }
    @JsonProperty
    public void setSelective(boolean selective) {
        this.selective = selective;
    }
}
