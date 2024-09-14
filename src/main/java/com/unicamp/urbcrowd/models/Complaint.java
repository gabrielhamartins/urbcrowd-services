package com.unicamp.urbcrowd.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
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
    private String userId;
    private Address address;
    private Geolocation geolocation;
    private String imageHref;
    @CreatedDate
    private LocalDateTime createdDate;
    private String description;
    private Set<Comment> comments;
    private Integer thumbsUpCount;

    private void thumbsUp(){
        this.thumbsUpCount++;
    }

}
