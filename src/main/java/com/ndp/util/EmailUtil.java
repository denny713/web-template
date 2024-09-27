package com.ndp.util;

import java.util.regex.Pattern;

public class EmailUtil {

    private EmailUtil() {
        super();
    }

    public static boolean isValidEmail(String email) {
        return Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
                .matcher(email).matches();
    }
}
