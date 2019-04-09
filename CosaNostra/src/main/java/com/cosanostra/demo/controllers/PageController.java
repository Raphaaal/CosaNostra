package com.cosanostra.demo.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jayway.jsonpath.JsonPath;

import controllers.search.Result;
import controllers.search.SearchQuery;

@Controller
public class PageController {

	@GetMapping("/")
	public String index() {
		return "index.html";
	}

	@GetMapping("/search")
	public String search(HttpServletRequest request, ModelMap modelMap) throws IOException, ParseException {
		if (request.getParameter("search").equals("")) {
			return "index.html";
		}
		String search = request.getParameter("search");

		System.out.println("Recherche :" + search);

		SearchQuery sq = new SearchQuery(search);

		List<Result> wikiResult = sq.getResultsList();
		
		modelMap.put("results", wikiResult);
		
		return "search.html";
	}

	@GetMapping("/result/{pageId}")
	public String result(@PathVariable String pageId) {
		
		System.out.println(pageId);
		
		return "result.html";
	}
	
	
}
