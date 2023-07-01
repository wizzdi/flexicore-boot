package com.flexicore.service;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by Asaf on 15/01/2017.
 */
public class PasswordGenerator {
    private static Random random = new SecureRandom();
    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public static String generateRandom(int length) {
            if (length <= 0) {
                throw new IllegalArgumentException("String length must be a positive integer");
            }

            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append(characters.charAt(random.nextInt(characters.length())));
            }

            return sb.toString();
        }

}
