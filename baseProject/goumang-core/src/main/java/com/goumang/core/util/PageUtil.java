package com.goumang.core.util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goumang.core.base.Pager;
import com.goumang.core.base.BasePo;

import java.util.Map;

public class PageUtil {

    public static final int pageSize = 10;

    public static void startPage(Object obj,boolean count){
        if(obj instanceof Map){
            startPage((Map)obj,count);
        }else if(obj instanceof BasePo){
            startPage((BasePo)obj,count);
        }
    }

    public static void startPage(Map<String,Object> map, boolean count){
        int pageSize = map.containsKey("pageSize")?(int)map.get("pageSize"):PageUtil.pageSize;
        pageSize = pageSize<=0?PageUtil.pageSize:pageSize;

        int pageNum = map.containsKey("pageNum")?(int)map.get("pageNum"):1;
        pageNum = pageNum<=0?1:pageNum;
        PageHelper.startPage(pageNum, pageSize, count);
    }

    public static void startPage(BasePo po, boolean count){
        int pageSize = po.getPageSize()<=0?PageUtil.pageSize:po.getPageSize();
        int pageNum = po.getPageNum()<=0?1 : po.getPageNum();

        PageHelper.startPage(pageNum, pageSize, count);
    }


    public static Pager getPager(PageInfo pageInfo){
        Pager pager = new Pager();
        pager.setPageNumber(pageInfo.getPageNum());
        pager.setPageSize(pageInfo.getPageSize());
        pager.setTotalElements(pageInfo.getTotal());
        pager.setContent(pageInfo.getList());
        pager.setTotalPages((long) Math.ceil(pageInfo.getTotal()*1.0d/pageInfo.getPageSize()));
        return pager;
    }

}
