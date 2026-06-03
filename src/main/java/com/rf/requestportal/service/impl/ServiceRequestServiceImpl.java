package com.rf.requestportal.service.impl;

import com.rf.requestportal.entity.RequestStatus;
import com.rf.requestportal.entity.ServiceRequest;
import com.rf.requestportal.repository.ServiceRequestRepository;
import com.rf.requestportal.service.ServiceRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestRepository repository;

    public ServiceRequestServiceImpl(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceRequest save(ServiceRequest request) {
        return repository.save(request);
    }

    @Override
    public List<ServiceRequest> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Page<ServiceRequest> findAll(Pageable pageable) {
        return repository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public Page<ServiceRequest> findByStatus(RequestStatus status, Pageable pageable) {
        return repository.findByStatusOrderByCreatedAtDesc(status, pageable);
    }

    @Override
    public List<ServiceRequest> findByUser(String username) {
        return repository.findBySubmittedByOrderByCreatedAtDesc(username);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, RequestStatus status) {
        ServiceRequest req = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + id));
        req.setStatus(status);
    }
}
