package controllers.search;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;


public class SearchQuery {
	
	private List<Map<String, Result>> resultsList= new ArrayList();

	
	public static Properties properties = new Properties();
	private String query;
	private String nbResult;
	public SearchQuery(String query,String nbResult) {
		this.query=Objects.requireNonNull(query);
		this.nbResult=Objects.requireNonNull(nbResult);
	}
	
	public JSONObject search(String query) {
		return null;
	}
	
	public void getIds(String title) throws IOException, ParseException{
		  String page_id="";
	      HttpTransport httpTransport = new NetHttpTransport();
	      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
	      JSONParser parser = new JSONParser();
	      GenericUrl url = new GenericUrl("https://www.wikidata.org/w/api.php?");
	      //url.put("action","wbgetentities");
	      url.put("action","query");
	      url.put("list","search");
	     // url.put("sites", "enwiki");
	     //url.put("titles", title);
	      url.put("srsearch", title);
	      url.put("format", "json");
	      System.out.println(url);
	      HttpRequest request = requestFactory.buildGetRequest(url);
	      HttpResponse httpResponse = request.execute();
	      JSONObject response = (JSONObject) parser.parse(httpResponse.parseAsString());
	      List<String> ids=new ArrayList<>();
	     // Iterator it = response.keySet().iterator();
	      Object query =  response.get("query");
	      JSONObject Jquery = (JSONObject) query;
	      Object search =  Jquery.get("search");
	      JSONArray Jsearch = (JSONArray) search;
	      System.out.println("====================================");
	      
	      for (int i =0; i < Jsearch.size(); ++i) {
			     JSONObject jo = (JSONObject) Jsearch.get(i);
			     Result result = new Result(null, null, null, null, (String) jo.get("title"), (String) jo.get("snippet"));
			     Map<String,Result> map = new HashMap<>();
			     map.put((String) jo.get("title"),result);
			     resultsList.add(map);
		      }
	      
	     
	
	     //System.out.println(Jentities.keySet());
	      
	       //System.out.println(response);
	      //while(it.hasNext()) {
	    	 // System.out.println(it.next());
		     //JSONObject values = (JSONObject) response.get(it.next());
		     //for(Object id : response.get(it.next()).keySet() )
		    	// ids.add((String) id);
	     // }
	}
	
	
	public void wikiSearch(Result res) throws IOException, ParseException {
		
	      HttpTransport httpTransport = new NetHttpTransport();
	      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
	      JSONParser parser = new JSONParser();
	      
	      /*
	      GenericUrl url = new GenericUrl("https://www.wikidata.org/w/api.php?");
	      url.put("action","wbgetentities");
	      url.put("sites", "enwiki");
	      url.put("titles", title);
	      url.put("format", "json");
	      //System.out.println(url);
	      HttpRequest request = requestFactory.buildGetRequest(url);
	      HttpResponse httpResponse = request.execute();
	      JSONObject response = (JSONObject) parser.parse(httpResponse.parseAsString());
	      Iterator it = response.keySet().iterator();
	       //System.out.println(response);
	      if(it.hasNext()) {
		     JSONObject values = (JSONObject) response.get(it.next());
		     for(Object id : values.keySet())
		    	 page_id=(String) id;
	      }
	     System.out.println(page_id);
	     */
	     
	      GenericUrl baseUrl = new GenericUrl("https://www.wikidata.org/w/api.php?");
	      baseUrl.put("action","wbgetentities");
	      baseUrl.put("ids",res.getPageId());
	      baseUrl.put("props","labels|descriptions|claims");
	      baseUrl.put("languages","fr");
	      baseUrl.put("format", "json");
	      HttpRequest finalRequest = requestFactory.buildGetRequest(baseUrl);
	      HttpResponse finalHttpResponse = finalRequest.execute();
	      JSONObject finalResponse = (JSONObject) parser.parse(finalHttpResponse.parseAsString());
	      
	      // PHOTO (reste à creuser un peu plus dans le JSON)
	      Object entities =  finalResponse.get("entities");
	      JSONObject Jentities = (JSONObject) entities;
	      JSONObject Jid = (JSONObject) Jentities.get(res.getPageId());
	      Object claims = Jid.get("claims");
	      JSONObject Jclaims = (JSONObject) claims;
	      Object photo = Jclaims.getOrDefault("P18", "wallou");
	      res.setPhotoUrl(photo.toString());
	}
	
	/*
	
  public JSONObject kgSearch () {
	  JSONObject response=null;
    try {
      properties.load(new FileInputStream("src/main/resources/kgsearch.properties"));

      HttpTransport httpTransport = new NetHttpTransport();
      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
      JSONParser parser = new JSONParser();
      GenericUrl url = new GenericUrl("https://kgsearch.googleapis.com/v1/entities:search");
      url.put("query", this.query);
      url.put("limit", this.nbResult);
      url.put("indent", "true");
      url.put("key", properties.get("API_KEY"));
      HttpRequest request = requestFactory.buildGetRequest(url);
      HttpResponse httpResponse = request.execute();
      response = (JSONObject) parser.parse(httpResponse.parseAsString());
      
     
    } catch (Exception ex) {
      ex.printStackTrace();
    }
   
    return response;
  }
  
public List<String> kgGetDescriptions(JSONObject searchResult, int index) {
	  LoggerContext logContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	  ch.qos.logback.classic.Logger log = logContext.getLogger("com.jayway.jsonpath.internal.path.CompiledPath");
	  log.setLevel(Level.INFO);
	  List<String> descs = new LinkedList<>();
      JSONArray elements = (JSONArray) searchResult.get("itemListElement");
      for (Object element : elements) {
    	try {  
        String desc = (JsonPath.read(element, "$.result.detailedDescription.articleBody").toString());
        descs.add(desc);
    	}catch(Exception e) {
    	}
    	}
	  return descs;
  }--------------


  
public List<String> kgGetWikiUrl(JSONObject searchResult, int index) {
	  LoggerContext logContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	  ch.qos.logback.classic.Logger log = logContext.getLogger("com.jayway.jsonpath.internal.path.CompiledPath");
	  log.setLevel(Level.INFO);
	  List<String> urls = new LinkedList<>();
    JSONArray elements = (JSONArray) searchResult.get("itemListElement");
    for (Object element : elements) {
	  	try {  
	      String url = "null";
	      url=(JsonPath.read(element, "$.result.url"));
	      urls.add(url);
	  	}catch(Exception e) {
	  	}
    }
	  return urls;
}
  
  public List<String> kgGetNames(JSONObject searchResult, int index) {
	  LoggerContext logContext = (LoggerContext) LoggerFactory.getILoggerFactory();
	  ch.qos.logback.classic.Logger log = logContext.getLogger("com.jayway.jsonpath.internal.path.CompiledPath");
	  log.setLevel(Level.INFO);
	  List<String> names = new LinkedList<>();
      JSONArray elements = (JSONArray) searchResult.get("itemListElement");
      for (Object element : elements) {
    	try {  
        String name = (JsonPath.read(element, "$.result.name"));
        
        names.add(name);
    	}catch(Exception e ) {
    	}
    	}
	  return names;
  }
  
  public int getLimit() {
	  return Integer.parseInt(this.nbResult);
  }
  */
  
  /*
   * System.out.println(JsonPath.read(element, "$.result.@type").toString());
        System.out.println(JsonPath.read(element, "$.result.image.url").toString());
        System.out.println(JsonPath.read(element, "$.result.detailedDescription.url").toString());
        System.out.println(JsonPath.read(element, "$.result.description").toString());
        System.out.println(JsonPath.read(element, "$.result.detailedDescription.articleBody").toString());
   */
  
  
  public static void main(String[] args) throws IOException, ParseException {
	
	  String query = "macron";
	  SearchQuery sq=new SearchQuery(query,"10");
	  
	  /*
	  List description = sq.kgGetDescriptions(sq.kgSearch(), 10);
	  List<String> names=sq.kgGetNames(sq.kgSearch(), 10);
	  */
	  
	  sq.getIds(query);
      System.out.println(sq.resultsList);
	  System.out.println("-------------------");
	  
	  /*
	  Set <String> results = new HashSet<>();
	  for(int i=0;i<sq.resultsList.size();i++) {
		   results.addAll(sq.resultsList.get(i).keySet());
		  System.out.println(results);
	  }
	 List<String> resultsIdList = new ArrayList(results);
	 System.out.println("-------------------");
	   
	   */
	  List<Result> resultsIdList=null;
	  for(int i=0;i<sq.resultsList.size();i++) {
		  resultsIdList = new ArrayList(sq.resultsList.get(i).values());
		  sq.wikiSearch(resultsIdList.get(0));
		  //System.out.println(sq.resultsList.get(i).values());
	}
	  
	  System.out.println("-------------------");

	
	System.out.println(sq.resultsList);
	
	
	
	  
	  
	  
	  /*
	  System.out.println("-----------------------------------------------------------------------");
	 System.out.println(sq.wikiSearch("Jackson"));  
	  List urls=sq.kgGetWikiUrl(sq.kgSearch(), 10);
	  Map<String,List<String>> map =new HashMap<>();
	  List<String> values=new ArrayList<>();
	  for(int i=0;i<2;i++) {
		  if(i<urls.size()) {
			 values.add((String)urls.get(i));
		  }else {
			  values.add("unknown");
		  }
		  if(i<description.size())
			  values.add((String)description.get(i));
		  else {
			  values.add("unknown");
		  }
		  map.put((String) names.get(i), values);
			  
	  }
		  
	// System.out.println(map);
*/

	
}

  
}
