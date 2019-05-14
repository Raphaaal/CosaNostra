package controllers.search;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
	private String[] types; // Filters for the search query
	public SearchQuery(String query,String ...types) {
		this.query=Objects.requireNonNull(query);
		this.types=Objects.requireNonNull(types);
	}

	/**
	 * This method returns the list of Results objects after performing a search on WikiData 
	 * and populating name, photo, page_id and type for each result.
	 * 
	 * @return the list of Results objects
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public List<Result> getResultsList() throws IOException, ParseException {
		// Search on WikiData and create results with type and page_id
		this.createSearchResultsList();
		// Populate each result with properties name and photo and instanceOf
		List<Result> resultsIdList = null;
		for(int i = 0 ; i < this.resultsList.size() ; i++) {
			resultsIdList = new ArrayList(this.resultsList.get(i).values());
			this.getPropertiesFromSearchResult(resultsIdList.get(0));
		}
		return createSpecificResultsList(types);
	}

	/**
	 * This method creates a list of Result objects from WikiData related to the title passed as a parameter.
	 * The list and its Result elements are created on the fly. The Result objects are populated with their page id and description.
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	private void createSearchResultsList() throws IOException, ParseException{
		// Elements to populate
		String page_id = "";
		List<String> ids = new ArrayList<>();

		// API request
		HttpTransport httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		JSONParser parser = new JSONParser();
		GenericUrl url = new GenericUrl("https://www.wikidata.org/w/api.php?");
		url.put("action","query");
		url.put("list","search");
		url.put("srsearch", this.query);
		url.put("format", "json");
		HttpRequest request = requestFactory.buildGetRequest(url);
		HttpResponse httpResponse = request.execute();

		// JSON parsing
		JSONObject response = (JSONObject) parser.parse(httpResponse.parseAsString());
		Object query =  response.get("query");
		JSONObject Jquery = (JSONObject) query;
		Object search =  Jquery.get("search");
		JSONArray Jsearch = (JSONArray) search;

		// Add page_id and description for each Result element
		for (int i =0; i < Jsearch.size(); ++i) {
			JSONObject jo = (JSONObject) Jsearch.get(i);
			Result result = new Result(null, null, null, null, (String) jo.get("title"), (String) jo.get("snippet"),null);
			Map<String,Result> map = new HashMap<>();
			map.put((String) jo.get("title"),result);
			resultsList.add(map);
		}
	}

	/**
	 * This method filters out the Results list according to types provided ("instance of")
	 * 
	 * @param types : the filters to use ("human", ...)
	 * @return the list of Result objects
	 */
	private List<Result> createSpecificResultsList(String ... types) {

		List<Result> resultsToSend = new ArrayList();

		for(Map<String, Result> map : resultsList) {
			for(Result r : map.values()) {
				for(String t : types) {
					if(resultsToSend != null) {
						if((r.getInstanceOf().contains(t) && !resultsToSend.contains(r))) {
							resultsToSend.add(r);
						}
					}
					else {
						if(r.getInstanceOf().contains(t))
							resultsToSend.add(r);
					}
				}
			}
		}
		return resultsToSend;
	}

	/**
	 * This method populates several basic properties of a Result object (full name and picture url). 
	 * 
	 * @param res : the Result object to populate. This object must contain a non null page_id field to query the appropriate WikiData page.
	 * @throws IOException
	 * @throws ParseException
	 */
	private void getPropertiesFromSearchResult(Result res) throws IOException, ParseException {
		// API request
		HttpTransport httpTransport = new NetHttpTransport();
		JSONParser parser = new JSONParser();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		GenericUrl baseUrl = new GenericUrl("https://www.wikidata.org/w/api.php?");
		baseUrl.put("action","wbgetentities");
		baseUrl.put("ids",res.getPageId());
		baseUrl.put("props","labels|descriptions|claims");
		baseUrl.put("languages","fr");
		baseUrl.put("format", "json");
		HttpRequest finalRequest = requestFactory.buildGetRequest(baseUrl);
		HttpResponse finalHttpResponse = finalRequest.execute();

		// JSON parsing
		JSONObject finalResponse = (JSONObject) parser.parse(finalHttpResponse.parseAsString());
		Object entities =  finalResponse.get("entities");
		JSONObject Jentities = (JSONObject) entities;
		JSONObject Jid = (JSONObject) Jentities.get(res.getPageId());
		Object claims = Jid.get("claims");
		JSONObject Jclaims = (JSONObject) claims;

		// Only if the resulting JSON from the API call contains any claims :
		if(Jclaims.size()!=0) {

			// Add photo url
			Object photo = Jclaims.getOrDefault("P18", "noPhoto");
			if (photo.toString() != "noPhoto") {
				JSONArray Jphoto = (JSONArray) photo;
				Object mainsnak = Jphoto.get(0);
				JSONObject Jmainsnak = (JSONObject) mainsnak;
				Object datavalue = Jmainsnak.get("mainsnak");
				JSONObject Jdatavalue = (JSONObject) datavalue;
				Object value = Jdatavalue.get("datavalue");
				JSONObject Jvalue = (JSONObject) value;
				Object photoName = Jvalue.get("value");
				String finalPhotoName = photoName.toString().replaceAll(" ", "_");
				res.setPhotoUrl("https://commons.wikimedia.org/wiki/Special:FilePath/"+finalPhotoName);
			}

			// Add the type ("instanceOf")
			Object instanceOf = Jclaims.getOrDefault("P31", "noInstanceOf");
			if (instanceOf.toString() != "noInstanceOf") {
				JSONArray Jinstance = (JSONArray) instanceOf;
				Object mainsnak = Jinstance.get(0);
				JSONObject Jmainsnak = (JSONObject) mainsnak;
				Object datavalue = Jmainsnak.get("mainsnak");
				JSONObject Jdatavalue = (JSONObject) datavalue;
				Object value = Jdatavalue.get("datavalue");
				JSONObject Jvalue = (JSONObject) value;
				Object value2 = Jvalue.get("value");
				JSONObject Jvalue2 = (JSONObject) value2;
				String instanceOfName = ResultQuery.getPageName((String) Jvalue2.get("id"));

				if(!Arrays.asList(types).contains(instanceOfName)) {
					if (instanceOfName.contains("disambiguation page")) {
						res.setInstanceOf("Search");
					}
					else {
						res.setInstanceOf("Autre");
					}
				}
				else {
					res.setInstanceOf(instanceOfName.toString());
				}
			}
			else {
				res.setInstanceOf("Autre");
			}

			// Add full name
			res.setName(ResultQuery.getPageName(res.getPageId()));
		}
		else {
			// If size == 0 this means this result is just a search result linking to multiple pages
			res.setInstanceOf("Search");
		}
	}

	public static void main(String[] args) throws IOException, ParseException {
		String query = "Zidane";
		String[] types = {"human"};
		SearchQuery sq = new SearchQuery(query, types);
		System.out.println("------ LISTE DES OBJETS FINAUX RENVOYES -----------");
		System.out.println(sq.getResultsList());
	}

}
