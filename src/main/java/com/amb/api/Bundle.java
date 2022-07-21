package com.amb.api;

import com.amb.commonFunctions.ReadTextFile;
import com.amb.commonFunctions.ReadValueFromJson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Bundle {

	boolean flag=false;
	
	public Bundle() throws Exception{
	}

	String HOME_URL = ReadTextFile.readApplicationFile("ApiURL");

	/**
	 * This method will use for finding bundle id.
	 * @param secretKey
	 * @param bundleId
	 * @param statusValue
	 * @return
	 */
		public boolean findValidCreatedBundleID(String secretKey, String bundleId, int statusValue) {
			try {
				RestAssured.baseURI = HOME_URL;
				RequestSpecification httpRequest = RestAssured.given();
				httpRequest.header("Content-Type", "application/json");
				httpRequest.header("Authorization",secretKey);
				Response response = httpRequest.get(HOME_URL+"/bundle/"+bundleId);
				String responseBody = response.getBody().asString();
				if(response.statusCode()==200)
				flag= ReadValueFromJson.getStringvalueFromJsonResponse(ReadValueFromJson.getRequestBody(responseBody),"bundleId").equals(bundleId);
				else
					flag=false;
			} catch (Exception ex) {
				System.out.println(ex);
			}
			return flag;
		}
		
}
