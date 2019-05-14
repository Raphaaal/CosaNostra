package com.cosanostra.demo.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;
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
    public String userSignup(@ModelAttribute User user, ModelMap modelMap, HttpSession session) throws ClassNotFoundException, SQLException {

		modelMap.put("user", user);

        UserQuery.insertNewUser(user.getName(),user.getPassword(), user.getEmail());
        User newUser =UserQuery.getUser(user.getEmail(), user.getPassword());

        session.setAttribute("user_name", newUser.getName());
        session.setAttribute("user_id", newUser.getId());

        System.out.println(newUser.getId());

        return "redirect:" + "/";
    }


    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public String userSignin(@ModelAttribute User user, ModelMap modelMap, HttpSession session) throws ClassNotFoundException, SQLException {


        user = UserQuery.getUser(user.getEmail(),user.getPassword());
        
        session.setAttribute("user_name", user.getName());
        session.setAttribute("user_id", user.getId());

        modelMap.put("user", user);

        return "redirect:" + "/";
    }
    


}
