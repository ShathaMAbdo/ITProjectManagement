package se.BTH.services.sprintManag.security;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}