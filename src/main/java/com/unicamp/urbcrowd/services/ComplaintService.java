package com.unicamp.urbcrowd.services;

import com.unicamp.urbcrowd.controllers.dto.CommentRequestDTO;
import com.unicamp.urbcrowd.controllers.dto.ComplaintRequestDTO;
import com.unicamp.urbcrowd.controllers.dto.ComplaintResponseDTO;
import com.unicamp.urbcrowd.controllers.dto.UserInfoResponseDTO;
import com.unicamp.urbcrowd.models.Comment;
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
                .title(complaintRequestDTO.title())
                .address(complaintRequestDTO.address())
                .geolocation(complaintRequestDTO.geolocation())
                .description(complaintRequestDTO.description())
                .userEmail(userInfo.email())
                .type(complaintRequestDTO.type())
                .build()), userInfo.id());
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
                .build()), userInfo.id());
    }


    public List<ComplaintResponseDTO> findAll() throws AccountNotFoundException {
        UserInfoResponseDTO userInfo = userService.getUserInfo();
        return complaintRepository.findAll().stream().map(complaint -> complaintToComplaintResponseDTO(complaint, userInfo.id())).toList();
    }

    public ComplaintResponseDTO thumbsUp(String complaintId) throws AccountNotFoundException {
        Complaint complaint = complaintRepository.findById(complaintId).orElseThrow();
        UserInfoResponseDTO userInfo = userService.getUserInfo();
        if(complaint.userHasThumbsUp(userInfo.id())){
            complaint.revertThumbsUp(userInfo.id());
        } else {
            complaint.thumbsUp(userInfo.id());
        }
        return complaintToComplaintResponseDTO(complaintRepository.save(complaint), userInfo.id());
    }

    public ComplaintResponseDTO comment(String complaintId, CommentRequestDTO commentDto) throws AccountNotFoundException {
        Complaint complaint = complaintRepository.findById(complaintId).orElseThrow();
        UserInfoResponseDTO userInfo = userService.getUserInfo();
        complaint.addComment(Comment.builder()
                .userId(userInfo.id())
                .userName(userInfo.name())
                .text(commentDto.comment())
                .build());
        return complaintToComplaintResponseDTO(this.complaintRepository.save(complaint), userInfo.id());
    }
}
