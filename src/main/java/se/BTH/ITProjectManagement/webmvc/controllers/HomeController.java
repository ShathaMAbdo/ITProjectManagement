package se.BTH.ITProjectManagement.webmvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import se.BTH.ITProjectManagement.sprintManag.models.User;

@Controller
public class HomeController {
    @RequestMapping("/api/myuser")
    public String index(){
        return"index";
    }

    @RequestMapping(value="api/save", method= RequestMethod.POST)///api/myuser
    public ModelAndView save(@ModelAttribute User user){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user-data");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}