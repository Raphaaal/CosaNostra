package com.cosanostra.demo.controllers;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jayway.jsonpath.JsonPath;

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
		
		SearchQuery sq = new SearchQuery(search, "1");
		
		JSONObject kgResult = sq.kgSearch();
		
		JSONArray elements = (JSONArray) kgResult.get("itemListElement");
	    
	      for (Object element : elements) {
	    	try {  
		        modelMap.put("name", JsonPath.read(element, "$.result.name").toString());
		        System.out.println(JsonPath.read(element, "$.result.@type").toString());
		        System.out.println(JsonPath.read(element, "$.result.image.url").toString());
		        modelMap.put("image_url", JsonPath.read(element, "$.result.image.url").toString());
		        System.out.println(JsonPath.read(element, "$.result.detailedDescription.url").toString());
		        modelMap.put("description", JsonPath.read(element, "$.result.description").toString());
		        System.out.println(JsonPath.read(element, "$.result.detailedDescription.articleBody").toString());
		    	}catch(Exception e) {
		    		System.out.println(" ");
		    	}
	    	}
		return "search.html";
	}
	
}
