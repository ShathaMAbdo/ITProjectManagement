package se.BTH.ITProjectManagement.webmvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.BTH.ITProjectManagement.sprintManag.models.Role;
import se.BTH.ITProjectManagement.sprintManag.repositories.RoleRepository;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RoleController {

    private final Logger log = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleRepository repository;

    public RoleController(RoleRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/roles")
    Collection<Role> roles() {
        return repository.findAll();
    }

    @GetMapping("/role/{id}")
    ResponseEntity<?> getRole(@PathVariable String id) {
        Optional<Role> role = repository.findById(id);
        return role.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws URISyntaxException {
        log.info("Request to create role: {}", role);
        Role result = repository.save(role);
        return ResponseEntity.created(new URI("/api/role/" + result.getId())).body(result);
    }

    @PutMapping("/role")
    ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) {
        log.info("Request to update role: {}", role);
        Role result = repository.save(role);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/role/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable String id) {
        log.info("Request to delete role: {}", id);
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}