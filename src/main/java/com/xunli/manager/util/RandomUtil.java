package com.xunli.manager.util;

import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.codec.Base64;

import java.security.SecureRandom;

public final class RandomUtil {
  private static final int DEF_PASS_COUNT = 6;
  private static final int DEF_COUNT = 20;
  private static final int DEFAULT_TOKEN_LENGTH = 16;
  private static final SecureRandom random = new SecureRandom();

  private RandomUtil() {}

  public static String generatePassword() {
    return RandomStringUtils.randomAlphanumeric(DEF_PASS_COUNT);
  }

  public static String generateActivationKey() {
    return RandomStringUtils.randomNumeric(DEF_COUNT);
  }

  public static String generateResetKey() {
    return RandomStringUtils.randomNumeric(DEF_COUNT);
  }

  public static String generateToken()
  {
    /*byte[] newToken = new byte[DEFAULT_TOKEN_LENGTH];
    random.nextBytes(newToken);
    return new String(Base64.encode(newToken));*/
    return UUID.randomUUID().toString().replace("-", "");
  }
}
