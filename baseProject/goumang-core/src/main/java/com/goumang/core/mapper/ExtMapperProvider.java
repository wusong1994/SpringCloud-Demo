package com.goumang.core.mapper;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * By huang.rb on 2019/4/12
 */
public class ExtMapperProvider extends MapperTemplate {

    public ExtMapperProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectMax(MappedStatement ms){
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT MAX(<if test=\"columnName != null\">${columnName}</if>)");
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.whereAllIfColumns(entityClass, this.isNotEmpty()));
        return sql.toString();
    }


    public String selectMin(MappedStatement ms){
        Class<?> entityClass = this.getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT MIN(<if test=\"columnName != null\">${columnName}</if>)");
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.whereAllIfColumns(entityClass, this.isNotEmpty()));
        return sql.toString();
    }


    public String selectCountGroupBy(MappedStatement ms){
        //修改返回值类型为实体类型
        Class<?> entityClass = this.getEntityClass(ms);

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(<if test=\"columnName != null\">${columnName}</if><if test=\"columnName == null\">*</if>) cnt");
        sql.append("<if test=\"groupBy!=null\">,${groupBy}</if>");
        sql.append(SqlHelper.fromTable(entityClass, this.tableName(entityClass)));
        sql.append(SqlHelper.whereAllIfColumns(entityClass, this.isNotEmpty()));
        sql.append("<if test=\"groupBy!=null\">GROUP BY ${groupBy}</if>");
        return sql.toString();
    }

}
