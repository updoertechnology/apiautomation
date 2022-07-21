package com.amb.api;

import org.json.simple.JSONObject;
import org.testng.Assert;

import com.amb.commonFunctions.ReadTextFile;
import com.amb.commonFunctions.ReadValueFromJson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CreateToken {
	boolean flag=false;
	public CreateToken() throws Exception {
	}

	String HOME_URL = ReadTextFile.readApplicationFile("ApiURL");

	/**
	 * This method will generate the Token Key.
	 * @param body
	 * @return
	 */
	public String createToken(String body, String secretKey) {
		String token = "";
		try {
			RestAssured.baseURI = HOME_URL;
			RequestSpecification httpRequest = RestAssured.given();
			httpRequest.header("Content-Type", "application/json");
			httpRequest.header("Authorization",secretKey);
			JSONObject json=ReadValueFromJson.getRequestBody(body);
			httpRequest.body(json.toJSONString());
			Response response = httpRequest.post(HOME_URL+"/token");
			String responseBody = response.getBody().asString();
			Assert.assertTrue(response.getStatusCode()==201, "Error...!! Token is not generated..!!");
			token=ReadValueFromJson.getStringvalueFromJsonResponse(ReadValueFromJson.getRequestBody(responseBody),"token");
			
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return token;
	}
	
	/**
	 * This method will generate the Token Key with Invalid Data.
	 * @param body
	 * @return
	 */
	public boolean createTokenWithInvalidData(String body, String secretKey, int statusCode, String responseValue) {
		try {
			RestAssured.baseURI = HOME_URL;
			RequestSpecification httpRequest = RestAssured.given();
			httpRequest.header("Content-Type", "application/json");
			httpRequest.header("Authorization",secretKey);
			JSONObject json=ReadValueFromJson.getRequestBody(body);
			httpRequest.body(json.toJSONString());
			Response response = httpRequest.post(HOME_URL+"/token");
			String responseBody = response.getBody().asString();
			if(response.getStatusCode()==statusCode && responseBody.equals(responseValue))
				flag=true;
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return flag;
	}
	
	/**
	 * This method will generate the Token Key without secret key.
	 * @param body
	 * @return
	 */
	public boolean createTokenWithoutSecretKey(String body, int statusCode, String responseValue) {
		try {
			RestAssured.baseURI = HOME_URL;
			RequestSpecification httpRequest = RestAssured.given();
			httpRequest.header("Content-Type", "application/json");
			JSONObject json=ReadValueFromJson.getRequestBody(body);
			httpRequest.body(json.toJSONString());
			Response response = httpRequest.post(HOME_URL+"/token");
			String responseBody = response.getBody().asString();
			if(response.getStatusCode()==statusCode && responseBody.equals(responseValue))
				flag=true;
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return flag;
	}
	
	/**
	 * This method will generate the Token Key without contentType.
	 * @param body
	 * @return
	 */
	public boolean createTokenWithContentType(String body, String secretKey, int statusCode, String responseValue) {
		try {
			RestAssured.baseURI = HOME_URL;
			RequestSpecification httpRequest = RestAssured.given();
			httpRequest.header("Authorization",secretKey);
			JSONObject json=ReadValueFromJson.getRequestBody(body);
			httpRequest.body(json.toJSONString());
			Response response = httpRequest.post(HOME_URL+"/token");
			String responseBody = response.getBody().asString();
			if(response.getStatusCode()==statusCode && responseBody.equals(responseValue))
				flag=true;
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return flag;
	}
		
}
