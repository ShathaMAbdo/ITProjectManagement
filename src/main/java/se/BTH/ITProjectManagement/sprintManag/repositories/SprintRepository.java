package se.BTH.ITProjectManagement.sprintManag.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import se.BTH.ITProjectManagement.sprintManag.models.Sprint;
import se.BTH.ITProjectManagement.sprintManag.models.Task;
import se.BTH.ITProjectManagement.sprintManag.models.Team;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SprintRepository extends MongoRepository<Sprint,String> {

    Optional<Sprint> findByTeam(@Param("team")Team team);

    Optional<Sprint> findByTasks(@Param("tasks") Task task);

    Optional<Sprint> findByName(@Param("name") String name);

    Optional<Sprint> findByGoal(@Param("goal") String goal);

    List<Sprint> findByDelivery(@Param("delivery") LocalDate delivery);

    List<Sprint> findByDemo(@Param("demo") LocalDate demo);

    Optional<Sprint> findByRetrospective(@Param("retrospective") LocalDate retrospective);
}
