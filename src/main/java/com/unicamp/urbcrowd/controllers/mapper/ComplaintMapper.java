package com.unicamp.urbcrowd.controllers.mapper;

import com.unicamp.urbcrowd.controllers.dto.ComplaintResponseDTO;
import com.unicamp.urbcrowd.models.Complaint;

public class ComplaintMapper {

    public static ComplaintResponseDTO complaintToComplaintResponseDTO(Complaint complaint){
        return new ComplaintResponseDTO(
                complaint.getId(),
                complaint.getUserEmail(),
                complaint.getAddress(),
                complaint.getGeolocation(),
                complaint.getImageHref(),
                complaint.getCreatedDate(),
                complaint.getDescription(),
                complaint.getStatus(),
                complaint.getType(),
                complaint.getComments(),
                complaint.getThumbsUpUserIds().size()
        );
    }
}
