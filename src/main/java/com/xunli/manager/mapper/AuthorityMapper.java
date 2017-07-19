package com.xunli.manager.mapper;

import com.xunli.manager.domain.Authority;
import com.xunli.manager.model.AuthorityModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {})
public interface AuthorityMapper {

  AuthorityModel entityToModel(Authority authority);

  Authority modelToEntity(AuthorityModel model);
  
  void updateEntityFromModel(AuthorityModel model, @MappingTarget Authority entity);

}
