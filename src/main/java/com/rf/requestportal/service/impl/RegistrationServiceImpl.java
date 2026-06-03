package com.rf.requestportal.service.impl;

import com.rf.requestportal.entity.AppUser;
import com.rf.requestportal.repository.AppUserRepository;
import com.rf.requestportal.service.RegistrationService;
import com.rf.requestportal.service.UsernameAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationServiceImpl(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public AppUser register(String username, String rawPassword) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
        AppUser user = new AppUser(
                username,
                passwordEncoder.encode(rawPassword),
                "USER"
        );
        return userRepository.save(user);
    }
}
