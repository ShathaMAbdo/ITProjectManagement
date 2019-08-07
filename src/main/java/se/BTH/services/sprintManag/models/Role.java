package se.BTH.services.sprintManag.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Document(collection = "role")
public class Role {
    @Id
    private String id;
    private RoleName name;

}
