package com.xunli.manager.repository;

import com.xunli.manager.domain.PersistentLogin;
import com.xunli.manager.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, String> {

  List<PersistentLogin> findByUser(User user);

  List<PersistentLogin> findByLastUsedBefore(LocalDateTime dateTime);

}
