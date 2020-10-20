package com.goumang.core.web;


import com.goumang.core.base.BaseException;
import com.goumang.core.base.BaseExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;


/*
 * 通用异常处理类
 */
@ControllerAdvice
public class BasicControllerAdvice extends ResponseEntityExceptionHandler {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex) {
        BaseException be = (BaseException) ex;
        WebResponse webResponse = new WebResponse(be.getCode(), be.getMessage());
        return new ResponseEntity<>(webResponse, HttpStatus.OK);
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public ResponseEntity<?> handleRuntimeException(HttpServletRequest request, Throwable ex) {
        logger.error("runtime error", ex);
        WebResponse webResponse = new WebResponse(BaseExceptionEnum.ERROR.getCode(), ex.getMessage());
        return new ResponseEntity<>(webResponse, getStatus(request));
    }

    /**
     * 异常处理类
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler({Throwable.class})
    @ResponseBody
    public ResponseEntity<?> handleException(HttpServletRequest request, Throwable ex) {
        logger.error(ex.getMessage(), ex);
        WebResponse webResponse = new WebResponse(BaseExceptionEnum.ERROR.getCode(), ex.getMessage());
        return new ResponseEntity<>(webResponse, getStatus(request));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)  {
        logger.error("argument not valid error", ex);
        Collection<FieldError> errors = ex.getBindingResult().getFieldErrors();
        //显示第一个返回对象字段的错误
        WebResponse webresponse = new WebResponse(BaseExceptionEnum.ERROR.getCode(),errors.iterator().next().getDefaultMessage());
        return  new ResponseEntity<Object>(webresponse,headers,status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)  {
        logger.error("missing path error", ex);
        WebResponse webresponse = new WebResponse(BaseExceptionEnum.ERROR.getCode(),"missing path variable " + ex.getVariableName());
        return  new ResponseEntity<Object>(webresponse,headers,status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request){
        logger.error("", ex);
        //显示第一个返回对象字段的错误
        WebResponse webresponse = new WebResponse(String.valueOf(status.value()),ex.getMessage());
        return  new ResponseEntity<Object>(webresponse,headers,status);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}