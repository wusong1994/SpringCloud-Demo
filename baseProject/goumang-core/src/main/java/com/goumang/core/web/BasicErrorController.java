package com.goumang.core.web;

import com.goumang.core.base.BaseExceptionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**

 * 自定义的ErrorController,用于使用WebResponse对象封装Spring boot返回的错误信息。

 * 本类只负责处理CustomControllerAdvice无法处理的错误，一般本类返回的都是内部系统错误

 * @author Gene

 *

 */
@Controller
public class BasicErrorController extends AbstractErrorController {

	@Autowired
	public BasicErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
	}

	private static final String PATH = "/error";

	@RequestMapping(value=PATH)
	@ResponseBody
	public ResponseEntity<WebResponse> error(HttpServletRequest request) {
		//具体请参看ErrorAttributes的源码
		//https://github.com/spring-projects/spring-boot/blob/master/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/web/DefaultErrorAttributes.java
		Map<String, Object> body = getErrorAttributes(request,true);
		HttpStatus status = this.getStatus(request);
		WebResponse webresponse;

		if(status == HttpStatus.INTERNAL_SERVER_ERROR){
			webresponse = new WebResponse(String.valueOf(status.value()), BaseExceptionEnum.ERROR.getMessage());
			return new ResponseEntity<WebResponse>(webresponse,status);
		}else {
			Object errorMessage = body.get("error");	
			webresponse = new WebResponse(String.valueOf(status.value()),(String)errorMessage);
			return new ResponseEntity<WebResponse>(webresponse,status);
		}

	}

	public String getErrorPath() {
		return PATH;
	}
}
