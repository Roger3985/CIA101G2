package com.chihyun.servicerobot.controller;

import com.chihyun.servicerobot.entity.ServiceRobot;
import com.chihyun.servicerobot.model.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/backend/servicerobot")
public class RobotBackendController {

    @Autowired
    RobotService robotService;

    @GetMapping("/selectServiceRobot")
    public String showAllRobotInfo(ModelMap modelMap) {
        List<ServiceRobot> list = robotService.getAll();
        modelMap.addAttribute("robotList", list);
        return "backend/servicerobot/selectServiceRobot";
    }

    @GetMapping("/addServiceRobot")
    public String addRobotReply(ModelMap modelMap) {
        modelMap.addAttribute("serviceRobot", new ServiceRobot());
        return "backend/servicerobot/addServiceRobot";
    }

    @PostMapping("insert")
    public String insertRobotReply(@Valid ServiceRobot serviceRobot, BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            List<String> errorMessage = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()){
                errorMessage.add(error.getDefaultMessage());
                modelMap.addAttribute("errors", errorMessage);
                return "backend/servicerobot/addServiceRobot";
            }
        }
        robotService.addRobotReply(serviceRobot);
        List<ServiceRobot> list = robotService.getAll();
        modelMap.addAttribute("robotList", list);
        return "redirect:/backend/servicerobot/selectServiceRobot";
    }

    @PostMapping("/updateRobot")
    public String getOneForUpdate(@RequestParam Integer keywordNo, ModelMap modelMap) {
        ServiceRobot serviceRobot = robotService.getOneReply(keywordNo);
        modelMap.addAttribute("serviceRobot", serviceRobot);
        return "backend/servicerobot/updateServiceRobot";
    }

    @PostMapping("/update")
    public String update(@Valid ServiceRobot serviceRobot, BindingResult result, ModelMap modelMap) {
        if (result.hasErrors()) {
            List<String> errorMessage = new ArrayList<>();
            for(FieldError error : result.getFieldErrors()){
                errorMessage.add(error.getDefaultMessage());
                modelMap.addAttribute("errors", errorMessage);
                return "backend/servicerobot/updateServiceRobot";
            }
        }
        robotService.updateRobotReply(serviceRobot);
        List<ServiceRobot> list = robotService.getAll();
        modelMap.addAttribute("robotList", list);
        return "redirect:/backend/servicerobot/selectServiceRobot";
    }

}
