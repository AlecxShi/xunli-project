package com.xunli.manager.repository;

import com.xunli.manager.model.CommonUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by Betty on 2017/7/16.
 */
public interface CommonUserRepository extends JpaRepository<CommonUser, Long>, JpaSpecificationExecutor<CommonUser> {

    List<CommonUser> findAllByIdIn(List<Long> ids);

    Optional<CommonUser> findOneByPhone(String value);

    List<CommonUser> findAllByPasswordIsNull();

    @Query("UPDATE CommonUser SET password = ?1 WHERE password IS NULL")
    @Modifying
    @Transactional
    void updatePassword(String password);

    List<CommonUser> findAllByUsertype(Long i);
}
