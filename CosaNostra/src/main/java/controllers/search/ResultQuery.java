package controllers.search;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;

/**
 * This class is used to get all necessary information about a final result (chosen from the all the potential results to a query). 
 * These pieces of information are gathered thanks to the Wikipedia API, based on the final result name.
 *
 */
public class ResultQuery {

	public static Properties properties = new Properties();
	

	public static FinalResult getFinalResult(String pageId) throws IOException, ParseException {
		// Final result to be populated
		FinalResult fRes = new FinalResult(null, null, pageId, null, null, null, null, null, null,null); 

		// Wikipedia API request
		HttpTransport httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		JSONParser parser = new JSONParser();
		GenericUrl baseUrl = new GenericUrl("https://www.wikidata.org/w/api.php?");
		baseUrl.put("action","wbgetentities");
		baseUrl.put("ids",pageId);
		baseUrl.put("props","labels|descriptions|claims");
		baseUrl.put("languages","fr");
		baseUrl.put("format", "json");
		HttpRequest finalRequest = requestFactory.buildGetRequest(baseUrl);
		HttpResponse finalHttpResponse = finalRequest.execute();

		// JSON parsing
		JSONObject finalResponse = (JSONObject) parser.parse(finalHttpResponse.parseAsString());
		Object entities =  finalResponse.get("entities");
		JSONObject Jentities = (JSONObject) entities;
		JSONObject Jid = (JSONObject) Jentities.get(fRes.getPageId());
		Object claims = Jid.get("claims");
		JSONObject Jclaims = (JSONObject) claims;

		// Add full name
		fRes.setName(getPageName(pageId));

		// Add summary
		fRes.setDesc(getSummary(fRes.getName()));

		// Add gender
		fRes.setGender(getProperty("P21", Jclaims));

		// Add nationality
		fRes.setNationality(getProperty("P27", Jclaims));

		// Add type (instanceOf)
		fRes.setInstanceOf(getProperty("P31", Jclaims));

		// Add occupation
		fRes.setOccupation(getProperty("P106", Jclaims));

		// Add style
		fRes.setStyle(getProperty("P136", Jclaims));

		// Add photo
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
			fRes.setPhotoUrl("https://commons.wikimedia.org/wiki/Special:FilePath/"+finalPhotoName);
		}
		
		// Add birth date
		Object dtBirth = Jclaims.getOrDefault("P569", "noBirthDate");
		if (dtBirth.toString() != "noBirthDate") {
			JSONArray JdtBirth = (JSONArray) dtBirth;
			Object mainsnak = JdtBirth.get(0);
			JSONObject Jmainsnak = (JSONObject) mainsnak;
			Object datavalue = Jmainsnak.get("mainsnak");
			JSONObject Jdatavalue = (JSONObject) datavalue;
			Object value = Jdatavalue.get("datavalue");
			JSONObject Jvalue = (JSONObject) value;
			Object namevalue = Jvalue.get("value");
			JSONObject Jnamevalue = (JSONObject) namevalue;
			Object nametext = Jnamevalue.get("time");
			fRes.setDateOfBirth(nametext.toString().substring(1, 11));
		}
		
		// Wikipedia page link
		fRes.getRelatedServices().put("Wikipedia","https://en.wikipedia.org/wiki/" + getPageName(pageId));
		
		// Add backlinks (for recommendations)
		fRes.setBacklinks(getBackLinks(pageId));

		// Add related services
		
		// MusicBrainz
		String musicBrainizid = getRelatedService("P434", Jclaims);
		if(musicBrainizid != "noRelatedService")
			fRes.getRelatedServices().put("MusicBrainz", "https://musicbrainz.org/artist/" + musicBrainizid);
		
		// Twitter
		String twitterId = getRelatedService("P2002", Jclaims);
		if(twitterId != "noRelatedService")
			fRes.getRelatedServices().put("Twitter", "https://twitter.com/" + twitterId);

		// Spotify
		String SpotifyId = getRelatedService("P1902", Jclaims);
		if(SpotifyId != "noRelatedService")
			fRes.getRelatedServices().put("Spotify", "https://open.spotify.com/artist/" + SpotifyId);

		return fRes;

	}
	
	public static String getProperty(String property, JSONObject Jclaims) throws IOException, ParseException {
		Object prop = Jclaims.getOrDefault(property, "noProperty");
		if (prop.toString() != "noProperty") {
			JSONArray Jprop = (JSONArray) prop;
			Object mainsnak = Jprop.get(0);
			JSONObject Jmainsnak = (JSONObject) mainsnak;
			Object datavalue = Jmainsnak.get("mainsnak");
			JSONObject Jdatavalue = (JSONObject) datavalue;
			Object value = Jdatavalue.get("datavalue");
			JSONObject Jvalue = (JSONObject) value;
			Object value2 = Jvalue.get("value");
			JSONObject Jvalue2 = (JSONObject) value2;
			String propName = getPageName((String) Jvalue2.get("id"));
			return propName;
		}
		return "noProperty";
	}
	
	public static String getRelatedService (String property, JSONObject Jclaims) {
		Object prop = Jclaims.getOrDefault(property, "noRelatedService");
		if (prop.toString() != "noRelatedService") {
			JSONArray Jproperty = (JSONArray) prop;
			Object mainsnak = Jproperty.get(0);
			JSONObject Jmainsnak = (JSONObject) mainsnak;
			Object datavalue = Jmainsnak.get("mainsnak");
			JSONObject Jdatavalue = (JSONObject) datavalue;
			Object value = Jdatavalue.get("datavalue");
			JSONObject Jvalue = (JSONObject) value;
			Object relatedServiceValue = Jvalue.get("value");
			return relatedServiceValue.toString();
		}
		return "noRelatedService";
	}

	/**
	 * This method gets all pages linking to a specific page (backlinks).
	 * @param pageId : the id of the sepcific page
	 * @return the list of the backlink pages' names
	 * @throws IOException
	 * @throws ParseException
	 */
	public static List<String> getBackLinks(String pageId) throws IOException, ParseException{
		// Wikipedia API request
		HttpTransport httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		JSONParser parser = new JSONParser();
		GenericUrl baseUrl = new GenericUrl("https://www.wikipedia.org/w/api.php?");
		baseUrl.put("action","query");
		baseUrl.put("format", "json");
		baseUrl.put("list", "backlinks");
		baseUrl.put("bltitle", getPageName(pageId));
		HttpRequest finalRequest = requestFactory.buildGetRequest(baseUrl);
		HttpResponse finalHttpResponse = finalRequest.execute();

		// JSON Parsing
		JSONObject finalResponse = (JSONObject) parser.parse(finalHttpResponse.parseAsString());
		Object query = finalResponse.get("query");
		JSONObject Jquery = (JSONObject) query;
		Object backlinks = Jquery.get("backlinks");
		JSONArray Jbacklinks = (JSONArray) backlinks;

		List<String> backlinksTitles = new ArrayList<>();

		for (Object bl : Jbacklinks) {
			JSONObject Jbl = (JSONObject) bl;
			String title = (String) Jbl.get("title");
			backlinksTitles.add(title);
		}
		return backlinksTitles;
	}

	/**
	 * This method gets the full name of a page on Wikipedia given its pageId
	 * @param pageId
	 * @return the name of the page
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String getPageName(String pageId) throws IOException, ParseException {
		// Wikidata API request
		HttpTransport httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		JSONParser parser = new JSONParser();
		GenericUrl baseUrl = new GenericUrl("https://www.wikidata.org/w/api.php?");
		baseUrl.put("action","wbgetentities");
		baseUrl.put("ids",pageId);
		baseUrl.put("props","sitelinks");
		baseUrl.put("sitefilter","enwiki");
		baseUrl.put("format", "json");
		HttpRequest finalRequest = requestFactory.buildGetRequest(baseUrl);
		HttpResponse finalHttpResponse = finalRequest.execute();
		
		//JSON parsing
		JSONObject finalResponse = (JSONObject) parser.parse(finalHttpResponse.parseAsString());
		Object entities = finalResponse.get("entities");
		JSONObject Jentities = (JSONObject) entities;
		Object id = Jentities.get(pageId);
		JSONObject Jid = (JSONObject) id;
		Object sitelinks = Jid.get("sitelinks");
		JSONObject Jsitelinks = (JSONObject) sitelinks;
		Object enwiki = Jsitelinks.getOrDefault("enwiki", "noWikidataSitelinks");

		// Use if we can get the Wikipedia page name directly from wikidata info
		if (enwiki.toString() != "noWikidataSitelinks") {
			JSONObject Jenwiki = (JSONObject) enwiki;
			Object title = Jenwiki.get("title");
			return  title.toString();
		}
		else {
			// Use if we cannot get the Wikipedia page name directly from wikidata info
			// Wikidata API request
			HttpTransport httpTransport2 = new NetHttpTransport();
			HttpRequestFactory requestFactory2 = httpTransport2.createRequestFactory();
			JSONParser parser2 = new JSONParser();
			GenericUrl baseUrl2 = new GenericUrl("https://www.wikidata.org/w/api.php?");
			baseUrl2.put("action","wbgetentities");
			baseUrl2.put("ids",pageId);
			baseUrl2.put("props","labels");
			baseUrl2.put("languages","en");
			baseUrl2.put("format", "json");
			HttpRequest finalRequest2 = requestFactory2.buildGetRequest(baseUrl2);
			HttpResponse finalHttpResponse2 = finalRequest2.execute();
			
			// JSON parsing to get the page label (will be used as full name)
			JSONObject finalResponse2 = (JSONObject) parser2.parse(finalHttpResponse2.parseAsString());
			Object entities2 = finalResponse2.get("entities");
			JSONObject Jentities2 = (JSONObject) entities2;
			Object page = Jentities2.get(pageId);
			JSONObject Jpage = (JSONObject) page;
			Object labels = Jpage.get("labels");
			JSONObject Jlabels = (JSONObject) labels;
			Object lang = Jlabels.get("en");
			JSONObject Jlang = (JSONObject) lang;
			if(Jlang != null) {
				Object value = Jlang.get("value");
				return value.toString();
			}
			else {
				return "Unnamed";
			}
		}
	}

	/**
	 * This method extracts the summary of a page given its full Wikipedia title.
	 * @param title
	 * @return the summary
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String getSummary(String title) throws IOException, ParseException {
		// Wikipedia API request
		HttpTransport httpTransport = new NetHttpTransport();
		HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
		JSONParser parser = new JSONParser();
		GenericUrl baseUrl = new GenericUrl("https://www.wikipedia.org/w/api.php?");
		baseUrl.put("action","query");
		baseUrl.put("titles",title);
		baseUrl.put("prop","extracts");
		baseUrl.put("format", "json");
		HttpRequest finalRequest = requestFactory.buildGetRequest(baseUrl);
		HttpResponse finalHttpResponse = finalRequest.execute();
		
		// JSON parsing
		JSONObject finalResponse = (JSONObject) parser.parse(finalHttpResponse.parseAsString());
		Object query = finalResponse.get("query");
		JSONObject Jquery = (JSONObject) query;
		Object pages = Jquery.get("pages");
		JSONObject Jpages = (JSONObject) pages;
		Object key = null;
		for (Object element : Jpages.keySet()) {
			key = element;
			break;
		}
		Object objectKey = Jpages.get(key);
		JSONObject JobjectKey = (JSONObject) objectKey;
		Object extract = JobjectKey.get("extract");
		return (String) extract;
	}

	public static void main(String[] args) throws IOException, ParseException {
		getFinalResult("Q3052772");
	}
}
