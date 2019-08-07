package se.BTH.services.sprintManag.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Document(collection = "task")
public class Task {
    @Id
    private String id;
    private String name;
    private Integer priority;
    private Integer storyPoints;
    List<SubTask> subTasks;

    public int findSubTaskIndex(String subtaskid) {
        int index = -1;
        for (SubTask temp : this.subTasks) {
            index++;
            if (temp.getId().equals(subtaskid))
                return index;
        }
        return index;
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
