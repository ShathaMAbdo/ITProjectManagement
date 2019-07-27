package se.BTH.ITProjectManagement.sprintManag.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.Principal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Document(collection = "team")
public class Team {

    @Id
    private String id;
    private String name;
    private boolean active;
    List<User> users;

    public boolean changeActive() {
        return active = !active;
    }

    public boolean isMememberExcit(User user) {
        for (User u : this.getUsers()) {
            if (user.getId().equals(u.getId())) {
                return true;
            }
        }
        return false;
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
  public Boolean isUserInTeam(Principal user){
     Boolean present =users.stream().filter(u ->u.getUsername().equals(user.getName())).findFirst().isPresent();
        return present;
  }
}
