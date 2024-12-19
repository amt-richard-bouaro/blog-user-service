package com.amalitech.usermanagementservice.services;

import com.amalitech.usermanagementservice.model.User;

public interface AuthService {
    void authenticateUser(String username, String password);

    User getAuthenticatedUser();
}
