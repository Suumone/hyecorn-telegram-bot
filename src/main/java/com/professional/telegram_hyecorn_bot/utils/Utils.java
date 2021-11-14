package com.professional.telegram_hyecorn_bot.utils;


import org.apache.commons.validator.routines.EmailValidator;

public class Utils {
    public static boolean isValidEmail(String email){
        return EmailValidator.getInstance().isValid(email);
    }
}
