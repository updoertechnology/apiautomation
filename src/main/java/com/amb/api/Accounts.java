 package com.amb.api;

import org.json.simple.JSONObject;
import org.testng.Assert;

import com.amb.commonFunctions.ReadTextFile;
import com.amb.commonFunctions.ReadValueFromJson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Accounts {
	boolean flag=false; 
	public Accounts() throws Exception {
	}

	String HOME_URL = ReadTextFile.readApplicationFile("ApiURL");

	/**
		 * This method will create account.
		 * @param body
		 * @return
		 */
		public String createAccount(String body, String secretKey) {
			String address="";
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Content-Type", "application/json");
				httpRequest.header("Authorization",secretKey);
				JSONObject json=ReadValueFromJson.getRequestBody(body);
				httpRequest.body(json.toJSONString());
				Response response = httpRequest.post(HOME_URL+"/accounts");
				String responseBody = response.getBody().asString();
				Assert.assertTrue(response.getStatusCode()==201,"Account not created successfully");
				address= ReadValueFromJson.getStringvalueFromJsonResponse(ReadValueFromJson.getRequestBody(responseBody),"address");
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return address;
			
		}
		
		/**
		 * This method will trying to create account with invalid Authorization key/Data
		 * @param body
		 * @param secretKey
		 * @param statusCode
		 * @param responseValue
		 * @return
		 */
		public boolean createAccountwithInvalidData(String body, String secretKey, int statusCode, String responseValue) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Content-Type", "application/json");
				httpRequest.header("Authorization",secretKey);
				JSONObject json=ReadValueFromJson.getRequestBody(body);
				httpRequest.body(json.toJSONString());
				Response response = httpRequest.post(HOME_URL+"/accounts");
				String responseBody = response.getBody().asString();
				if(response.getStatusCode()==statusCode && responseBody.equals(responseValue))
					flag=true;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
			
		}
		
		/**
		 * This method is trying to create account without Authorization key. 
		 * @param body
		 * @param statusCode
		 * @param responseValue
		 * @return
		 */
		public boolean createAccountWithoutAuthorizationKey(String body, int statusCode, String responseValue) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Content-Type", "application/json");
				JSONObject json=ReadValueFromJson.getRequestBody(body);
				httpRequest.body(json.toJSONString());
				Response response = httpRequest.post(HOME_URL+"/accounts");
				String responseBody = response.getBody().asString();
				if(response.getStatusCode()==statusCode && responseBody.equals(responseValue))
					flag=true;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
			
		}
		
		/**
		 * This method is trying to create account without Content Type. 
		 * @param body
		 * @param secretKey
		 * @param statusCode
		 * @param responseValue
		 * @return
		 */
		public boolean createAccountwithoutContentType(String body, String secretKey, int statusCode, String responseValue) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Authorization",secretKey);
				JSONObject json=ReadValueFromJson.getRequestBody(body);
				httpRequest.body(json.toJSONString());
				Response response = httpRequest.post(HOME_URL+"/accounts");
				String responseBody = response.getBody().asString();
				if(response.getStatusCode()==statusCode && responseBody.equals(responseValue))
					flag=true;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
		}
		
		/**
		 * This method will use for finding a created account.
		 * @param accountAddress
		 * @param secretKey
		 * @return
		 */
		public String findCreatedAccount(String accountAddress, String secretKey) {
			String address="";
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Authorization",secretKey);
				Response response = httpRequest.get(HOME_URL+"/accounts/"+accountAddress);
				String responseBody = response.getBody().asString();
				Assert.assertTrue(response.getStatusCode()==200,"Created Account found successfully");
				address= ReadValueFromJson.getStringvalueFromJsonResponse(ReadValueFromJson.getRequestBody(responseBody),"address");
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return address;
		}
		
		/**
		 * This method will use for finding account with invalid data.
		 * @param accountAddress
		 * @param secretKey
		 * @param statusCode
		 * @param responseValue
		 * @return
		 */
		public boolean findCreatedAccountWithInvalidData(String accountAddress, String secretKey, int statusCode,String responseValue) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Authorization",secretKey);
				Response response = httpRequest.get(HOME_URL+"/accounts/"+accountAddress);
				String responseBody = response.getBody().asString();
				if(response.getStatusCode()==statusCode && responseBody.equals(responseValue))
					flag=true;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
		}
		
		/**
		 * This method will use for finding account without Authorization key.
		 * @param accountAddress
		 * @param statusCode
		 * @param responseValue
		 * @return
		 */
		public boolean findCreatedAccountWithoutAuthorization(String accountAddress, int statusCode,String responseValue) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				Response response = httpRequest.get(HOME_URL+"/accounts/"+accountAddress);
				String responseBody = response.getBody().asString();
				if(response.getStatusCode()==statusCode && responseBody.equals(responseValue))
					flag=true;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
		}
		
		/**
		 * This method will use for finding an account with valid parameter.
		 * @param secretKey
		 * @param statusCode
		 * @param value
		 * @return
		 */
		public boolean findCreatedAccountWithvalidParameter(String secretKey, int statusCode, int value) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Authorization",secretKey);
				Response response = httpRequest.get(HOME_URL+"/accounts?accessLevel="+value+"&perPage="+value+"&page="+value+"");
				System.out.println(response.getBody().asString());
				if(response.getStatusCode()==statusCode)
					flag=true;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
		}
		
		/**
		 * This method will use for finding an account with invalid parameter.
		 * @param secretKey
		 * @param statusCode
		 * @param value
		 * @return
		 */
		public boolean findCreatedAccountWithInvalidParameter(String secretKey, int statusCode, String value) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Authorization",secretKey);
				Response response = httpRequest.get(HOME_URL+"/accounts?accessLevel="+value+"&perPage="+value+"&page="+value+"");
				System.out.println(response.getBody().asString());
				int statusValue=response.getStatusCode();
				if(statusValue ==statusCode)
					flag=true;
				else {
					flag=false;
				}
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
		}
		
}


