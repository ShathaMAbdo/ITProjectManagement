package se.BTH.services.sprintManag.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.BTH.services.sprintManag.models.SubTask;
import se.BTH.services.sprintManag.repositories.SubTaskRepository;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SubTaskController {
    private final Logger log = LoggerFactory.getLogger(SubTaskController.class);

    @Autowired
    private SubTaskRepository repository;

    public SubTaskController(SubTaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/subtasks")
    Collection<SubTask> subtasks() {
        return repository.findAll();
    }

    @GetMapping("/getsubtask/{id}")
    ResponseEntity<?> getSubTask(@PathVariable String id) {
        Optional<SubTask> subtask = repository.findById(id);
        return subtask.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/newsubtask", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<SubTask> createSubTask(@Valid @RequestBody SubTask subtask) throws URISyntaxException {
        log.info("Request to create subtask: {}", subtask);
        SubTask result = repository.save(subtask);
        return ResponseEntity.created(new URI("/api/subtask/" + result.getId())).body(result);
    }

    @PutMapping("/subtask")
    ResponseEntity<SubTask> updateTask(@Valid @RequestBody SubTask subtask) {
        log.info("Request to update subtask: {}", subtask);
        SubTask result = repository.save(subtask);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/deletesubtask/{id}")
    public ResponseEntity<?> deleteSubTask(@PathVariable String id) {
        log.info("Request to delete subtask: {}", id);
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}