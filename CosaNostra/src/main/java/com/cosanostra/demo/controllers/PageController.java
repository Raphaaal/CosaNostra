package com.cosanostra.demo.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import models.Favoris;
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

import controllers.search.FinalResult;
import controllers.search.Result;
import controllers.search.ResultQuery;
import controllers.search.SearchQuery;
import queries.FavorisQuery;

@Controller
public class PageController {

	@GetMapping("/")
	public String index(HttpSession session) {
		return "index.html";
	}

	@GetMapping("/signup")
	public String signup(HttpSession session) {
		return "signup.html";
	}

	@GetMapping("/signin")
	public String signin(HttpSession session) {


		if(session.getAttribute("user_name") != null){
			return "redirect:" + "/";
		}

		return "signin.html";
	}

	@GetMapping("/search")
	public String search(HttpServletRequest request, ModelMap modelMap) throws IOException, ParseException {
		if (request.getParameter("search").equals("")) {
			return "redirect:" + "/";
		}
		String search = request.getParameter("search");
		ArrayList<String> types = new ArrayList<>();
		
		if(request.getParameter("autre") != null && request.getParameter("autre").equals("on")) {
			types.add(new String("Autre"));
		}
		if(request.getParameter("human") != null && request.getParameter("human").equals("on")) {
			types.add(new String("human"));
			types.add(new String("Human"));

		}
		if(request.getParameter("painting") != null && request.getParameter("painting").equals("on")) {
			types.add(new String("painting"));
			types.add(new String("artistic type"));
		}
		if(request.getParameter("book") != null && request.getParameter("book").equals("on")) {
			types.add(new String("literary work"));
		}
		if(request.getParameter("film") != null && request.getParameter("film").equals("on")) {
			types.add(new String("film"));
			types.add(new String("television series"));
		}
		if(request.getParameter("music") != null && request.getParameter("music").equals("on")) {
			types.add(new String("music form"));
			types.add(new String("music term"));
			types.add(new String("song"));
			types.add(new String("album"));
			types.add(new String("single"));
		}

		
		String[] typesArray = types.toArray(new String[types.size()]);

		SearchQuery sq = new SearchQuery(search, typesArray);

		List<Result> wikiResult = sq.getResultsList();
		
		modelMap.put("results", wikiResult);
		
		return "search.html";
	}

	@GetMapping("/result/{pageId}")
	public String result(@PathVariable String pageId, ModelMap modelMap) throws IOException, ParseException {
		
		FinalResult result = ResultQuery.getFinalResult(pageId);
		
		modelMap.put("result", result);
		
		return "result.html";
	}


	@GetMapping("/favoris/{pageId}")
	public @ResponseBody
	void addFavoris(@PathVariable String pageId, HttpSession session) throws IOException, ParseException, SQLException, ClassNotFoundException {

		FavorisQuery.insertNewFavoris(session.getAttribute("user_id").toString(), pageId);

	}

	@GetMapping("/checkfav/{pageId}")
	public @ResponseBody
	Favoris checkFav(@PathVariable String pageId, ModelMap modelMap, HttpSession session) throws IOException, ParseException, SQLException, ClassNotFoundException {

		Favoris favoris = FavorisQuery.getFavoris(session.getAttribute("user_id").toString(), pageId);
		System.out.println("FAVVVV" + favoris);
		modelMap.put("favoris", favoris);
		return favoris;

	}
	
}
