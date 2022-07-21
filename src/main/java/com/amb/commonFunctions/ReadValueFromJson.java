package com.amb.commonFunctions;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadValueFromJson {

	/**
	 * This method will read the value from json file and return the same.
	 * @param filepath
	 * @param propertyKey
	 * @return
	 */
	public static String readValueFromJson(String filepath, String propertyKey)  {
		String value = "";
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(filepath));
			JSONObject jsonObject = (JSONObject) obj;
			value = jsonObject.get(propertyKey).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * This method will return Json object with request body
	 * @param body
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getRequestBody(String body) {
		Map<Object,Object> requestMap= new HashMap<Object,Object>();
		JSONObject json= new JSONObject();
		String jsonvalue=body;
		ObjectMapper mapper= new ObjectMapper();
		try {
			requestMap=  mapper.readValue(jsonvalue, new TypeReference<Map<Object, Object>>(){});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		json.putAll(requestMap);
		return json;
	}
	
	/**
	 * This method will Return the value form json.
	 * @param jsonResponse
	 * @param key
	 * @return
	 */
	public static String getStringvalueFromJsonResponse(JSONObject jsonResponse, String key) {
		String value= jsonResponse.get(key).toString();
        return value;
	}
	
}
