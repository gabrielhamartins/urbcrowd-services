package com.unicamp.urbcrowd.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Complaint {

    @Id
    private String id;
    @Indexed
    private String userEmail;
    private String title;
    private Address address;
    private Geolocation geolocation;
    private String imageHref;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    private String description;
    private Set<Comment> comments;
    @Builder.Default
    private Set<String> thumbsUpUserIds = new HashSet<>();
    @Builder.Default
    private ComplaintStatus status = ComplaintStatus.OPEN;
    private ComplaintType type;
    public void thumbsUp(String userId) {
        this.thumbsUpUserIds.add(userId);
    }

}
