package com.unicamp.urbcrowd.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Role {

    @Id
    private long id;
    private String name;

    public enum Values {
        ADMIN(1L),
        DEFAULT(2L);

        final long roleId;

        Values(long roleId) {
            this.roleId = roleId;
        }
    }
}
