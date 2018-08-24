package org.icddrb.dhis.android.sdk.utils.api;

import java.security.SecureRandom;
import java.util.regex.Pattern;

public class CodeGenerator {
    public static final int CODESIZE = 11;
    private static final Pattern CODE_PATTERN = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9]{10}$");
    public static final int NUMBER_OF_CODEPOINTS = allowedChars.length();
    public static final String allowedChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateCode() {
        return generateCode(11);
    }

    public static String generateCode(int codeSize) {
        SecureRandom sr = new SecureRandom();
        char[] randomChars = new char[codeSize];
        randomChars[0] = letters.charAt(sr.nextInt(letters.length()));
        for (int i = 1; i < codeSize; i++) {
            randomChars[i] = allowedChars.charAt(sr.nextInt(NUMBER_OF_CODEPOINTS));
        }
        return new String(randomChars);
    }

    public static boolean isValidCode(String code) {
        return code != null && CODE_PATTERN.matcher(code).matches();
    }
}
