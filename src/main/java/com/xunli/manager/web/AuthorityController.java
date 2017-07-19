package com.xunli.manager.web;

import com.xunli.manager.domain.Authority;
import com.xunli.manager.domain.criteria.AuthorityCriteria;
import com.xunli.manager.domain.specification.AuthoritySpecification;
import com.xunli.manager.mapper.AuthorityMapper;
import com.xunli.manager.model.AuthorityModel;
import com.xunli.manager.repository.AuthorityRepository;
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
public class AuthorityController
{

	private final Logger log = LoggerFactory.getLogger(AuthorityController.class);

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private AuthorityMapper authorityMapper;

	@RequestMapping(value = "/authorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = true)
	public List<Authority> list()
	{
		return authorityRepository.findAll();
	}

	@RequestMapping(value = "/authority", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = true)
	public Page<Authority> query(@ModelAttribute AuthorityCriteria criteria, @PageableDefault Pageable pageable)
	{
		return authorityRepository.findAll(new AuthoritySpecification(criteria), pageable);
	}

	@RequestMapping(value = "/authority", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured(ROLE_ADMIN)
	public ResponseEntity<AuthorityModel> createAuthority(@Valid @RequestBody AuthorityModel model) throws URISyntaxException
	{
		log.debug("REST request to save authority : {}", model);
		if (model.getId() != null)
		{
			return updateAuthority(model);
		}
		Authority authority = authorityMapper.modelToEntity(model);
		AuthorityModel result = authorityMapper.entityToModel(authorityRepository.save(authority));
		return ResponseEntity.created(new URI("/api/authority/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert("authority", result.getId().toString())).body(result);
	}

	@RequestMapping(value = "/authority", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@Secured(ROLE_ADMIN)
	public ResponseEntity<AuthorityModel> updateAuthority(@RequestBody AuthorityModel model)
	{
		log.debug("REST request to update Authority : {}", model);
		return Optional.ofNullable(authorityRepository.findOne(model.getId())).map(t ->
		{
			authorityMapper.updateEntityFromModel(model, t);
			log.debug("{}", t);
			Authority result = authorityRepository.save(t);
			return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("authority", result.getId().toString())).body(authorityMapper.entityToModel(result));
		}).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping(value = "/authority/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured(ROLE_ADMIN)
	public ResponseEntity<AuthorityModel> getAuthority(@PathVariable Long id)
	{
		log.debug("REST request to get Authority : {}", id);
		return Optional.ofNullable(authorityRepository.findOne(id)).map(authorityMapper::entityToModel).map(model -> new ResponseEntity<>(model, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping(value = "/authority/{ids}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured(ROLE_ADMIN)
	public ResponseEntity<Void> deleteAuthority(@PathVariable List<Long> ids)
	{
		log.debug("REST request to delete Authority : {}", ids);
		List<Authority> list = authorityRepository.findAll(ids);
		List<Authority> deleteList = new ArrayList<>();
		list.forEach(authority ->
		{
			if (!authority.isSys())
				deleteList.add(authority);
		});
		authorityRepository.delete(deleteList);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("authority", ids.toString())).build();
	}
}
