package com.rf.requestportal.config;

import com.rf.requestportal.entity.AppUser;
import com.rf.requestportal.entity.RequestStatus;
import com.rf.requestportal.entity.ServiceRequest;
import com.rf.requestportal.repository.AppUserRepository;
import com.rf.requestportal.repository.ServiceRequestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedData(
            AppUserRepository userRepository,
            ServiceRequestRepository requestRepository,
            PasswordEncoder encoder
    ) {
        return args -> {

            if (userRepository.count() == 0) {

                userRepository.save(
                        new AppUser("admin",
                                encoder.encode("admin123"),
                                "ADMIN")
                );

                for (int i = 1; i <= 25; i++) {
                    userRepository.save(
                            new AppUser(
                                    "user" + i,
                                    encoder.encode("123"),
                                    "USER"
                            )
                    );
                }
            }

            if (requestRepository.count() == 0) {

                for (int i = 1; i <= 75; i++) {

                    ServiceRequest request = new ServiceRequest();

                    request.setFullName("Test User " + i);
                    request.setEmail("user" + i + "@mail.com");
                    request.setMessage(
                            "Test request number " + i
                    );

                    if (i % 3 == 0) {
                        request.setStatus(RequestStatus.APPROVED);
                    } else if (i % 5 == 0) {
                        request.setStatus(RequestStatus.REJECTED);
                    } else {
                        request.setStatus(RequestStatus.PENDING);
                    }

                    request.setSubmittedBy(
                            "user" + ((i % 25) + 1)
                    );

                    requestRepository.save(request);
                }
            }
        };
    }
}