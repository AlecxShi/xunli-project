package com.xunli.manager.web;

import com.xunli.manager.domain.User;
import com.xunli.manager.domain.criteria.UserCriteria;
import com.xunli.manager.domain.specification.UserSpecification;
import com.xunli.manager.mapper.UserMapper;
import com.xunli.manager.model.UserModel;
import com.xunli.manager.model.Validation;
import com.xunli.manager.model.ValidationResult;
import com.xunli.manager.repository.UserRepository;
import com.xunli.manager.util.HeaderUtil;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;

@RestController
@RequestMapping("/api")
public class UserController
{

	private final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	

	@Autowired
	private UserMapper userMapper;

	// @Autowired
	// private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;


	

	@RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(readOnly = true)
	public Page<User> query(@ModelAttribute UserCriteria criteria, @PageableDefault Pageable pageable)
	{
		return userRepository.findAll(new UserSpecification(criteria), pageable);
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured(ROLE_ADMIN)
	public ResponseEntity<User> createUser(@Valid @RequestBody UserModel userModel) throws URISyntaxException
	{
		log.debug("REST request to save User : {}", userModel);
		if (userModel.getId() != null)
		{
			return updateUser(userModel);
		}
		User u = userMapper.modelToEntity(userModel);
		u.setPassword(passwordEncoder.encode(userModel.getPassword()));
		User result = userRepository.save(u);
		return ResponseEntity.created(new URI("/api/users/" + result.getUsername())).headers(HeaderUtil.createEntityCreationAlert("user", result.getId().toString())).body(result);
	}

	@RequestMapping(value = "/user", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@Secured(ROLE_ADMIN)
	public ResponseEntity<User> updateUser(@RequestBody UserModel user)
	{
		log.debug("REST request to update User : {}", user);

		return Optional.ofNullable(userRepository.findOne(user.getId())).map(u ->
		{
			u.setUsername(user.getUsername());
			u.setNickname(user.getNickname());
			if (StringUtils.isNoneBlank(user.getPassword()))
			{
				u.setPassword(passwordEncoder.encode(user.getPassword()));
			}
			u.setName(user.getName());
			u.setEmail(user.getEmail());
			u.setPhone(user.getPhone());
			u.setActivated(user.isActivated());
			u.setRoles(user.getRoles());
			u.setEnabled(user.isEnabled());
			userRepository.saveAndFlush(u);
			return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("user", user.getUsername())).body(userRepository.findOne(user.getId()));
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured(ROLE_ADMIN)
	public ResponseEntity<User> getUser(@PathVariable Long id)
	{
		log.debug("REST request to get User : {}", id);
		return Optional.ofNullable(userRepository.findOne(id)).map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping(value = "/user/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured(ROLE_ADMIN)
	public ResponseEntity<User> getbyname(@PathVariable String name)
	{
		return Optional.ofNullable(userRepository.findOne2ByUsername(name)).map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping(value = "/user/{ids}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured(ROLE_ADMIN)
	public ResponseEntity<Void> deleteUser(@PathVariable List<Long> ids)
	{
		log.debug("REST request to delete User : {}", ids);
		for (Long id : ids)
		{
			if (id > 3)
			{ // 系统内置用户无法删除
				userRepository.delete(id);
			}

		}
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("user", ids.toString())).build();
	}

	@RequestMapping(value = "/validate/user/username/unique", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ValidationResult validateUsernameUnique(@RequestBody Validation validation)
	{
		return userRepository.findOneByUsername(validation.getValue()).filter(u -> validation.getId() == null ? true : !validation.getId().equals(u.getId())).map(user -> ValidationResult.INVALID).orElse(ValidationResult.VALID);
	}

	@RequestMapping(value = "/validate/user/email/unique", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ValidationResult validateEmailUnique(@RequestBody Validation validation)
	{
		return userRepository.findOneByEmail(validation.getValue()).filter(u -> validation.getId() == null ? true : !validation.getId().equals(u.getId())).map(user -> ValidationResult.INVALID).orElse(ValidationResult.VALID);

	}

	@RequestMapping(value = "/validate/user/phone/unique", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ValidationResult validatePhoneUnique(@RequestBody Validation validation)
	{
		return userRepository.findOneByPhone(validation.getValue()).filter(u -> validation.getId() == null ? true : !validation.getId().equals(u.getId())).map(user -> ValidationResult.INVALID).orElse(ValidationResult.VALID);

	}

}
