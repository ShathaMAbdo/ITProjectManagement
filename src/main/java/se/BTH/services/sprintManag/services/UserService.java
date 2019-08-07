package se.BTH.services.sprintManag.services;


import se.BTH.services.sprintManag.models.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
    Boolean isAdmin(String userName);
}