package com.unicamp.urbcrowd.controllers.mapper;

import com.unicamp.urbcrowd.controllers.dto.CommentDTO;
import com.unicamp.urbcrowd.controllers.dto.ComplaintResponseDTO;
import com.unicamp.urbcrowd.models.Comment;
import com.unicamp.urbcrowd.models.Complaint;

import java.util.stream.Collectors;

public class ComplaintMapper {

    public static ComplaintResponseDTO complaintToComplaintResponseDTO(Complaint complaint, String userId){
        return new ComplaintResponseDTO(
                complaint.getId(),
                complaint.getTitle(),
                complaint.getUserEmail(),
                complaint.getAddress(),
                complaint.getGeolocation(),
                complaint.getImageHref(),
                complaint.getCreatedDate(),
                complaint.getDescription(),
                complaint.getStatus(),
                complaint.getType(),
                complaint.getComments() == null ? null : complaint.getComments().stream().map(ComplaintMapper::commentToCommentDTO).collect(Collectors.toSet()),
                complaint.getThumbsUpUserIds().size(),
                complaint.userHasThumbsUp(userId)
        );
    }

    public static CommentDTO commentToCommentDTO(Comment comment){
        return new CommentDTO(
                comment.getUserName(),
                comment.getText()
        );
    }
}
