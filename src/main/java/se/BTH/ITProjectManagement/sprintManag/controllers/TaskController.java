package se.BTH.ITProjectManagement.sprintManag.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.BTH.ITProjectManagement.sprintManag.models.Task;
import se.BTH.ITProjectManagement.sprintManag.repositories.TaskRepository;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final Logger log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/tasks")
    Collection<Task> tasks() {
        return repository.findAll();
    }

    @GetMapping("/task/{id}")
    ResponseEntity<?> getTask(@PathVariable String id) {
        Optional<Task> task = repository.findById(id);
        return task.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Task> createTask(@Valid @RequestBody Task task) throws URISyntaxException {
        log.info("Request to create task: {}", task);
        Task result = repository.save(task);
        return ResponseEntity.created(new URI("/api/task/" + result.getId())).body(result);
    }

    @PutMapping("/task")
    ResponseEntity<Task> updateTask(@Valid @RequestBody Task task) {
        log.info("Request to update task: {}", task);
        Task result = repository.save(task);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        log.info("Request to delete task: {}", id);
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}