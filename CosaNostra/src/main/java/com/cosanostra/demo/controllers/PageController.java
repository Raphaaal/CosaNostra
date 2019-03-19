package com.cosanostra.demo.controllers;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import controllers.search.SearchQuery;


@Controller
public class PageController {
	
	@GetMapping("/")
	public String index() {
		return "index.html";
	}
	
	@GetMapping("/search")
	public String search(HttpServletRequest request, ModelMap modelMap) {
		if(request.getParameter("search").equals("")) {
			return "index.html";
		}
		String search = request.getParameter("search");
		
		System.out.println("Recherche :" + search);
		
		SearchQuery sq = new SearchQuery(search, "10");
		
		JSONObject kgResult = sq.kgSearch();
		
		System.out.println(kgResult);
		
		modelMap.put("search", kgResult);
		return "search.html";
	}
	
}
