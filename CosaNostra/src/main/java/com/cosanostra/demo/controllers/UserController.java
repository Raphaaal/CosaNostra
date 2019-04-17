package com.cosanostra.demo.controllers;

import java.sql.SQLException;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import models.User;
import queries.UserQuery;

@Controller
public class UserController {
	/*
    @GetMapping("/user")
    public String userForm(Model model) {
        model.addAttribute("user", new User());
        return "user";
    }
    */
	
    @RequestMapping(value = "/submissionResult", method = RequestMethod.POST)
    public String userSubmit(@ModelAttribute User user, ModelMap modelMap) throws ClassNotFoundException, SQLException {
		modelMap.put("user", user);
		
		UserQuery.insertNewUser(user.getLogin(),user.getPassword(), user.getEmail());
		
		return "submissionResult";
    }
    


}
