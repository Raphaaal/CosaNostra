package controllers.search;

import java.io.FileInputStream;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.jayway.jsonpath.JsonPath;


public class SearchQuery {
	
	public JSONObject search(String query) {
		return null;
	}
	
  public static Properties properties = new Properties();
  
  public JSONObject kgSearch (String query) {
    try {
      properties.load(new FileInputStream("src/main/resources/kgsearch.properties"));

      HttpTransport httpTransport = new NetHttpTransport();
      HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
      JSONParser parser = new JSONParser();
      GenericUrl url = new GenericUrl("https://kgsearch.googleapis.com/v1/entities:search");
      url.put("query", query);
      url.put("limit", "10");
      url.put("indent", "true");
      url.put("key", properties.get("API_KEY"));
      HttpRequest request = requestFactory.buildGetRequest(url);
      HttpResponse httpResponse = request.execute();
      JSONObject response = (JSONObject) parser.parse(httpResponse.parseAsString());
      JSONArray elements = (JSONArray) response.get("itemListElement");
      System.out.println(response);
      System.out.println("------------------------");
      for (Object element : elements) {
    	try {  
        System.out.println(JsonPath.read(element, "$.result.name").toString());
        System.out.println(JsonPath.read(element, "$.result.@type").toString());
        System.out.println(JsonPath.read(element, "$.result.image.url").toString());
        System.out.println(JsonPath.read(element, "$.result.detailedDescription.url").toString());
        System.out.println(JsonPath.read(element, "$.result.description").toString());
        System.out.println(JsonPath.read(element, "$.result.detailedDescription.articleBody").toString());
    	}catch(Exception e) {
    		System.out.println(" ");
    	}
    	}
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }
}
