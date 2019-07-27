package se.BTH.ITProjectManagement.sprintManag.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.BTH.ITProjectManagement.sprintManag.models.User;
import se.BTH.ITProjectManagement.sprintManag.repositories.UserRepository;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    Collection<User> users() {
        return repository.findAll();
    }

    @GetMapping("/user/{id}")
    ResponseEntity<?> getUser(@PathVariable String id) {
        Optional<User> user = repository.findById(id);
        return user.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> createUser(@Valid @RequestBody User user) throws URISyntaxException {
        log.info("Request to create user: {}", user);
        User result = repository.save(user);
        return ResponseEntity.created(new URI("/api/user/" + result.getId())).body(result);
    }

    @PutMapping("/user")
    ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("Request to update user: {}", user);
        User result = repository.save(user);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        log.info("Request to delete user: {}", id);
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}