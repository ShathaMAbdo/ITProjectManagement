package se.BTH.ITProjectManagement.sprintManag.services;


import se.BTH.ITProjectManagement.sprintManag.models.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
    Boolean isAdmin(String userName);
}