package com.unicamp.urbcrowd.controllers;

import com.unicamp.urbcrowd.controllers.dto.CommentRequestDTO;
import com.unicamp.urbcrowd.controllers.dto.ComplaintRequestDTO;
import com.unicamp.urbcrowd.controllers.dto.ComplaintResponseDTO;
import com.unicamp.urbcrowd.controllers.dto.ComplaitStatusUpdateRequestDTO;
import com.unicamp.urbcrowd.services.ComplaintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping("/complaints")
    public ResponseEntity<ComplaintResponseDTO> create(@RequestBody ComplaintRequestDTO complaintRequestDTO) throws AccountNotFoundException {
        return new ResponseEntity<>(complaintService.create(complaintRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/complaints-image")
    public ResponseEntity<ComplaintResponseDTO> createWithImage(@ModelAttribute ComplaintRequestDTO complaintRequestDTO, @RequestParam("image") MultipartFile image) throws AccountNotFoundException, IOException {
        return new ResponseEntity<>(complaintService.createWithImage(complaintRequestDTO, image), HttpStatus.CREATED);
    }

    @GetMapping("/complaints")
    public ResponseEntity<List<ComplaintResponseDTO>> findAll() throws AccountNotFoundException {
        return ResponseEntity.ok(complaintService.findAll());
    }

    @PatchMapping("/complaints/{complaintId}")
    public ResponseEntity<ComplaintResponseDTO> thumbsUp(@PathVariable String complaintId) throws AccountNotFoundException {
        return ResponseEntity.ok(complaintService.thumbsUp(complaintId));
    }

    @PatchMapping("/complaints/{complaintId}/status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ComplaintResponseDTO> updateStatus(@PathVariable String complaintId, @RequestBody ComplaitStatusUpdateRequestDTO dto) throws AccountNotFoundException {
        return ResponseEntity.ok(complaintService.updateStatus(complaintId, dto.status()));
    }

    @PostMapping("/complaints/{complaintId}/comments")
    public ResponseEntity<ComplaintResponseDTO> comment(@PathVariable String complaintId, @RequestBody CommentRequestDTO comment) throws AccountNotFoundException {
        return ResponseEntity.ok(complaintService.comment(complaintId, comment));
    }

    @DeleteMapping("/complaints/{complaintId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteComplaint(@PathVariable String complaintId) {
        complaintService.delete(complaintId);
        return ResponseEntity.noContent().build();
    }
}
