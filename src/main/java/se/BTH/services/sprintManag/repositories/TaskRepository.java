package se.BTH.services.sprintManag.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import se.BTH.services.sprintManag.models.SubTask;
import se.BTH.services.sprintManag.models.Task;

import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task,String>{

    Optional<Task> findByName(@Param("name") String name);

    Optional<Task> findByPriority(@Param("priority") Integer priority);

    Optional<Task> findBySubTasks(@Param("subTask") SubTask subTask);
}
