package com.professional.telegram_hyecorn_bot.utils;


import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.LongValidator;
import javax.validation.constraints.NotNull;

public class Utils {
    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        phoneNumber = getValidPhoneString(phoneNumber);
        if (phoneNumber.length() != 10) {
            return false;
        }
        return LongValidator.getInstance().isValid(phoneNumber);
    }

    public static String validatePhoneNumber(String phoneNumber) {
        return getValidPhoneString(phoneNumber);
    }

    @NotNull
    private static String getValidPhoneString(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("\\s", "");
        phoneNumber = phoneNumber.replaceFirst("\\+", "");
        phoneNumber = phoneNumber.replaceFirst("\\(", "");
        phoneNumber = phoneNumber.replaceFirst("\\)", "");
        phoneNumber = phoneNumber.replaceAll("-", "");

        if (phoneNumber.length() == 11 && phoneNumber.charAt(0) == '7' || phoneNumber.charAt(0) == '8') {
            phoneNumber = phoneNumber.substring(1);
        }
        return phoneNumber;
    }
}
