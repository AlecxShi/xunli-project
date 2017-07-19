package com.xunli.manager.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;


@Component
public class CustomAccessDecisionManager implements AccessDecisionManager
{

	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException
	{
		if (configAttributes == null)
		{
			return;
		}
		//所请求的资源拥有的权限(一个资源对多个权限)
		Iterator<ConfigAttribute> iterator = configAttributes.iterator();
		while (iterator.hasNext())
		{
			ConfigAttribute configAttribute = iterator.next();
			for (GrantedAuthority ca : authentication.getAuthorities())
			{
				if (ca.getAuthority().equals(configAttribute.getAttribute()))
				{
					return;
				}
			}
			//            if(authentication.getAuthorities().contains(configAttribute)){
			//                return;
			//            }
			//访问所请求资源所需要的权限
			//String needPermission = configAttribute.getAttribute();
			//System.out.println("needPermission is " + needPermission);
			//用户所拥有的权限authentication
			//for(GrantedAuthority ga : authentication.getAuthorities()) {
			//if(needPermission.equals(ga.getAuthority())) {
			//return;
			//}
			//}
		}
		//没有权限
		throw new AccessDeniedException(" 没有权限访问！ ");
	}

	public boolean supports(ConfigAttribute configAttribute)
	{
		return true;
	}

	public boolean supports(Class<?> aClass)
	{
		return true;
	}
}
