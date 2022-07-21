package com.amb.api;

import org.json.simple.JSONObject;
import org.testng.Assert;

import com.amb.commonFunctions.ReadTextFile;
import com.amb.commonFunctions.ReadValueFromJson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Events {

	boolean flag=false;
	
	public Events() throws Exception{
	}

	String HOME_URL = ReadTextFile.readApplicationFile("ApiURL");

	/**
		 * This method will create Event.
		 * @param body
		 * @return
		 */
		public String createEvent(String body, String secretKey, String assetID) {
			String eventId="";
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Content-Type", "application/json");
				httpRequest.header("Authorization",secretKey);
				JSONObject json=ReadValueFromJson.getRequestBody(body);
				httpRequest.body(json.toJSONString());
				Response response = httpRequest.post(HOME_URL+"/assets/"+assetID+ "/events");
				String responseBody = response.getBody().asString();
				Assert.assertTrue(response.getStatusCode()==201,"Event not created successfully");
				eventId= ReadValueFromJson.getStringvalueFromJsonResponse(ReadValueFromJson.getRequestBody(responseBody),"eventId");
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return eventId;
		}
		
		/**
		 * This method will try to create event with invalid data
		 * @param body
		 * @param secretKey
		 * @param assetID
		 * @param status
		 * @param responseValue
		 * @return
		 */
		public boolean createEventWithInvalidData(String body, String secretKey, String assetID, int status, String responseValue) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Content-Type", "application/json");
				httpRequest.header("Authorization",secretKey);
				JSONObject json=ReadValueFromJson.getRequestBody(body);
				httpRequest.body(json.toJSONString());
				Response response = httpRequest.post(HOME_URL+"/assets/"+assetID+ "/events");
				String responseBody = response.getBody().asString();
				if(response.statusCode()==status && responseBody.equals(responseValue))
					flag=true;
					else 
						flag=false;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
		}
		
		/**
		 * This method will try to create event without secret key.
		 * @param body
		 * @param assetID
		 * @param status
		 * @param responseValue
		 * @return
		 */
		public boolean createEventWithoutSecretKey(String body, String assetID, int status, String responseValue) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Content-Type", "application/json");
				JSONObject json=ReadValueFromJson.getRequestBody(body);
				httpRequest.body(json.toJSONString());
				Response response = httpRequest.post(HOME_URL+"/assets/"+assetID+ "/events");
				String responseBody = response.getBody().asString();
				if(response.statusCode()==status && responseBody.equals(responseValue))
					flag=true;
					else 
						flag=false;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
		}
		
		/**
		 * This method will create event eithout content type.
		 * @param body
		 * @param secretKey
		 * @param assetID
		 * @param status
		 * @param responseValue
		 * @return
		 */
		public boolean createEventWithoutContentType(String body, String secretKey, String assetID, int status, String responseValue) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Authorization",secretKey);
				JSONObject json=ReadValueFromJson.getRequestBody(body);
				httpRequest.body(json.toJSONString());
				Response response = httpRequest.post(HOME_URL+"/assets/"+assetID+ "/events");
				String responseBody = response.getBody().asString();
				if(response.statusCode()==status && responseBody.equals(responseValue))
					flag=true;
					else 
						flag=false;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
		}
		
		/**
		 * This method will use for finding event with valid event id.
		 * @param secretKey
		 * @param events
		 * @param statusValue
		 * @return
		 */
		public String findValidCreatedEvents(String secretKey, String events, int statusValue) {
			String bundleID="";
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Content-Type", "application/json");
				httpRequest.header("Authorization",secretKey);
				Response response = httpRequest.get(HOME_URL+"/events/"+events);
				String responseBody = response.getBody().asString();
				if(response.statusCode()==200) {
				flag= ReadValueFromJson.getStringvalueFromJsonResponse(ReadValueFromJson.getRequestBody(responseBody),"eventId").equals(events);
				String data= ReadValueFromJson.getStringvalueFromJsonResponse(ReadValueFromJson.getRequestBody(responseBody),"metadata");
				bundleID=data.split("=")[1].split(",")[0];
				}
				if(response.statusCode()==404) {
				flag=true;
				}
				Assert.assertTrue(flag);
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return bundleID;
		}
		
		/**
		 * This method will use for finding event with parameter.
		 * @param secretKey
		 * @param statusCode
		 * @param parameter
		 * @return
		 */
		public boolean findEventsWithParameter(String secretKey, int statusCode, String parameter) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Authorization",secretKey);
				httpRequest.header("Content-Type", "application/json");
				Response response = httpRequest.get(HOME_URL+"/events?"+parameter);
				if(response.getStatusCode()==statusCode)
					flag=true;
				else
					flag=false;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
		}
}
