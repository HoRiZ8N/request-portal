package com.rf.requestportal.controller;

import com.rf.requestportal.config.SecurityConfig;
import com.rf.requestportal.entity.AppUser;
import com.rf.requestportal.service.RegistrationService;
import com.rf.requestportal.service.UsernameAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@Import(SecurityConfig.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrationService registrationService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void registerPage_isPublic() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void register_validInput_redirectsToLogin() throws Exception {
        when(registrationService.register("alice", "secret"))
                .thenReturn(new AppUser("alice", "HASH", "USER"));

        mockMvc.perform(post("/register")
                        .param("username", "alice")
                        .param("password", "secret")
                        .param("confirmPassword", "secret")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        verify(registrationService).register(eq("alice"), eq("secret"));
    }

    @Test
    void register_passwordMismatch_returnsFormWithError() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "alice")
                        .param("password", "secret")
                        .param("confirmPassword", "different")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void register_takenUsername_returnsFormWithError() throws Exception {
        when(registrationService.register(any(), any()))
                .thenThrow(new UsernameAlreadyExistsException("alice"));

        mockMvc.perform(post("/register")
                        .param("username", "alice")
                        .param("password", "secret")
                        .param("confirmPassword", "secret")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));
    }
}
