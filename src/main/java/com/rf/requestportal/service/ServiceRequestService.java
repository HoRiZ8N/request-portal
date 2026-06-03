package com.rf.requestportal.service;

import com.rf.requestportal.entity.RequestStatus;
import com.rf.requestportal.entity.ServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceRequestService {

    ServiceRequest save(ServiceRequest request);

    List<ServiceRequest> findAll();

    Page<ServiceRequest> findAll(Pageable pageable);

    Page<ServiceRequest> findByStatus(RequestStatus status, Pageable pageable);

    List<ServiceRequest> findByUser(String username);

    void updateStatus(Long id, RequestStatus status);
}
