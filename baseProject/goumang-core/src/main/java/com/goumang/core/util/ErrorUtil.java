package com.goumang.core.util;

import com.goumang.core.base.BaseExceptionEnum;
import com.goumang.core.base.BaseException;

/**
 * By huang.rb on 2018/12/26
 */
public class ErrorUtil {


    /**
     * 失败
     * @param message 失败信息
     */
    public static void error(String message){
        throw new BaseException(message);
    }

    /**
     * 未登录
     */
    public static void unauthorized(){
        throw new BaseException(BaseExceptionEnum.UNAUTHORIZED);
    }

    /**
     * 权限不足
     */
    public static void forbidden(){
        throw new BaseException(BaseExceptionEnum.FORBIDDEN);
    }

    /**
     * 权限不足
     * @param message 原因
     */
    public static void forbidden(String message){
        throw new BaseException(BaseExceptionEnum.FORBIDDEN.getCode(),message);
    }

}
