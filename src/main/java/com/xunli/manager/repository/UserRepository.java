package com.xunli.manager.repository;

import com.xunli.manager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

  Optional<User> findOneByUsername(String username);

  Optional<User> findOneByNickname(String nickname);

  Optional<User> findOneByEmail(String email);

  Optional<User> findOneByPhone(String phone);

  Optional<User> findOneByActivationKey(String activationKey);

  Optional<User> findOneByResetKey(String resetKey);

  List<User> findAllByActivatedFalseAndCreatedDateBefore(LocalDateTime dateTime);
  
  List<User> findAllByActivatedTrueAndEnabledTrue();
  
  User findOne2ByUsername(String username);

}
