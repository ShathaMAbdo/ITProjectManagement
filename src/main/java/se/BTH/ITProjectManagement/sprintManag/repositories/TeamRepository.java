package se.BTH.ITProjectManagement.sprintManag.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import se.BTH.ITProjectManagement.sprintManag.models.Team;
import java.util.Optional;

public interface TeamRepository extends MongoRepository<Team,String> {
    Optional<Team> findByName(@Param("name") String name);

}
