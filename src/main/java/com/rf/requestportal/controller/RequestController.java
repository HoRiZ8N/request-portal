package com.rf.requestportal.controller;

import com.rf.requestportal.entity.RequestStatus;
import com.rf.requestportal.entity.ServiceRequest;
import com.rf.requestportal.service.ServiceRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RequestController {

    private static final int PAGE_SIZE = 10;

    private final ServiceRequestService service;

    public RequestController(ServiceRequestService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return isAdmin ? "redirect:/requests" : "redirect:/requests/my";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/requests/new")
    public String showForm(Model model) {
        model.addAttribute("serviceRequest", new ServiceRequest());
        return "form";
    }

    @PostMapping("/requests")
    public String submitRequest(@ModelAttribute ServiceRequest serviceRequest,
                                Authentication auth) {
        serviceRequest.setSubmittedBy(auth.getName());
        service.save(serviceRequest);
        return "redirect:/requests/my?submitted";
    }

    @GetMapping("/requests/my")
    public String myRequests(Model model, Authentication auth) {
        model.addAttribute("requests", service.findByUser(auth.getName()));
        return "my-requests";
    }

    @GetMapping("/requests")
    public String listAll(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<ServiceRequest> result = service.findAll(PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("requests", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("baseUrl", "/requests");
        return "admin-requests";
    }

    @GetMapping("/requests/pending")
    public String listPending(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<ServiceRequest> result =
                service.findByStatus(RequestStatus.PENDING, PageRequest.of(page, PAGE_SIZE));
        model.addAttribute("requests", result.getContent());
        model.addAttribute("page", result);
        model.addAttribute("baseUrl", "/requests/pending");
        model.addAttribute("pendingView", true);
        return "admin-requests";
    }

    @PostMapping("/requests/{id}/approve")
    public String approve(@PathVariable Long id,
                          @RequestParam(defaultValue = "/requests") String returnTo) {
        service.updateStatus(id, RequestStatus.APPROVED);
        return "redirect:" + returnTo;
    }

    @PostMapping("/requests/{id}/reject")
    public String reject(@PathVariable Long id,
                         @RequestParam(defaultValue = "/requests") String returnTo) {
        service.updateStatus(id, RequestStatus.REJECTED);
        return "redirect:" + returnTo;
    }
}
