package com.xunli.manager.interceptor;


import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.model.CommonUserLogins;
import com.xunli.manager.repository.CommonUserLoginsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class ManagerCheckTokenInterceptor extends BaseInterceptor implements HandlerInterceptor
{

	@Autowired
	CommonUserLoginsRepository commonUserLoginsRepository;

	public ManagerCheckTokenInterceptor()
	{

	}

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception
	{
		Boolean result = true;
		String token = httpServletRequest.getParameter("token");
		if (token == null || token.isEmpty() || token.toLowerCase().equals("null"))
		{
			writeError(httpServletRequest, httpServletResponse, ReturnCode.PUBLIC_TOKEN_MISSING);
			return false;
		}

		CommonUserLogins login = commonUserLoginsRepository.getByToken(token);
		if (login == null)
		{
			writeError(httpServletRequest, httpServletResponse, ReturnCode.PUBLIC_TOKEN_IS_INVALID);
			return false;
		}

		if (login.getExpireTime().compareTo(new Date()) <= 0)
		{
			writeError(httpServletRequest, httpServletResponse, ReturnCode.PUBLIC_TOKEN_IS_INVALID);
			return false;
		}
		return result;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception
	{

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception
	{

	}
}
