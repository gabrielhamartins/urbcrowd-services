package com.unicamp.urbcrowd.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Builder.Default
    private String commentId = UUID.randomUUID().toString();
    private String userId;
    private String userName;
    private String text;
    @Builder.Default
    private Integer thumbsUpCount = 0;

    public void thumbsUp(){
        this.thumbsUpCount++;
    }
}
