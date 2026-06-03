package com.rf.requestportal.controller;

import com.rf.requestportal.config.SecurityConfig;
import com.rf.requestportal.entity.RequestStatus;
import com.rf.requestportal.entity.ServiceRequest;
import com.rf.requestportal.service.ServiceRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
@Import(SecurityConfig.class)
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceRequestService service;

    @MockitoBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Test
    void protectedPage_anonymous_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/requests/my"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void loginPage_isPublic() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void showForm_asUser_ok() throws Exception {
        mockMvc.perform(get("/requests/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeExists("serviceRequest"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void submitRequest_asUser_savesWithUsernameAndRedirects() throws Exception {
        when(service.save(any(ServiceRequest.class))).thenReturn(new ServiceRequest());

        mockMvc.perform(post("/requests")
                        .param("fullName", "Иван")
                        .param("email", "ivan@example.com")
                        .param("message", "текст")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/requests/my?submitted"));

        verify(service).save(any(ServiceRequest.class));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void myRequests_asUser_ok() throws Exception {
        when(service.findByUser("user")).thenReturn(List.of());

        mockMvc.perform(get("/requests/my"))
                .andExpect(status().isOk())
                .andExpect(view().name("my-requests"))
                .andExpect(model().attributeExists("requests"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void adminList_asUser_isForbidden() throws Exception {
        mockMvc.perform(get("/requests"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminList_asAdmin_ok() throws Exception {
        Page<ServiceRequest> empty = new PageImpl<>(List.of());
        when(service.findAll(any(Pageable.class))).thenReturn(empty);

        mockMvc.perform(get("/requests"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-requests"))
                .andExpect(model().attributeExists("requests"))
                .andExpect(model().attributeExists("page"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void pendingList_asAdmin_ok() throws Exception {
        Page<ServiceRequest> empty = new PageImpl<>(List.of());
        when(service.findByStatus(eq(RequestStatus.PENDING), any(Pageable.class))).thenReturn(empty);

        mockMvc.perform(get("/requests/pending"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-requests"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attribute("pendingView", true));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void pendingList_asUser_isForbidden() throws Exception {
        mockMvc.perform(get("/requests/pending"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void approve_asAdmin_updatesStatusAndRedirects() throws Exception {
        mockMvc.perform(post("/requests/5/approve").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/requests"));

        verify(service).updateStatus(eq(5L), eq(RequestStatus.APPROVED));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void reject_asAdmin_updatesStatusAndRedirects() throws Exception {
        mockMvc.perform(post("/requests/7/reject").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/requests"));

        verify(service).updateStatus(eq(7L), eq(RequestStatus.REJECTED));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void approve_asUser_isForbidden() throws Exception {
        mockMvc.perform(post("/requests/5/approve").with(csrf()))
                .andExpect(status().isForbidden());
    }
}
