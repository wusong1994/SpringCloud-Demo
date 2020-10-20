package com.goumang.core.base;

/**
 * By huang.rb on 2019/7/23
 */
public class BaseException extends RuntimeException{

    private String code;

    private String message;

    public BaseException(){};

    public BaseException(BaseExceptionEnum baseExceptionEnum){
        this.code = baseExceptionEnum.getCode();
        this.message = baseExceptionEnum.getMessage();
    }

    public BaseException(String message) {
        this.code = "error";
        this.message = message;
    }

    public BaseException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
