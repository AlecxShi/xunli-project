package com.xunli.manager.security;

import com.xunli.manager.config.Constants;
import com.xunli.manager.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public final class SecurityUtils
{

	private SecurityUtils()
	{
	}

	public static String getCurrentUsername()
	{
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		String username = null;
		if (authentication != null)
		{
			if (authentication.getPrincipal() instanceof UserDetails)
			{
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				username = springSecurityUser.getUsername();
			}
			else if (authentication.getPrincipal() instanceof String)
			{
				username = (String) authentication.getPrincipal();
			}
		}
		return username;
	}

	public static boolean isAuthenticated()
	{
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
		if (authorities != null)
		{
			for (GrantedAuthority authority : authorities)
			{
				if (authority.getAuthority().equals(Constants.ROLE_ANONYMOUS))
				{
					return false;
				}
			}
		}
		return true;
	}

	public static Long getCurrentUserId()
	{
		return getCurrentUser().getId();
	}

	public static User getCurrentUser()
	{
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null)
		{
			if (authentication.getPrincipal() instanceof User)
			{
				return (User) authentication.getPrincipal();
			}
		}
		throw new IllegalStateException("User not found!");
	}

	public static boolean isUserInRole(String authority)
	{
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null)
		{
			if (authentication.getPrincipal() instanceof UserDetails)
			{
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority));
			}
		}
		return false;
	}
}
