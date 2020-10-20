package com.goumang.sys;

import com.goumang.core.web.EnableControllerAdvice;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * By huang.rb on 2019/3/18
 */
@Configuration
@EnableControllerAdvice
@MapperScan(basePackages = {"com.goumang.sys.mapper"})
public class SysConfiguration  {

}
