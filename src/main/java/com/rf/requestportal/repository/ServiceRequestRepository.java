package com.rf.requestportal.repository;

import com.rf.requestportal.entity.RequestStatus;
import com.rf.requestportal.entity.ServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    List<ServiceRequest> findBySubmittedByOrderByCreatedAtDesc(String submittedBy);

    List<ServiceRequest> findAllByOrderByCreatedAtDesc();

    Page<ServiceRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ServiceRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status, Pageable pageable);
}
