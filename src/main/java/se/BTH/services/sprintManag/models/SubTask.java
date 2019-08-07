package se.BTH.services.sprintManag.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import se.BTH.services.sprintManag.Annotations.PositiveNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Document(collection = "subTask")
public class SubTask {
    @Id
    private String id;
    private String name;
    private TaskStatus status;
    @PositiveNumber
    private Integer OEstimate; //planned hours
    // private List<Integer> actualHours;
    private List<User> users;
    private Map<String, List<Integer>> userActualHours;

    public static List<Integer> intiActualHoursList(int dayes) {
        List<Integer> temp = new ArrayList<>();
        for (int i = 0; i < dayes; i++) {
            temp.add(0);
        }
        return temp;
    }

    public List<Integer> totalSubtaskActualHours(Integer dayes) {
        List<Integer> temp, total = new ArrayList<>();
        total=SubTask.intiActualHoursList(dayes);
        for (User u : this.getUsers()) {
            temp = this.getUserActualHours().get(u.getUsername());
            for (int j = 0; j < temp.size(); j++) {
                total.set(j, total.get(j)+temp.get(j));
            }
        }
        return total;
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
