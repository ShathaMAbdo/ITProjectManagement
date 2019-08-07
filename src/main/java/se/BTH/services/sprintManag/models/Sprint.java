package se.BTH.services.sprintManag.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Document(collection = "sprint")
public class Sprint {
    @Id
    private String id;
    private Team team;
    private String name;
    private String goal;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate delivery;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate retrospective;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate demo;
    private DayOfWeek review;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime daily_meeting;
    private List<Task> tasks;
    private Integer plannedPeriod; // how many days will sprint take

    public int findTaskIndex(String taskid) {
        int index = -1;
        for (Task temp : this.tasks) {
            index++;
            if (temp.getId().equals(taskid))
                return index;
        }
        return index;
    }

    public LocalDate calcDelivery() {
        int count = 0;
        this.delivery = this.start.minusDays(1);
        // System.out.println("delivery="+this.delivery+"start="+this.start+"count ="+count);
        while (!this.plannedPeriod.equals(count)) {
            if (!(this.delivery.getDayOfWeek().equals(DayOfWeek.SATURDAY)) && !(this.delivery.getDayOfWeek().equals(DayOfWeek.SUNDAY)))
                count++;
            this.delivery = this.delivery.plusDays(1);
            //   System.out.println("delivery="+this.delivery+"start="+this.start+"count ="+count);
        }
        return this.delivery;
    }

    //total eststimate
    public Integer Calculate_total_estimate() {
        int total = tasks.stream().mapToInt(o -> o.getStoryPoints()).sum();
        return total;
    }

    //Actual hours today
    public List<Double> Actual_hours_today_sum() {
        List<Double> Actual_hours_today = new ArrayList<>();
        Double total;
        for (int i = 0; i < plannedPeriod; i++) {
            total = 0.0;
            for (Task t : tasks) {
                int j = i;
                total += t.getSubTasks().stream().mapToInt(st -> st.totalSubtaskActualHours(plannedPeriod).get(j)).sum();
            }
            Actual_hours_today.add(i, total);
        }
        return Actual_hours_today;
    }

    //Planned hours today equal (total eststimate) div plannedPeriod
    public Double Calculate_Planned_hours_today() {
        return Calculate_total_estimate() * 1.0 / plannedPeriod;
    }

    public List<Double> Calculate_actual_remaining() {
        List<Double> Actual_hours_remaining = new ArrayList<>();
        List<Double> Actual_hours_today = Actual_hours_today_sum();
        double remain = 0;
        for (int i = 0; i < plannedPeriod; i++) {
            if (i == 0) remain = Calculate_total_estimate() - Actual_hours_today.get(i);
            else remain -= Actual_hours_today.get(i);

            Actual_hours_remaining.add(i, remain);
        }
        return Actual_hours_remaining;
    }

    public List<Double> Calculate_planned_remaining() {
        List<Double> planned_hours_remaining = new ArrayList<>();
        double plannedToday = Calculate_Planned_hours_today();
        double totalEstimate = Calculate_total_estimate();
        double remain = totalEstimate;
        for (int i = 0; i < plannedPeriod; i++) {
            remain -= plannedToday;
            planned_hours_remaining.add(i, remain);
        }
        return planned_hours_remaining;
    }

    public Boolean isUserInSprint(Principal user) {

        Boolean present = team.getUsers().stream().filter(u -> u.getUsername().equals(user.getName())).findFirst().isPresent();
        return present;

    }

    public Map<String, List<Integer>> totalActualHoursPerUser() {
        List<Integer> temp = new ArrayList<>(), total = new ArrayList<>();
        Map<String, List<Integer>> totalAHPerUser = new HashMap<>();
        for (User u : this.getTeam().getUsers()) {
            temp = new ArrayList<>();
            total=SubTask.intiActualHoursList(this.getPlannedPeriod());
            for (Task task : this.getTasks()) {
                for (SubTask subTask : task.getSubTasks()) {
                    temp = subTask.getUserActualHours().get(u.getUsername());
               //     System.out.println(u.getUsername() + "actual Hours" + temp);
                    if (temp != null) {
                        for (int j = 0; j < temp.size(); j++) {
                         //   System.out.println(u.getUsername() + "actual Hours" + temp+"totla "+total);
                            total.set(j, total.get(j)+temp.get(j));
                        }
                    }
                }
            }
            totalAHPerUser.put(u.getUsername(), total);
        }
        return totalAHPerUser;
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
