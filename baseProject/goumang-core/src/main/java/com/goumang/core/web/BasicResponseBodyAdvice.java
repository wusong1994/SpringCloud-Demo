package com.goumang.core.web;


import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 自定义的返回结果处理器
 * @author Gene
 *
 */
@ControllerAdvice
public class BasicResponseBodyAdvice implements ResponseBodyAdvice<Object>{

	/**
	 * 在返回结果前先处理，处理规则如下：
	 * 1）如果发现返回对象为WebResponse,byte,byte[]不作处理;
	 * 2）其他对象使用WebResponse封装后返回
	 */
	public  Object beforeBodyWrite(Object object, MethodParameter returnType, MediaType arg2,
                                  Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest arg4, ServerHttpResponse arg5) {
		if(object == null) return new WebResponse();
		Class<?> classType = object.getClass();
		if(classType.equals(WebResponse.class)
				|| classType.equals(Byte[].class)
				|| classType.equals(Byte.class)
		)
			return object;
		WebResponse webResponse = new WebResponse(object);
		return webResponse;
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		Class<?> classType = returnType.getParameterType();
		return !classType.equals(WebResponse.class) && !classType.equals(Byte[].class) && !classType.equals(Byte.class);
	}

}
