package com.xunli.manager.repository;

import com.xunli.manager.domain.PersistentAuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface PersistenceAuditEventRepository extends JpaRepository<PersistentAuditEvent, Long> {

  List<PersistentAuditEvent> findByPrincipal(String principal);

  List<PersistentAuditEvent> findByPrincipalAndEventDateAfter(String principal,
                                                              LocalDateTime after);

  List<PersistentAuditEvent> findAllByEventDateBetween(LocalDateTime fromDate,
                                                       LocalDateTime toDate);
}
