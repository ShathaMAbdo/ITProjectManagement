package se.BTH.services.sprintManag.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.BTH.services.sprintManag.models.Sprint;
import se.BTH.services.sprintManag.repositories.SprintRepository;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
//@RequestMapping("/api")
public class SprintController {
    private final Logger log = LoggerFactory.getLogger(SprintController.class);

    @Autowired
    private SprintRepository repository;

    public SprintController(SprintRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/sprints")
    Collection<Sprint> sprints() {
        return repository.findAll();
    }

    @GetMapping("/getsprint/{id}")
    ResponseEntity<?> getSprint(@PathVariable String id) {
        Optional<Sprint> sprint = repository.findById(id);
        return sprint.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/newsprint", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Sprint> createSprint(@Valid @RequestBody Sprint sprint) throws URISyntaxException {
        log.info("Request to create sprint: {}", sprint);
        Sprint result = repository.save(sprint);
        return ResponseEntity.created(new URI("/sprint/" + result.getId())).body(result);
    }

    @PutMapping("/sprint")
    ResponseEntity<Sprint> updateSprint(@Valid @RequestBody Sprint sprint) {
        log.info("Request to update sprint: {}", sprint);
        Sprint result = repository.save(sprint);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/deletesprint/{id}")
    public ResponseEntity<?> deleteSprint(@PathVariable String id) {
        log.info("Request to delete sprint: {}", id);
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}