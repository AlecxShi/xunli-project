package com.xunli.manager.web;

import com.xunli.manager.config.AppProperties;
import com.xunli.manager.domain.PersistentLogin;
import com.xunli.manager.domain.User;
import com.xunli.manager.model.Account;
import com.xunli.manager.model.KeyAndPassword;
import com.xunli.manager.repository.PersistentLoginRepository;
import com.xunli.manager.repository.UserRepository;
import com.xunli.manager.security.SecurityUtils;
import com.xunli.manager.service.UserService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import static com.xunli.manager.config.Constants.ROLE_ADMIN;
import static com.xunli.manager.config.Constants.ROLE_USER;

@RestController
@RequestMapping("/api")
public class AccountController
{
	private final Logger log = LoggerFactory.getLogger(AccountController.class);

	private static final Map<String, MediaType> mediaTypeMap;

	static
	{
		mediaTypeMap = new HashMap<>();
		mediaTypeMap.put("jpg", MediaType.IMAGE_JPEG);
		mediaTypeMap.put("jpeg", MediaType.IMAGE_JPEG);
		mediaTypeMap.put("gif", MediaType.IMAGE_GIF);
		mediaTypeMap.put("png", MediaType.IMAGE_PNG);
		mediaTypeMap.put("svg", MediaType.parseMediaType("image/svg+xml"));
	}

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PersistentLoginRepository persistentLoginRepository;

	//@Autowired
	//private MailService mailService;

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> registerAccount(@Valid @RequestBody Account account, HttpServletRequest request)
	{
		log.debug("registerAccount {}", account);
		User user = userService.createUserInformation(account.getUsername(), account.getPassword(), account.getEmail());

		//String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

		//mailService.sendActivationEmail(user, baseUrl);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/activate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> sendActivationEmail(@RequestParam String email, HttpServletRequest request)
	{
		return userService.requestActivation(email).map(user ->
		{
			//String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

			//mailService.sendActivationEmail(user, baseUrl);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}).orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}

	@RequestMapping(value = "/activate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> activateAccount(@RequestParam String key)
	{
		return userService.activateRegistration(key).map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String isAuthenticated(HttpServletRequest request)
	{
		log.debug("REST request to check if the current user is authenticated");
		return request.getRemoteUser();
	}

	@RequestMapping(value = "/account", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured({ ROLE_USER, ROLE_ADMIN })
	public ResponseEntity<User> getAccount()
	{
		return Optional.ofNullable(userService.getUserWithAuthorities()).map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@RequestMapping(value = "/account", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured({ ROLE_USER, ROLE_ADMIN })
	public ResponseEntity<User> saveAccount(@RequestBody User user)
	{
		return userRepository.findOneByUsername(user.getUsername()).filter(u -> u.getUsername().equals(SecurityUtils.getCurrentUsername())).map(u ->
		{
			u = userService.updateUserInformation(user.getNickname(), user.getName(), user.getEmail(), user.getPhone()).get();
			return new ResponseEntity<>(u, HttpStatus.OK);
		}).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@RequestMapping(value = "/password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured({ ROLE_USER, ROLE_ADMIN })
	public ResponseEntity<?> changePassword(@RequestBody Map<String,Object> param)
	{
		if(userService.checkPassword(String.valueOf(param.get("originalPassword"))).get()){
			userService.changePassword(String.valueOf(param.get("password")));
			return new ResponseEntity<>(HttpStatus.OK);
		}else{
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@RequestMapping(value = "/sessions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Secured({ ROLE_USER, ROLE_ADMIN })
	public ResponseEntity<List<PersistentLogin>> getCurrentSessions()
	{
		return userRepository.findOneByUsername(SecurityUtils.getCurrentUsername()).map(user -> new ResponseEntity<>(persistentLoginRepository.findByUser(user), HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@RequestMapping(value = "/sessions/{series}", method = RequestMethod.DELETE)
	@Secured({ ROLE_USER, ROLE_ADMIN })
	public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException
	{
		String decodedSeries = URLDecoder.decode(series, "UTF-8");
		userRepository.findOneByUsername(SecurityUtils.getCurrentUsername()).ifPresent(u ->
		{
			persistentLoginRepository.findByUser(u).stream().filter(login -> StringUtils.equals(login.getSeries(), decodedSeries)).findAny().ifPresent(t -> persistentLoginRepository.delete(decodedSeries));
		});
	}

	@RequestMapping(value = "/reset", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> requestPasswordReset(@RequestParam String email, HttpServletRequest request)
	{
		return userService.requestPasswordReset(email).map(user ->
		{
			//String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
			//mailService.sendPasswordResetMail(user, baseUrl);
			return new ResponseEntity<>(HttpStatus.OK);
		}).orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> finishPasswordReset(@Valid @RequestBody KeyAndPassword keyAndPassword)
	{
		return userService.completePasswordReset(keyAndPassword.getPassword(), keyAndPassword.getKey()).map(user -> new ResponseEntity<>(HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@RequestMapping(value = "/avatar", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@Secured({ ROLE_USER, ROLE_ADMIN })
	public ResponseEntity<User> removeAvatar(HttpServletRequest request)
	{
		User user = userRepository.findOne(SecurityUtils.getCurrentUserId());
		if (null == user)
		{
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		// String realPath = request.getSession().getServletContext()
		// .getRealPath("/" + user.getAvatar());
		// FileUtils.deleteQuietly(new File(realPath));
		user.setAvatar(null);
		userRepository.save(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/avatar", method = RequestMethod.GET)
	@Secured({ ROLE_USER, ROLE_ADMIN })
	public ResponseEntity<byte[]> getAvatar()
	{
		User user = userService.getUserWithAuthorities();
		if (null == user || StringUtils.isBlank(user.getAvatar()))
		{
			return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
		}

		try
		{

			String basePath = appProperties.getWork().getDirectory();

			log.debug("basePath: {}", basePath);

			String realPath = FilenameUtils.concat(basePath, user.getAvatar());

			byte[] b = FileUtils.readFileToByteArray(new File(realPath));
			HttpHeaders headers = new HttpHeaders();

			String ext = FilenameUtils.getExtension(realPath);
			MediaType mediaType = mediaTypeMap.get(ext.toLowerCase());

			if (null != mediaType)
			{
				headers.setContentType(mediaType);
			}

			return new ResponseEntity<byte[]>(b, headers, HttpStatus.OK);
		}
		catch (IOException e)
		{

			log.error(e.getMessage(), e);
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/avatar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	@Secured({ ROLE_USER, ROLE_ADMIN })
	public ResponseEntity<User> setAvatar(@RequestPart("file") Part part)
	{

		try
		{
			User user = userService.getUserWithAuthorities();

			String path = "assets/avatar/" + user.getId() + "/" + UUID.randomUUID().toString() + ".jpg";

			String basePath = appProperties.getWork().getDirectory();

			log.debug("basePath: {}", basePath);

			String destination = FilenameUtils.concat(basePath, path);

			log.debug("destination = {}", destination);

			FileUtils.copyInputStreamToFile(part.getInputStream(), new File(destination));

			user.setAvatar(path);

			userRepository.save(user);

			return new ResponseEntity<User>(user, HttpStatus.OK);

		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);

			return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
