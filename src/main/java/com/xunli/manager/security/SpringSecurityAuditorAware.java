package com.xunli.manager.security;

import com.xunli.manager.config.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String>
{

	@Override
	public String getCurrentAuditor()
	{
		String username = SecurityUtils.getCurrentUsername();
		return (username != null ? username : Constants.SYSTEM_ACCOUNT);
	}
}
