package com.goumang.core.base;

/**
 * By huang.rb on 2019/7/23
 */
public enum BaseExceptionEnum {

    UNAUTHORIZED("unauthorized","用户未登录"),
    FORBIDDEN("forbidden","权限不足"),
    ERROR("error","错误");

    String code;
    String message;

    BaseExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
