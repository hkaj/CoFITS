package utc.bsfile.util;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonManager {

	private static JsonManager instance = null;
	
	public static JsonManager getInstance(){
		if (instance == null){
			instance = new JsonManager();
		}
		return instance;
	}
	
	public static void releaseInstance(){
		instance = null;
	}
	
	//Constructors
	private JsonManager() {}
	
	//Methods
	public JsonNode createJsonNode(File file){
		return createJsonNode((Object) file);
	}
	
	
	public JsonNode createJsonNode(String jsonContent){
		return createJsonNode((Object) jsonContent);
	}
	
	
	private JsonNode createJsonNode(Object obj){
		JsonNode jsonNode = null;
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonParser jp = null;
			if (obj instanceof String){
				jp = mapper.getFactory().createParser((String) obj);
			} else if (obj instanceof File){
				jp = mapper.getFactory().createParser((File) obj);
			}
			jsonNode = (JsonNode) mapper.readTree(jp);
		} catch (IOException e) {
			System.err.println("Unabled to parse json content");
			e.printStackTrace();
		}
		
		return jsonNode;
	}
	

}
