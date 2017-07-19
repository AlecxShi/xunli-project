package com.xunli.manager.security;

import com.xunli.manager.domain.Authority;
import com.xunli.manager.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;


@Component
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource
{

	@Autowired
	private AuthorityRepository authorityRepository;

	private static Map<String, Collection<ConfigAttribute>> authorityMap = null;

	@PostConstruct
	private void loadAuthorityDefine()
	{
		if (authorityMap == null)
		{
			authorityMap = new HashMap<>();
			List<Authority> authorities = authorityRepository.findAll();
			for (Authority auth : authorities)
			{
				String path = auth.getAuthorityPath();
				if (path != null)
				{
					Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
					ConfigAttribute configAttribute = new SecurityConfig("ROLE_" + auth.getAuthorityCode());
					configAttributes.add(configAttribute);
					authorityMap.put(path, configAttributes);
				}
			}
		}
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException
	{
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		if (authorityMap == null)
		{
			loadAuthorityDefine();
		}
		if (requestUrl.indexOf("?") > -1)
		{
			requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
		}
		Collection<ConfigAttribute> configAttributes = authorityMap.get(requestUrl);
		//        System.out.println("--------->" + requestUrl + "=" + (configAttributes == null ? 0 : configAttributes.size()) );
		return configAttributes;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes()
	{
		if (authorityMap == null)
		{
			loadAuthorityDefine();
		}
		Set<ConfigAttribute> all = new HashSet<>();
		authorityMap.values().forEach(all::addAll);
		return all;
	}

	@Override
	public boolean supports(Class<?> clazz)
	{
		return true;
	}
}
