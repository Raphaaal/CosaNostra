package controllers.search;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
	private String query;
	
	public SearchQuery(String query) {
		this.query=Objects.requireNonNull(query);
	}

	/**
	 * This method returns the list of Results objects after performing a search on WikiData 
	 * and populating name, photo, page_id and type for each result.
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public List<Result> getResultsList() throws IOException, ParseException {
		// Search on WikiData and create results with type and page_id
		this.createSearchResultsList();
		
		// Populate each result with properties name and photo
		List<Result> resultsIdList=null;
		for(int i=0;i<this.resultsList.size();i++) {
			resultsIdList = new ArrayList(this.resultsList.get(i).values());
			this.getPropertiesFromSearchResult(resultsIdList.get(0));
		}
		
		// Return the list of results populated
		List<Result> resultsToSend = new ArrayList();
		for(Map<String, Result> map : resultsList) {
			for(Result r : map.values())
				resultsToSend.add(r);
		}
		return resultsToSend;
	}

	
	/**
	 * This method creates a list of Result objects from WikiData related to the title passed as a parameter.
	 * The list and its Result elements are created on the fly. The Result objects are populated with their page id and type (description).
	 * @param title
	 * @throws IOException
	 * @throws ParseException
	 */
	private void createSearchResultsList() throws IOException, ParseException{
		String page_id="";
		HttpTransport httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		JSONParser parser = new JSONParser();
		GenericUrl url = new GenericUrl("https://www.wikidata.org/w/api.php?");
		url.put("action","query");
		url.put("list","search");
		url.put("srsearch", this.query);
		url.put("format", "json");
		// REQUETE WIKIDATA CORRESPONDANT AU SEARCH
		//System.out.println(url);
		HttpRequest request = requestFactory.buildGetRequest(url);
		HttpResponse httpResponse = request.execute();
		JSONObject response = (JSONObject) parser.parse(httpResponse.parseAsString());
		
		List<String> ids=new ArrayList<>();
		Object query =  response.get("query");
		JSONObject Jquery = (JSONObject) query;
		Object search =  Jquery.get("search");
		JSONArray Jsearch = (JSONArray) search;
		
		// AJOUT DES PAGE_ID ET TYPE DE CHAQUE RESULT
		for (int i =0; i < Jsearch.size(); ++i) {
			JSONObject jo = (JSONObject) Jsearch.get(i);
			Result result = new Result(null, null, null, null, (String) jo.get("title"), (String) jo.get("snippet"));
			Map<String,Result> map = new HashMap<>();
			map.put((String) jo.get("title"),result);
			resultsList.add(map);
		}
	}

	
	/**
	 * This method populates the full name and picture url of a given Result object. 
	 * This object must contain a non null page_id field (to query the appropriate WikiData page).
	 * @param res
	 * @throws IOException
	 * @throws ParseException
	 */
	private void getPropertiesFromSearchResult(Result res) throws IOException, ParseException {
		HttpTransport httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		JSONParser parser = new JSONParser();

		GenericUrl baseUrl = new GenericUrl("https://www.wikidata.org/w/api.php?");
		baseUrl.put("action","wbgetentities");
		baseUrl.put("ids",res.getPageId());
		baseUrl.put("props","labels|descriptions|claims");
		baseUrl.put("languages","fr");
		baseUrl.put("format", "json");
		HttpRequest finalRequest = requestFactory.buildGetRequest(baseUrl);
		HttpResponse finalHttpResponse = finalRequest.execute();
		JSONObject finalResponse = (JSONObject) parser.parse(finalHttpResponse.parseAsString());

		// AJOUT PHOTO DU RESULT
		Object entities =  finalResponse.get("entities");
		JSONObject Jentities = (JSONObject) entities;
		JSONObject Jid = (JSONObject) Jentities.get(res.getPageId());
		Object claims = Jid.get("claims");
		JSONObject Jclaims = (JSONObject) claims;
		Object photo = Jclaims.getOrDefault("P18", "wallou photo");
		if (photo.toString() != "wallou photo") {
			JSONArray Jphoto = (JSONArray) photo;
			Object mainsnak = Jphoto.get(0);
			JSONObject Jmainsnak = (JSONObject) mainsnak;
			Object datavalue = Jmainsnak.get("mainsnak");
			JSONObject Jdatavalue = (JSONObject) datavalue;
			Object value = Jdatavalue.get("datavalue");
			JSONObject Jvalue = (JSONObject) value;
			Object photoName = Jvalue.get("value");
			String finalPhotoName = photoName.toString().replaceAll(" ", "_");
			res.setPhotoUrl("https://commons.wikimedia.org/wiki/File:"+finalPhotoName);
		}
		
		// AJOUT NOM DU RESULT
		Object name = Jclaims.getOrDefault("P1559", "wallou nom");
		if (name.toString() != "wallou nom") {
			JSONArray Jname = (JSONArray) name;
			Object mainsnak = Jname.get(0);
			JSONObject Jmainsnak = (JSONObject) mainsnak;
			Object datavalue = Jmainsnak.get("mainsnak");
			JSONObject Jdatavalue = (JSONObject) datavalue;
			Object value = Jdatavalue.get("datavalue");
			JSONObject Jvalue = (JSONObject) value;
			Object namevalue = Jvalue.get("value");
			JSONObject Jnamevalue = (JSONObject) namevalue;
			Object nametext = Jnamevalue.get("text");
			res.setName(nametext.toString());
		}
		else {
			res.setName(query);
		}
	}
	

	public static void main(String[] args) throws IOException, ParseException {
		String query = "macron";
		SearchQuery sq = new SearchQuery(query);
		
		System.out.println("------ LISTE DES OBJETS FINAUX RENVOYES -----------");
		System.out.println(sq.getResultsList());


	}


}

/* OLD (Google Knowledge Graph)

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
  }


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