package com.xunli.manager.web;

import com.xunli.manager.domain.Role;
import com.xunli.manager.domain.criteria.RoleCriteria;
import com.xunli.manager.domain.specification.RoleSpecification;
import com.xunli.manager.mapper.RoleMapper;
import com.xunli.manager.model.RoleModel;
import com.xunli.manager.repository.RoleRepository;
import com.xunli.manager.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

@RestController
@RequestMapping("/api")
public class RoleController
{

	private final Logger log = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RoleMapper roleMapper;

	@RequestMapping(value = "/roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = true)
	public List<Role> list()
	{
		return roleRepository.findAll();
	}

	@RequestMapping(value = "/role", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = true)
	public Page<Role> query(@ModelAttribute RoleCriteria criteria, @PageableDefault Pageable pageable)
	{
		return roleRepository.findAll(new RoleSpecification(criteria), pageable);
	}

	@RequestMapping(value = "/role", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured(ROLE_ADMIN)
	public ResponseEntity<RoleModel> createRole(@Valid @RequestBody RoleModel model) throws URISyntaxException
	{
		log.debug("REST request to save role : {}", model);
		if (model.getId() != null)
		{
			return updateRole(model);
		}
		Role role = roleMapper.modelToEntity(model);
		RoleModel result = roleMapper.entityToModel(roleRepository.save(role));
		return ResponseEntity.created(new URI("/api/role/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert("role", result.getId().toString())).body(result);
	}

	@RequestMapping(value = "/role", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@Secured(ROLE_ADMIN)
	public ResponseEntity<RoleModel> updateRole(@RequestBody RoleModel model) throws URISyntaxException
	{
		log.debug("REST request to update Role : {}", model);
		return Optional.ofNullable(roleRepository.findOne(model.getId())).map(t ->
		{
			roleMapper.updateEntityFromModel(model, t);
			log.debug("{}", t);
			Role result = roleRepository.save(t);
			return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("role", result.getId().toString())).body(roleMapper.entityToModel(result));
		}).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping(value = "/role/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured(ROLE_ADMIN)
	public ResponseEntity<RoleModel> getRole(@PathVariable Long id)
	{
		log.debug("REST request to get Role : {}", id);
		return Optional.ofNullable(roleRepository.findOne(id)).map(roleMapper::entityToModel).map(model -> new ResponseEntity<>(model, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping(value = "/role/{ids}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured(ROLE_ADMIN)
	public ResponseEntity<Void> deleteRole(@PathVariable List<Long> ids)
	{
		log.debug("REST request to delete Role : {}", ids);
		List<Role> list = roleRepository.findAll(ids);
		List<Role> deleteList = new ArrayList<>();
		list.forEach(role ->
		{
			if (!role.isSys())
				deleteList.add(role);
		});
		roleRepository.delete(deleteList);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("role", ids.toString())).build();
	}
}
