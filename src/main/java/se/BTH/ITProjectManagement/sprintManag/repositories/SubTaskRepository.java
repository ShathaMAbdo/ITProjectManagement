package se.BTH.ITProjectManagement.sprintManag.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import se.BTH.ITProjectManagement.sprintManag.models.SubTask;
import se.BTH.ITProjectManagement.sprintManag.models.Task;
import se.BTH.ITProjectManagement.sprintManag.models.User;

import java.util.Optional;

public interface SubTaskRepository extends MongoRepository<SubTask,String> {
    Optional<SubTask> findByName(@Param("name") String name);

    Optional<SubTask> findByUsers(@Param("user") User user);
}
