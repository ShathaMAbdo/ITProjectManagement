package se.BTH.ITProjectManagement.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        for (int i = 0; i < plannedPeriod - 1; i++) {
            total = 0.0;
            for (Task t : tasks) {
                int j = i;
                total += t.getSubTasks().stream().mapToInt(st -> st.getActualHours().get(j)).sum();
            }
            Actual_hours_today.set(i, total);
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
        for (int i = 0; i < plannedPeriod - 1; i++) {
            if (i == 0) remain = Calculate_total_estimate() - Actual_hours_today.get(i);
            else remain -= Actual_hours_today.get(i);

            Actual_hours_remaining.set(i, remain);
        }
        return Actual_hours_remaining;
    }

    public List<Double> Calculate_planned_remaining() {
        List<Double> planned_hours_remaining = new ArrayList<>();
        double plannedToday = Calculate_Planned_hours_today();
        double totalEstimate = Calculate_total_estimate();
        double remain = totalEstimate;
        for (int i = 0; i < plannedPeriod - 1; i++) {
            remain -= plannedToday;
            planned_hours_remaining.set(i, remain);
        }
        return planned_hours_remaining;
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
