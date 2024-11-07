package com.unicamp.urbcrowd.controllers;

import com.unicamp.urbcrowd.controllers.dto.ComplaintRequestDTO;
import com.unicamp.urbcrowd.models.Complaint;
import com.unicamp.urbcrowd.repositories.ComplaintRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ComplaintController {

    private final ComplaintRepository complaintRepository;

    public ComplaintController(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @PostMapping("/complaints")
    public ResponseEntity<String> create(@RequestBody ComplaintRequestDTO complaintRequestDTO, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Complaint savedComplaint = this.complaintRepository.save(Complaint.builder()
                .address(complaintRequestDTO.address())
                .geolocation(complaintRequestDTO.geolocation())
                .description(complaintRequestDTO.description())
                .userEmail(jwt.getClaim("email"))
                .build());

        return new ResponseEntity<>(savedComplaint.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/complaints")
    public ResponseEntity<List<Complaint>> findAll() {
        return ResponseEntity.ok(this.complaintRepository.findAll());
    }
}
