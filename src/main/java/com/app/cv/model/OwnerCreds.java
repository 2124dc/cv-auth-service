package com.app.cv.model;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "id") // Exclude id field from toString output for better readability.
@EqualsAndHashCode(exclude = "id") // Exclude id field from equals and hashCode methods for better performance.
@JsonIgnoreProperties(ignoreUnknown = true) // Ignore any unknown properties during JSON parsing.
@Document(collection = "owner-credentials")
public class OwnerCreds {
    @Id
    private String id;
    private String email;
    private String password;
    @CreatedDate
    private LocalDateTime  createdAt;

    @LastModifiedDate
    private LocalDateTime  modifiedAt;
}
