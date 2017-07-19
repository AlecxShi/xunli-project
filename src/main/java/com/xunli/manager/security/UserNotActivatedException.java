package com.xunli.manager.security;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException
{

	private static final long serialVersionUID = -1840931386692195261L;

	public UserNotActivatedException(String message)
	{
		super(message);
	}

	public UserNotActivatedException(String message, Throwable t)
	{
		super(message, t);
	}
}
