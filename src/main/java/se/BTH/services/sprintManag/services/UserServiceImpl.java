package se.BTH.services.sprintManag.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import se.BTH.services.sprintManag.models.Role;
import se.BTH.services.sprintManag.models.RoleName;
import se.BTH.services.sprintManag.models.User;
import se.BTH.services.sprintManag.repositories.RoleRepository;
import se.BTH.services.sprintManag.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        List list=new ArrayList();
        list.add(roleRepository.findByName(RoleName.ROLE_USER));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new ArrayList<>(list));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        User user=userRepository.findByUsername(username);
        return user;
    }
    @Override
    public Boolean isAdmin(String userName){
        User currentUser=userRepository.findByUsername(userName);
        Optional<Role> optiona=currentUser.getRoles().stream().filter(r->r.getName().equals(RoleName.ROLE_ADMIN)).findFirst();
        Boolean isAdmin=(optiona).isPresent();
        return isAdmin;
    }
}