package se.BTH.ITProjectManagement.webmvc.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import se.BTH.ITProjectManagement.sprintManag.models.Sprint;
import se.BTH.ITProjectManagement.sprintManag.models.SubTask;
import se.BTH.ITProjectManagement.sprintManag.models.*;
import se.BTH.ITProjectManagement.sprintManag.repositories.SprintRepository;
import se.BTH.ITProjectManagement.sprintManag.repositories.SubTaskRepository;
import se.BTH.ITProjectManagement.sprintManag.repositories.TaskRepository;
import se.BTH.ITProjectManagement.sprintManag.repositories.UserRepository;
import se.BTH.ITProjectManagement.sprintManag.validators.SubTaskValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/api/subtask")
public class SubTaskController {

    private static Logger log = Logger.getLogger(SubTaskController.class);

    @Autowired
    private SubTaskRepository repository;
    @Autowired
    private TaskRepository taskRepo;
    @Autowired
    private SprintRepository sprintRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private SubTaskValidator subTaskValidator;

//    // Displaying the initial subtasks list.
//    @RequestMapping(value = "/subtasks", method = RequestMethod.GET)
//    public String getSubTasks(Model model) {
//        log.debug("Request to fetch all subtasks from the mongo database");
//        List<SubTask> subtask_list = repository.findAll();
//        model.addAttribute("subtasks", subtask_list);
//        return "subtask";
//    }

    // Opening the add new subtask form page.
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addSubTask(@RequestParam(value = "taskid", required = true) String taskid,
                             @RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        log.debug("Request to open the new subtask form page");
        model.addAttribute("subtaskAttr", SubTask.builder().OEstimate(0).users(new ArrayList<>()).build());
        model.addAttribute("taskid", taskid);
        model.addAttribute("sprintid", sprintid);
        model.addAttribute("taskname", taskRepo.findById(taskid).get().getName());
        model.addAttribute("sprintname", sprintRepo.findById(sprintid).get().getName());
        return "subtaskform";
    }

    // Opening the edit subtask form page.
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editSubTask(@RequestParam(value = "id", required = true) String id, @RequestParam(value = "taskid", required = true) String taskid,
                              @RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        log.debug("Request to open the edit subtask form page");
        model.addAttribute("subtaskAttr", repository.findById(id).get());
        model.addAttribute("taskid", taskid);
        model.addAttribute("sprintid", sprintid);
        model.addAttribute("taskname", taskRepo.findById(taskid).get().getName());
        model.addAttribute("sprintname", sprintRepo.findById(sprintid).get().getName());

        return "subtaskform";
    }

    // Opening the members of team to select member for assignment subtask form page.
    @RequestMapping(value = "/selectmember", method = RequestMethod.GET)
    public String selectmember(@RequestParam(value = "id", required = true) String id,
                               @RequestParam(value = "taskid", required = true) String taskid,
                               @RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        log.debug("Request to open the members of team for add to customed subtask form page");
        Team team = sprintRepo.findById(sprintid).get().getTeam();
        model.addAttribute("teamAttr", team);
        model.addAttribute("id", id);
        model.addAttribute("taskid", taskid);
        model.addAttribute("sprintid", sprintid);
        return "subtaskteamform";
    }

    // Deleting the specified member from assignment of subtask.
    @RequestMapping(value = "/addmember", method = RequestMethod.GET)
    public String addmember(@RequestParam(value = "userid", required = true) String userid,
                            @RequestParam(value = "id", required = true) String subtaskid,
                            @RequestParam(value = "taskid", required = true) String taskid,
                            @RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        Sprint sprint = sprintRepo.findById(sprintid).get();
        Task task = taskRepo.findById(taskid).get();
        SubTask subTask = repository.findById(subtaskid).get();
        List<User> users = subTask.getUsers();
        if (users==null)
            users=new ArrayList<>();

        if (!users.contains(userRepo.findById(userid).get())) {
            users.add(userRepo.findById(userid).get());
            subTask.setUsers(users);
            repository.save(subTask);
            int taskindex = sprint.findTaskIndex(taskid);
            int subtaskindex = sprint.getTasks().get(taskindex).findSubTaskIndex(subtaskid);
            task.getSubTasks().remove(subtaskindex);
            task.getSubTasks().add(subtaskindex, subTask);
            taskRepo.save(task);
            sprint.getTasks().remove(taskindex);
            sprint.getTasks().add(taskindex, task);
            sprintRepo.save(sprint);
        }

        return "redirect:/api/subtask/edit?id=" + subtaskid + "&taskid=" + taskid + "&sprintid=" + sprintid;
    }

    // Deleting the specified member from assignment of subtask.
    @RequestMapping(value = "/deletemember", method = RequestMethod.GET)
    public String deletemember(@RequestParam(value = "userid", required = true) String userid,
                               @RequestParam(value = "id", required = true) String subtaskid,
                               @RequestParam(value = "taskid", required = true) String taskid,
                               @RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        // repository.deleteById(id);
        Sprint sprint = sprintRepo.findById(sprintid).get();
        Task task = taskRepo.findById(taskid).get();
        SubTask subTask = repository.findById(subtaskid).get();
        List<User> users = subTask.getUsers();
        int index = IntStream.range(0, users.size()).filter(i -> users.get(i).getId().equals(userid)).findFirst().getAsInt();
        users.remove(index);
        subTask.setUsers(users);
        repository.save(subTask);
        int taskindex = sprint.findTaskIndex(taskid);
        int subtaskindex = sprint.getTasks().get(taskindex).findSubTaskIndex(subtaskid);
        task.getSubTasks().remove(subtaskindex);
        task.getSubTasks().add(subtaskindex, subTask);
        taskRepo.save(task);
        sprint.getTasks().remove(taskindex);
        sprint.getTasks().add(taskindex, task);
        sprintRepo.save(sprint);
        return "redirect:/api/subtask/edit?id=" + subtaskid + "&taskid=" + taskid + "&sprintid=" + sprintid;
    }

    // Deleting the specified subtask.
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value = "id", required = true) String id,
                         @RequestParam(value = "taskid", required = true) String taskid,
                         @RequestParam(value = "sprintid", required = true) String sprintid, Model model) {
        Sprint sprint = sprintRepo.findById(sprintid).get();
        List<Task> tasks = sprint.getTasks();
        Task task = taskRepo.findById(taskid).get();
        int taskindex = sprint.findTaskIndex(taskid);
        int subtaskindex = task.findSubTaskIndex(id);
        List<SubTask> subTasks = task.getSubTasks();
        subTasks.remove(subtaskindex);
        repository.deleteById(id);
        task.setSubTasks(subTasks);
        taskRepo.save(task);
        tasks.remove(taskindex);
        tasks.add(taskindex, task);
        sprint.setTasks(tasks);
        sprintRepo.save(sprint);
        return "redirect:/api/task/edit?taskid=" + taskid + "&sprintid=" + sprintid;
    }

    // Adding a new subtask or updating an existing subtask.
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@ModelAttribute("subtaskAttr")@Valid  SubTask subtask,
                       BindingResult bindingResult,Model model,
                       @RequestParam(value = "taskid", required = true) String taskid,
                       @RequestParam(value = "sprintid", required = true) String sprintid) {
       // subTaskValidator.validate(subtask, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("taskid", taskid);
            model.addAttribute("sprintid", sprintid);
            return "subtaskform";
        }
        Sprint sprint = sprintRepo.findById(sprintid).get();
        int taskIndex = sprint.findTaskIndex(taskid);
        Task task = sprint.getTasks().get(taskIndex);
        if (!subtask.getId().equals("")) {
            repository.save(subtask);
            int index = task.findSubTaskIndex(subtask.getId());
            task.getSubTasks().remove(index);
            task.getSubTasks().add(index, subtask);
        } else {
            List<Integer> actualHours = SubTask.intiActualHoursList(sprint.getPlannedPeriod());
            Map<String,List<Integer>> userActualHours=new HashMap<>();
            for (User u:subtask.getUsers()) {
                userActualHours.put(u.getUsername(),actualHours);
            }
            SubTask subtask1 = SubTask.builder().name(subtask.getName()).userActualHours(userActualHours).OEstimate(subtask.getOEstimate())
                    .status(TaskStatus.PLANNED).users(subtask.getUsers()).build();
            repository.save(subtask1);
            task.getSubTasks().add(subtask1);
        }
        taskRepo.save(task);
        sprint.getTasks().remove(taskIndex);
        sprint.getTasks().add(taskIndex, task);
        sprintRepo.save(sprint);
        return "redirect:/api/task/edit?taskid=" + taskid + "&sprintid=" + sprintid;

    }
}
