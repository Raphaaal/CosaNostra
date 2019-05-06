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
	
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String userSignup(@ModelAttribute User user, ModelMap modelMap) throws ClassNotFoundException, SQLException {


        System.out.println("AZEZAE" + user);
		modelMap.put("user", user);
        System.out.println("ZZZZZ" + user);



        UserQuery.insertNewUser(user.getName(),user.getPassword(), user.getEmail());
		
		return "submissionResult";
    }


    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String userSignin(@ModelAttribute User user, ModelMap modelMap) throws ClassNotFoundException, SQLException {

        user = UserQuery.getUser(user.getEmail(),user.getPassword());

        modelMap.put("user", user);


        return "submissionResult";
    }
    


}
