package com.xunli.manager.mapper;

import com.xunli.manager.domain.User;
import com.xunli.manager.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {

  UserModel entityToModel(User user);

  User modelToEntity(UserModel model);
  
  void updateEntityFromModel(UserModel model, @MappingTarget User entity);

}
