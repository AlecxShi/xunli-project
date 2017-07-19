package com.xunli.manager.security;

import com.xunli.manager.domain.User;
import com.xunli.manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService
{

	private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{

		log.debug("Authenticating {}", username);
		String lowerUsername = username.toLowerCase();

		Optional<User> userFromDatabase = userRepository.findOneByUsername(lowerUsername);
		log.debug("user: {}", userFromDatabase);

		return userFromDatabase.map(user ->
		{
			if (!user.isActivated())
			{
				throw new UserNotActivatedException("User " + lowerUsername + " was not activated");
			}
			return user;
		}).orElseThrow(() -> new UsernameNotFoundException("User " + lowerUsername + " was not found in the " + "database"));
	}
}
