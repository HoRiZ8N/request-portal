package com.rf.requestportal.service.impl;

import com.rf.requestportal.entity.RequestStatus;
import com.rf.requestportal.entity.ServiceRequest;
import com.rf.requestportal.repository.ServiceRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceRequestServiceImplTest {

    @Mock
    private ServiceRequestRepository repository;

    @InjectMocks
    private ServiceRequestServiceImpl service;

    private ServiceRequest request;

    @BeforeEach
    void setUp() {
        request = new ServiceRequest();
        request.setId(1L);
        request.setFullName("Иван Иванов");
        request.setEmail("ivan@example.com");
        request.setMessage("Прошу подключить услугу");
        request.setSubmittedBy("user");
        request.setStatus(RequestStatus.PENDING);
    }

    @Test
    void save_delegatesToRepositoryAndReturnsSaved() {
        when(repository.save(request)).thenReturn(request);

        ServiceRequest result = service.save(request);

        assertThat(result).isSameAs(request);
        verify(repository).save(request);
    }

    @Test
    void findAll_returnsRepositoryResult() {
        when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(request));

        List<ServiceRequest> result = service.findAll();

        assertThat(result).containsExactly(request);
        verify(repository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void findByUser_passesUsernameToRepository() {
        when(repository.findBySubmittedByOrderByCreatedAtDesc("user"))
                .thenReturn(List.of(request));

        List<ServiceRequest> result = service.findByUser("user");

        assertThat(result).containsExactly(request);
        verify(repository).findBySubmittedByOrderByCreatedAtDesc("user");
    }

    @Test
    void updateStatus_existingRequest_changesStatus() {
        when(repository.findById(1L)).thenReturn(Optional.of(request));

        service.updateStatus(1L, RequestStatus.APPROVED);

        assertThat(request.getStatus()).isEqualTo(RequestStatus.APPROVED);
        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void updateStatus_missingRequest_throws() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateStatus(99L, RequestStatus.REJECTED))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("99");

        verify(repository).findById(99L);
    }
}
