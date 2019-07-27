package se.BTH.ITProjectManagement.webmvc.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import se.BTH.ITProjectManagement.sprintManag.models.Sprint;
import se.BTH.ITProjectManagement.sprintManag.models.Team;
import se.BTH.ITProjectManagement.sprintManag.repositories.SprintRepository;
import se.BTH.ITProjectManagement.sprintManag.repositories.TeamRepository;
import se.BTH.ITProjectManagement.sprintManag.services.SprintService;
import se.BTH.ITProjectManagement.sprintManag.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/sprint")
public class SprintController {
    private static final Logger log = LoggerFactory.getLogger(SprintController.class);

    @Autowired
    private SprintRepository repository;
    @Autowired
    private SprintService sprintService;

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserService userService;

    public SprintController(SprintRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/sprints", method = RequestMethod.GET)
    public String getsprints(Model model, Principal user) {
        log.info("Request to fetch all sprints from the mongo database");
        Boolean isAdmin = userService.isAdmin(user.getName());

        List<Sprint> sprint_list;
        if (isAdmin)
            sprint_list = repository.findAll();
        else
            sprint_list = repository.findAll().stream().filter(s -> s.isUserInSprint(user)).collect(Collectors.toList());
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("sprints", sprint_list);
        return "sprint";
    }

    // Opening the add new sprint form page.
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addSprint(@Param(value = "name") String name, Model model) {
        log.info("Request to open the new sprint form page");
        Sprint sprint = Sprint.builder().name(name).build();
        // repository.save(sprint);
        model.addAttribute("sprintAttr", sprint);
        return "sprintform";
    }

    // Opening the edit sprint form page.
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editSprint(@RequestParam(value = "sprintid", required = true) String id, Model model,Principal user) {
        log.info("Request to open the edit Sprint form page");
        Boolean isAdmin=userService.isAdmin(user.getName());
        model.addAttribute("sprintAttr", repository.findById(id).get());
        model.addAttribute("isAdmin", isAdmin);
        return "sprintform";
    }


    // view actualHours
    @RequestMapping(value = "/actualHours", method = RequestMethod.GET)
    public String editActualHours(@RequestParam(value = "sprintid", required = true) String id, Model model) {
        log.info("Request to open the edit actualHours form page");
        model.addAttribute("sprintAttr", repository.findById(id).get());
        return "actualHours";
    }

    // view actualHours
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public String printSprint(@RequestParam(value = "sprintid", required = true) String id, Model model) {
        log.info("Request to open the print sprint form page");
        model.addAttribute("sprintAttr", repository.findById(id).get());
        return "printList";
    }

    // Deleting the specified sprint.
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", required = true) String id, Model model) {
        repository.deleteById(id);
        return "redirect:sprints";
    }

    // Adding a new sprint or updating an existing sprint.
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("sprintAttr") Sprint sprint) {
        Sprint sprint1;
        if (!sprint.getId().equals("")) {
            sprint.calcDelivery();
            sprint.setTeam(repository.findById(sprint.getId()).get().getTeam());
            repository.save(sprint);
        } else {
            if (sprint.getTeam() == null) {
                sprint1 = Sprint.builder().name(sprint.getName()).daily_meeting(sprint.getDaily_meeting()).
                        start(sprint.getStart()).demo(sprint.getDemo()).goal(sprint.getGoal()).plannedPeriod(sprint.getPlannedPeriod())
                        .retrospective(sprint.getRetrospective()).review(sprint.getReview()).tasks(sprint.getTasks()).build();
            } else {
                sprint1 = Sprint.builder().name(sprint.getName()).daily_meeting(sprint.getDaily_meeting()).
                        start(sprint.getStart()).demo(sprint.getDemo()).goal(sprint.getGoal()).plannedPeriod(sprint.getPlannedPeriod())
                        .retrospective(sprint.getRetrospective()).review(sprint.getReview()).team(sprint.getTeam()).tasks(sprint.getTasks()).build();
            }

            sprint1.calcDelivery();
            repository.save(sprint1);
        }
        return "redirect:sprints";
    }


    //Select one team from teams
    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    public String viewTeamsToSelect(@RequestParam(value = "id", required = true) String id, Model model) {
        log.info("Request to fetch all teams from the db for custom team and select team");
        List<Team> teams = teamRepository.findAll();
        teams.removeIf(team -> !team.isActive());
        model.addAttribute("teams", teams);
        model.addAttribute("sprintid", id);
        return "sprintteam";
    }

    @RequestMapping(value = "/addteam", method = RequestMethod.GET)
    public String addTeamToSprint(@RequestParam(value = "sprintid", required = true) String sprintid, @RequestParam(value = "teamid", required = true) String teamid, Model model) {
        Sprint sprint = repository.findById(sprintid).get();
        sprint.setTeam(teamRepository.findById(teamid).get());
        repository.save(sprint);
        return "redirect:/api/sprint/edit?sprintid=" + sprintid;
    }

    @RequestMapping(value = "/sprintcharts", method = RequestMethod.GET)
    public String sprintcharts(@RequestParam(value = "sprintid", required = true) String sprintid, ModelMap modelMap) {
        modelMap.addAttribute("sprintid", sprintid);
        return "sprintcharts";
    }

    @RequestMapping(value = "/canvasjschart", method = RequestMethod.GET)
    public String canvasjschart(@RequestParam(value = "sprintid", required = true) String sprintid, ModelMap modelMap) {
        List<List<Map<Object, Object>>> canvasjsDataList = sprintService.getCanvasjsDataList(sprintid);
        modelMap.addAttribute("dataPointsList", canvasjsDataList);
        modelMap.addAttribute("sprintname", repository.findById(sprintid).get().getName());
        modelMap.addAttribute("teamname", repository.findById(sprintid).get().getTeam().getName());

        return "actualdonedaily";
    }

    @RequestMapping(value = "/canvasjschart1", method = RequestMethod.GET)
    public String canvasjschart1(@RequestParam(value = "sprintid", required = true) String sprintid, ModelMap modelMap) {
        List<List<Map<Object, Object>>> canvasjsDataList = sprintService.getCanvasjsDataList1(sprintid);
        modelMap.addAttribute("dataPointsList", canvasjsDataList);
        modelMap.addAttribute("sprintname", repository.findById(sprintid).get().getName());
        modelMap.addAttribute("teamname", repository.findById(sprintid).get().getTeam().getName());
        return "actualremaindaily";
    }
    @RequestMapping(value = "/canvasjschart2", method = RequestMethod.GET)
    public String canvasjschart2(@RequestParam(value = "sprintid", required = true) String sprintid, ModelMap modelMap) {
        List<List<Map<Object, Object>>> canvasjsDataList = sprintService.getCanvasjsDataList2(sprintid);
        modelMap.addAttribute("dataPointsList", canvasjsDataList);
        modelMap.addAttribute("sprintname", repository.findById(sprintid).get().getName());
        modelMap.addAttribute("teamname", repository.findById(sprintid).get().getTeam().getName());
        return "developerPerformance";
    }
}
