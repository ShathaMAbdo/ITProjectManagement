package se.BTH.services.sprintManag.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.BTH.services.sprintManag.models.Team;
import se.BTH.services.sprintManag.repositories.TeamRepository;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class TeamController {
    private final Logger log = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private TeamRepository repository;

    public TeamController(TeamRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/teams")
    Collection<Team> teams() {
        return repository.findAll();
    }

    @GetMapping("/getteam/{id}")
    ResponseEntity<?> getTeam(@PathVariable String id) {
        Optional<Team> team = repository.findById(id);
        return team.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/newteam", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Team> createTeam(@Valid @RequestBody Team team) throws URISyntaxException {
        log.info("Request to create team: {}", team);
        Team result = repository.save(team);
        return ResponseEntity.created(new URI("/api/team/" + result.getId())).body(result);
    }

    @PutMapping("/team")
    ResponseEntity<Team> updateTeam(@Valid @RequestBody Team team) {
        log.info("Request to update team: {}", team);
        Team result = repository.save(team);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/deleteteam/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable String id) {
        log.info("Request to delete team: {}", id);
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}