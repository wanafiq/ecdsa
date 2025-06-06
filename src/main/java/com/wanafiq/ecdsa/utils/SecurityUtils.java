package com.wanafiq.ecdsa.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

    public static String getPrincipalName(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        if (authentication.getPrincipal() instanceof UserDetails ud) {
            return ud.getUsername();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        } else {
            return null;
        }
    }

}
