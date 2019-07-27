package se.BTH.ITProjectManagement.webmvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import se.BTH.ITProjectManagement.sprintManag.models.*;

import se.BTH.ITProjectManagement.sprintManag.repositories.SprintRepository;
import se.BTH.ITProjectManagement.sprintManag.repositories.SubTaskRepository;
import se.BTH.ITProjectManagement.sprintManag.repositories.TaskRepository;
import se.BTH.ITProjectManagement.sprintManag.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/task")
public class TaskController {

    private static Logger log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskRepository repository;
    @Autowired
    private SprintRepository sprintRepo;
    @Autowired
    private SubTaskRepository subTaskRepo;
    @Autowired
    private UserService userService;

    // Displaying the tasks list for custom sprint .
    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public String getTasks(@RequestParam(value = "sprintid", required = true) String sprintid, Model model, Principal user) {
        log.info("Request to fetch all tasks for custom sprint from the mongo database");
        Sprint sprint = sprintRepo.findById(sprintid).get();
        List<Task> task_list = sprint.getTasks();
        Boolean isAdmin=userService.isAdmin(user.getName());
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("tasks", task_list);
        model.addAttribute("sprintid", sprintid);
        model.addAttribute("sprintname", sprintRepo.findById(sprintid).get().getName());
        return "task";
    }

    // Opening the add new task form page.
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addTask(@RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        log.info("Request to open the new task form page");
        model.addAttribute("taskAttr", Task.builder().storyPoints(0).build());
        model.addAttribute("sprintid", sprintid);
        model.addAttribute("sprintname", sprintRepo.findById(sprintid).get().getName());
        return "taskform";
    }

    // Opening the edit task form page.
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editTask(@RequestParam(value = "taskid", required = true) String id, @RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        log.info("Request to open the edit task form page");
        model.addAttribute("taskAttr", repository.findById(id).get());
        model.addAttribute("sprintid", sprintid);
        model.addAttribute("sprintname", sprintRepo.findById(sprintid).get().getName());
        return "taskform";
    }

    // Deleting the specified task.
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "taskid", required = true) String id, @RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        Sprint sprint = sprintRepo.findById(sprintid).get();
        List<Task> tasks = sprint.getTasks();
        Task task = repository.findById(id).get();
        tasks.remove(sprint.findTaskIndex(id));
        sprint.setTasks(tasks);
        sprintRepo.save(sprint);
        for (SubTask temp : task.getSubTasks()) {
            subTaskRepo.delete(temp);
        }
        repository.deleteById(id);
        return "redirect:/api/task/tasks?sprintid=" + sprintid;
    }

    // Adding a new task or updating an existing task.
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("taskAttr") Task task, @RequestParam(value = "sprintid", required = true) String sprintid) {
        if (task.getId() != "") {
            task.setSubTasks(repository.findById(task.getId()).get().getSubTasks());
            repository.save(task);
            Sprint sprint = sprintRepo.findById(sprintid).get();
            List<Task> tasks = sprint.getTasks();
            int index=sprint.findTaskIndex(task.getId());
            tasks.remove(index);
            tasks.add(index, task);
            sprint.setTasks(tasks);
            sprintRepo.save(sprint);
        }  else {
            Task task1 = Task.builder().name(task.getName()).storyPoints(task.getStoryPoints()).priority(task.getPriority())
                    .subTasks(new ArrayList<>()).build();
            repository.save(task1);
            Sprint sprint = sprintRepo.findById(sprintid).get();
            List<Task> tasks = sprint.getTasks();
            tasks.add(task1);
            sprint.setTasks(tasks);
            sprintRepo.save(sprint);
        }

        return "redirect:/api/task/tasks?sprintid=" + sprintid;
    }
}
