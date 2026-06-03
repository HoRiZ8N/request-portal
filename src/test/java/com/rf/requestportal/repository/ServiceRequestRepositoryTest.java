package com.rf.requestportal.repository;

import com.rf.requestportal.entity.RequestStatus;
import com.rf.requestportal.entity.ServiceRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ServiceRequestRepositoryTest {

    @Autowired
    private ServiceRequestRepository repository;

    private ServiceRequest newRequest(String submittedBy, String name) {
        ServiceRequest r = new ServiceRequest();
        r.setFullName(name);
        r.setEmail(name + "@example.com");
        r.setMessage("test message");
        r.setSubmittedBy(submittedBy);
        return r;
    }

    @Test
    void save_assignsIdAndCreatedAt() {
        ServiceRequest saved = repository.save(newRequest("user", "Иван"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();           // выставлено @PrePersist
        assertThat(saved.getStatus()).isEqualTo(RequestStatus.PENDING); // дефолт
    }

    @Test
    void findBySubmittedBy_returnsOnlyThatUsersRequests() {
        repository.save(newRequest("user", "Иван"));
        repository.save(newRequest("user", "Пётр"));
        repository.save(newRequest("admin", "Админ"));

        List<ServiceRequest> userRequests =
                repository.findBySubmittedByOrderByCreatedAtDesc("user");

        assertThat(userRequests).hasSize(2);
        assertThat(userRequests)
                .extracting(ServiceRequest::getSubmittedBy)
                .containsOnly("user");
    }

    @Test
    void findAll_isOrderedByCreatedAtDesc() throws InterruptedException {
        ServiceRequest first = repository.save(newRequest("user", "Первый"));
        Thread.sleep(5); // гарантируем разное время создания
        ServiceRequest second = repository.save(newRequest("user", "Второй"));

        List<ServiceRequest> all = repository.findAllByOrderByCreatedAtDesc();

        assertThat(all).hasSize(2);
        assertThat(all.get(0).getId()).isEqualTo(second.getId());
        assertThat(all.get(1).getId()).isEqualTo(first.getId());
    }

    @Test
    void findBySubmittedBy_unknownUser_returnsEmpty() {
        repository.save(newRequest("user", "Иван"));

        assertThat(repository.findBySubmittedByOrderByCreatedAtDesc("nobody"))
                .isEmpty();
    }
}
