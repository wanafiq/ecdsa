package com.wanafiq.ecdsa.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {

    public static String getPrincipalName() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication  authentication = securityContext.getAuthentication();
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
