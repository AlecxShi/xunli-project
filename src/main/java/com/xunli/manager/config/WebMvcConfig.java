package com.xunli.manager.config;

import com.xunli.manager.interceptor.ManagerCheckTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter
{
	@Autowired
	ManagerCheckTokenInterceptor managerCheckTokenInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) 
	{
		registry.addInterceptor(managerCheckTokenInterceptor)
				.addPathPatterns("/api/recommend/show/login")
				.addPathPatterns("/api/children/save")
				.addPathPatterns("/api/collect/save")
				.addPathPatterns("/api/collect/getAll")
				.addPathPatterns("/api/feedback/save");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler("/image/**/*")
				.addResourceLocations("file:/alidata/image/photos/");
		registry.addResourceHandler("/icon/**")
				.addResourceLocations("file:/alidata/image/icons/private/")
				.addResourceLocations("file:/alidata/image/icons/public/")
				.addResourceLocations("file:/alidata/image/icons/discover/");
	}
}
