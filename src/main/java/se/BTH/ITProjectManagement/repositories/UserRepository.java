package se.BTH.ITProjectManagement.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import se.BTH.ITProjectManagement.models.Team;
import se.BTH.ITProjectManagement.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {

    List<User> findByName(@Param("name") String Name);

    User findByEmail(@Param("email") String email);

    List<User> findByPhone(@Param("phone") String phone);
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<User> findAll();


    List<User> findByCity(@Param("city") String city);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
