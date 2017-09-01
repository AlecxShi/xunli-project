package com.xunli.manager.interceptor;


import com.xunli.manager.domain.UserLog;
import com.xunli.manager.enumeration.ReturnCode;
import com.xunli.manager.util.JSONUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by wanglei on 2015/9/17.
 */
public abstract class BaseInterceptor
{

	protected void writeError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, ReturnCode errorCode)
	{
		Map<String, Object> map = new LinkedHashMap();
		map.put("timestamp", new Date());
		map.put("code", errorCode.getCode());
		map.put("msg", errorCode.getMsg());
		map.put("detail", errorCode.getDetail());
		map.put("path", httpServletRequest.getServletPath());
		try
		{
			httpServletResponse.setContentType("application/json;charset=UTF-8");
			httpServletResponse.getWriter().print(JSONUtils.obj2json(map));
			
			UserLog userLog = (UserLog) httpServletRequest.getAttribute("userLog");
			if (userLog != null)
			{
				userLog.setReturnResult(false);
				userLog.setReturnTime(System.currentTimeMillis() / 1000);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 取得IP地址
	 * 
	 * @param request
	 * @return
	 */
	protected String getIP(HttpServletRequest request)
	{
		String ip = request.getHeader("X-REAL-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
