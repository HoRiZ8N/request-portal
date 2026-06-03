package com.rf.requestportal.service;

import com.rf.requestportal.entity.AppUser;

public interface RegistrationService {

    AppUser register(String username, String rawPassword);
}
