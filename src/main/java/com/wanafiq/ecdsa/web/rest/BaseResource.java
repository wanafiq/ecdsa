package com.wanafiq.ecdsa.web.rest;

import com.wanafiq.ecdsa.common.SecurityUtils;

public class BaseResource {

    protected boolean loggedIn() {
        String principalName = SecurityUtils.getPrincipalName();
        return principalName != null;
    }

    protected String getPrincipalName() {
        return SecurityUtils.getPrincipalName();
    }

}
