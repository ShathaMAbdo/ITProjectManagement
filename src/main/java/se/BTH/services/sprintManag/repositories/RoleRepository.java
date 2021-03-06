package se.BTH.services.sprintManag.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import se.BTH.services.sprintManag.models.Role;
import se.BTH.services.sprintManag.models.RoleName;

@Transactional
public interface RoleRepository extends MongoRepository<Role,String> {
    Role findByName(RoleName name);
}
