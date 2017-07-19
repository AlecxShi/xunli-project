package com.xunli.manager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

/**
 * @PreAuthorize("hasRole('PERM_READ_FORUMS')")
 * @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_FACULTY_MEMBER',
 *                                           'ROLE_ADMIN')").
 */
@Component
public class CustomSecurityFilter extends AbstractSecurityInterceptor implements Filter
{

	@Autowired
	private CustomSecurityMetadataSource securityMetadataSource;

	@Autowired
	private CustomAccessDecisionManager accessDecisionManager;

	@PostConstruct
	public void init()
	{
		super.setAccessDecisionManager(accessDecisionManager);
	}

	@Override
	public Class<?> getSecureObjectClass()
	{
		//下面的CustomAccessDecisionManager的supports方面必须放回true,否则会提醒类型错误
		return FilterInvocation.class;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource()
	{
		return this.securityMetadataSource;
	}

	public void init(FilterConfig filterConfig) throws ServletException
	{

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
	}

	private void invoke(FilterInvocation fi) throws IOException, ServletException
	{
		//super.beforeInvocation(fi);
		//1.获取请求资源的权限
		//执行Collection<ConfigAttribute> attributes = SecurityMetadataSource.getAttributes(object);
		//2.是否拥有权限
		//this.accessDecisionManager.decide(authenticated, object, attributes);
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try
		{
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		}
		finally
		{
			super.afterInvocation(token, null);
		}
	}

	public void destroy()
	{

	}
}
