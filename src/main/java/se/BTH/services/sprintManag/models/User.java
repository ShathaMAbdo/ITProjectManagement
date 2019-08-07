package se.BTH.services.sprintManag.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Document(collection = "user")
public class User  {
    @Id
    private String id;
    private String name;
    @Email
    private String email;
    private String phone;
    private String city;
    private List<Role> roles;
    private boolean active;
   // private CustomerStatus status;

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private  String passwordConfirm;
    public boolean changeActive(){
        return active=!active ;
    }
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "";
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }



}
