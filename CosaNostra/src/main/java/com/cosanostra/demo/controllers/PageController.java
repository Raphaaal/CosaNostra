package com.cosanostra.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class PageController {
	
	@GetMapping("/")
	public String index() {
		return "index.html";
	}
	
	@RequestMapping("/search")
	public String search(@RequestParam("search") String search, ModelMap modelMap) {
		modelMap.put("search", search);
		return "search.html";
	}
	
}
