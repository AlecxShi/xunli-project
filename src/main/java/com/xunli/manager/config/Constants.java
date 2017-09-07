package com.xunli.manager.config;

import java.time.format.DateTimeFormatter;

/**
 * 系统常量。
 * 
 */
public final class Constants
{

	public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
	public static final String SPRING_PROFILE_PRODUCTION = "prod";

	public static final String SYSTEM_ACCOUNT = "system";

	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

	public final static String IMAGE_ROOT_DIR = "/alidata/image/photos/";
	public final static String ICON_ROOT_DIR = "/alidata/image/icons/private/";

	public final static DateTimeFormatter DATE_PATH_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	private Constants()
	{

	}
}
