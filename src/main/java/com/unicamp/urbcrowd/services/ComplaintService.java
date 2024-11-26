package com.unicamp.urbcrowd.services;

import com.unicamp.urbcrowd.controllers.dto.ComplaintRequestDTO;
import com.unicamp.urbcrowd.controllers.dto.ComplaintResponseDTO;
import com.unicamp.urbcrowd.controllers.dto.UserInfoResponseDTO;
import com.unicamp.urbcrowd.controllers.mapper.ComplaintMapper;
import com.unicamp.urbcrowd.models.Complaint;
import com.unicamp.urbcrowd.repositories.ComplaintRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.List;

import static com.unicamp.urbcrowd.controllers.mapper.ComplaintMapper.complaintToComplaintResponseDTO;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserService userService;
    private final S3Service s3Service;

    public ComplaintService(ComplaintRepository complaintRepository,
                            UserService userService,
                            S3Service s3Service) {
        this.complaintRepository = complaintRepository;
        this.userService = userService;
        this.s3Service = s3Service;
    }

    public ComplaintResponseDTO create(ComplaintRequestDTO complaintRequestDTO) throws AccountNotFoundException {
        UserInfoResponseDTO userInfo = userService.getUserInfo();
        return complaintToComplaintResponseDTO(complaintRepository.save(Complaint.builder()
                .address(complaintRequestDTO.address())
                .geolocation(complaintRequestDTO.geolocation())
                .description(complaintRequestDTO.description())
                .userEmail(userInfo.email())
                .build()));
    }
    public ComplaintResponseDTO createWithImage(ComplaintRequestDTO complaintRequestDTO, MultipartFile image) throws AccountNotFoundException, IOException {
        String imageHref = s3Service.uploadFile(image);
        UserInfoResponseDTO userInfo = userService.getUserInfo();
        return complaintToComplaintResponseDTO(complaintRepository.save(Complaint.builder()
                .title(complaintRequestDTO.title())
                .address(complaintRequestDTO.address())
                .geolocation(complaintRequestDTO.geolocation())
                .description(complaintRequestDTO.description())
                .userEmail(userInfo.email())
                .imageHref(imageHref)
                .type(complaintRequestDTO.type())
                .build()));
    }


    public List<ComplaintResponseDTO> findAll(){
        return complaintRepository.findAll().stream().map(ComplaintMapper::complaintToComplaintResponseDTO).toList();
    }

    public ComplaintResponseDTO thumbsUp(String complaintId) throws AccountNotFoundException {
        Complaint complaint = complaintRepository.findById(complaintId).orElseThrow();
        UserInfoResponseDTO userInfo = userService.getUserInfo();
        complaint.thumbsUp(userInfo.id());
        return complaintToComplaintResponseDTO(complaintRepository.save(complaint));
    }
}
