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
    private Integer thumbsUpCount = 0;

    private void thumbsUp() {
        this.thumbsUpCount++;
    }

}
