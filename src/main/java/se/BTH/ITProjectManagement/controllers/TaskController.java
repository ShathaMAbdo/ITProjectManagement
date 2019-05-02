package se.BTH.ITProjectManagement.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import se.BTH.ITProjectManagement.models.Sprint;
import se.BTH.ITProjectManagement.models.SubTask;
import se.BTH.ITProjectManagement.models.Task;
import se.BTH.ITProjectManagement.repositories.SprintRepository;
import se.BTH.ITProjectManagement.repositories.SubTaskRepository;
import se.BTH.ITProjectManagement.repositories.TaskRepository;

import java.util.*;

@Controller
@RequestMapping("/api/task")
public class TaskController {

    private static org.apache.log4j.Logger log = Logger.getLogger(TaskController.class);

    @Autowired
    private TaskRepository repository;
    @Autowired
    private SprintRepository sprintRepo;
    @Autowired
    private SubTaskRepository subTaskRepo;

    // Displaying the tasks list for custom sprint .
    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public String getTasks(@RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        log.debug("Request to fetch all tasks for custom sprint from the mongo database");
        Sprint sprint = sprintRepo.findById(sprintid).get();
        List<Task> task_list = sprint.getTasks();
        model.addAttribute("tasks", task_list);
        model.addAttribute("sprintid", sprintid);
        return "task";
    }

    // Opening the add new task form page.
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addTask(@RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        log.debug("Request to open the new task form page");
        model.addAttribute("taskAttr", Task.builder().storyPoints(0).build());
        model.addAttribute("sprintid", sprintid);
        return "taskform";
    }

    // Opening the edit task form page.
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editTask(@RequestParam(value = "taskid", required = true) String id, @RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        log.debug("Request to open the edit task form page");
        model.addAttribute("taskAttr", repository.findById(id).get());
        model.addAttribute("sprintid", sprintid);
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
            tasks.remove(sprint.findTaskIndex(task.getId()));
            tasks.add(sprint.findTaskIndex(task.getId()), task);
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
