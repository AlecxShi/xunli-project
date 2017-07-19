package com.xunli.manager.mapper;

import com.xunli.manager.domain.Role;
import com.xunli.manager.model.RoleModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper {

  RoleModel entityToModel(Role role);

  Role modelToEntity(RoleModel model);
  
  void updateEntityFromModel(RoleModel model, @MappingTarget Role entity);

}
