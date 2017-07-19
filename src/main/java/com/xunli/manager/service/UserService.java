package com.xunli.manager.service;

import com.xunli.manager.domain.User;
import com.xunli.manager.repository.PersistentLoginRepository;
import com.xunli.manager.repository.UserRepository;
import com.xunli.manager.security.SecurityUtils;
import com.xunli.manager.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PersistentLoginRepository persistentLoginRepository;

  @Transactional(readOnly = true)
  public User getUserWithAuthorities() {
    User user = userRepository.findOneByUsername(SecurityUtils.getCurrentUsername()).get();
    user.getAuthorities().size();
    return user;
  }

  @Transactional(readOnly = true)
  public User getUserWithAuthorities(Long id) {
    User user = userRepository.findOne(id);
    user.getAuthorities().size();
    return user;
  }

  @Transactional(readOnly = true)
  public Optional<User> getUserWithAuthoritiesByUsername(String username) {
    return userRepository.findOneByUsername(username).map(u -> {
      u.getAuthorities().size();
      return u;
    });
  }

  public void changePassword(String password) {
    userRepository.findOneByUsername(SecurityUtils.getCurrentUsername()).ifPresent(u -> {
      String encryptedPassword = passwordEncoder.encode(password);
      u.setPassword(encryptedPassword);
      userRepository.save(u);
      log.debug("Changed password for User: {}", u);
    });
  }
  
  public Optional<Boolean> checkPassword(String password){
	  return userRepository.findOneByUsername(SecurityUtils.getCurrentUsername()).map(user -> {
		  return passwordEncoder.matches(password, user.getPassword());
	  });
  }

  public User createUserInformation(String username, String password, String email) {
    User newUser = new User();
    newUser.setUsername(username);
    String encryptedPassword = passwordEncoder.encode(password);
    newUser.setPassword(encryptedPassword);
    newUser.setEmail(email);
//TODO    newUser.getRoles().add(ROLE_USER);
    // new user is not active
    newUser.setActivated(false);
    // new user gets registration key
    newUser.setActivationKey(RandomUtil.generateActivationKey());
    userRepository.save(newUser);
    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  public Optional<User> updateUserInformation(String nickname, String name, String email,
      String phone) {
    return userRepository.findOneByUsername(SecurityUtils.getCurrentUsername()).map(u -> {
      u.setNickname(nickname);
      u.setName(name);
      u.setEmail(email);
      u.setPhone(phone);
      userRepository.save(u);
      log.debug("Changed Information for User: {}", u);
      return u;
    });
  }

  public Optional<User> requestActivation(String email) {
    return userRepository.findOneByEmail(email).filter(user -> !user.isActivated()).map(user -> {
      return user;
    });
  }

  public Optional<User> activateRegistration(String key) {
    log.debug("Activating user for activation key {}", key);
    return userRepository.findOneByActivationKey(key).map(user -> {
      // activate given user for the registration key.
      user.setActivated(true);
      user.setActivationKey(null);
      userRepository.save(user);
      log.debug("Activated user: {}", user);
      return user;
    });
  }

  public Optional<User> requestPasswordReset(String email) {
    log.info("{}", userRepository.findOneByEmail(email));
    return userRepository.findOneByEmail(email).filter(user -> user.isActivated()).map(user -> {
      log.info("{}", user);

      user.setResetKey(RandomUtil.generateResetKey());
      user.setResetDate(LocalDateTime.now());
      userRepository.save(user);
      return user;
    });
  }

  public Optional<User> completePasswordReset(String newPassword, String key) {
    log.debug("Reset user password for reset key {}", key);

    return userRepository.findOneByResetKey(key).filter(user -> {
      LocalDateTime oneDayAgo = LocalDateTime.now().minusHours(24);
      return user.getResetDate().isAfter(oneDayAgo);
    }).map(user -> {
      user.setPassword(passwordEncoder.encode(newPassword));
      user.setResetKey(null);
      user.setResetDate(null);
      userRepository.save(user);
      return user;
    });
  }

  @Scheduled(cron = "0 0 0 * * ?")
  public void removeOldPersistentLogins() {
    LocalDateTime now = LocalDateTime.now();
    persistentLoginRepository.findByLastUsedBefore(now.minusMonths(1)).stream().forEach(login -> {
      log.debug("Deleting persistent logins {}", login.getSeries());
      User user = login.getUser();
      user.getPersistentLogins().remove(login);
      persistentLoginRepository.delete(login);
    });
  }

  @Scheduled(cron = "0 0 1 * * ?")
  public void removeNotActivatedUsers() {
    LocalDateTime now = LocalDateTime.now();
    userRepository.findAllByActivatedFalseAndCreatedDateBefore(now.minusDays(3)).stream()
        .forEach(user -> {
          log.debug("Deleting not activated user {}", user.getUsername());
          userRepository.delete(user);
        });
  }

}
