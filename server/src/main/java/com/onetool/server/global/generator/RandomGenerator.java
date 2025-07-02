package com.onetool.server.global.generator;

import com.onetool.server.global.exception.BusinessLogicException;
import com.onetool.server.global.exception.codes.ErrorCode;
import com.onetool.server.global.new_exception.exception.ApiException;
import com.onetool.server.global.new_exception.exception.error.EmailErrorCode;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class RandomGenerator {

    public static String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(EmailErrorCode.AUTH_CODE_ERROR);
        }
    }

    public static String createRandomPassword() {
        int length = 15;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(EmailErrorCode.RANDOM_PASSWORD_ERROR,"생성 알고리즘 문제");
        }
    }
}
