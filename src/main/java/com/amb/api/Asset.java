package com.amb.api;

import org.json.simple.JSONObject;
import org.testng.Assert;

import com.amb.commonFunctions.ReadTextFile;
import com.amb.commonFunctions.ReadValueFromJson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Asset {
	boolean flag = false;

	public Asset() throws Exception {
	}

	String HOME_URL = ReadTextFile.readApplicationFile("ApiURL");

	/**
	 * This method will create Asset.
	 * 
	 * @param body
	 * @return
	 */
	public String createAsset(String body, String secretKey) {
		String assetId = "";
		try {
			RestAssured.baseURI = HOME_URL;
			RequestSpecification httpRequest = RestAssured.given();
			httpRequest.header("Content-Type", "application/json");
			httpRequest.header("Authorization", secretKey);
			JSONObject json = ReadValueFromJson.getRequestBody(body);
			httpRequest.body(json.toJSONString());
			Response response = httpRequest.post(HOME_URL + "/assets");
			String responseBody = response.getBody().asString();
			Assert.assertTrue(response.getStatusCode() == 201, "Asset not created successfully");
			assetId = ReadValueFromJson.getStringvalueFromJsonResponse(ReadValueFromJson.getRequestBody(responseBody),
					"assetId");
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return assetId;
	}

	/**
	 * This method will use for try to creating account without secret key.
	 * @param body
	 * @param statusValue
	 * @param responseValue
	 * @return
	 */
	public boolean createAssetWithoutSecretKey(String body, int statusValue, String responseValue) {
		try {
			RestAssured.baseURI = HOME_URL;
			RequestSpecification httpRequest = RestAssured.given();
			httpRequest.header("Content-Type", "application/json");
			JSONObject json = ReadValueFromJson.getRequestBody(body);
			httpRequest.body(json.toJSONString());
			Response response = httpRequest.post(HOME_URL + "/assets");
			String responseBody = response.getBody().asString();
			if (response.statusCode() == statusValue && responseBody.equals(responseValue))
				flag = true;
			else
				flag = false;
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return flag;
	}

	/**
	 * This method will use for try to creating account without content type.
	 * @param body
	 * @param secretKey
	 * @param statusValue
	 * @param responseValue
	 * @return
	 */
	public boolean createAssetWithoutContentType(String body, String secretKey, int statusValue, String responseValue) {
		try {
			RestAssured.baseURI = HOME_URL;
			RequestSpecification httpRequest = RestAssured.given();
			httpRequest.header("Authorization", secretKey);
			JSONObject json = ReadValueFromJson.getRequestBody(body);
			httpRequest.body(json.toJSONString());
			Response response = httpRequest.post(HOME_URL + "/assets");
			String responseBody = response.getBody().asString();
			if (response.statusCode() == statusValue && responseBody.equals(responseValue))
				flag = true;
			else
				flag = false;
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return flag;
	}
	
	/**
	 * This method will use for try to creating account with invalid data.
	 * @param body
	 * @param secretKey
	 * @param statusValue
	 * @param responseValue
	 * @return
	 */
	public boolean createAssetWithInvalidData(String body, String secretKey, int statusValue, String responseValue) {
		try {
			RestAssured.baseURI = HOME_URL;
			RequestSpecification httpRequest = RestAssured.given();
			httpRequest.header("Content-Type", "application/json");
			httpRequest.header("Authorization", secretKey);
			JSONObject json = ReadValueFromJson.getRequestBody(body);
			httpRequest.body(json.toJSONString());
			Response response = httpRequest.post(HOME_URL + "/assets");
			String responseBody = response.getBody().asString();
			if (response.statusCode() == statusValue && responseBody.equals(responseValue))
				flag = true;
			else
				flag = false;
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return flag;
	}

	/**
	 * This metghod will use for finding account with valid asset id.
	 * @param secretKey
	 * @param asset
	 * @param statusValue
	 * @return
	 */
	public boolean findValidCreatedAsset(String secretKey, String asset, int statusValue) {
		try {
			RestAssured.baseURI = HOME_URL;
			RequestSpecification httpRequest = RestAssured.given();
			httpRequest.header("Content-Type", "application/json");
			httpRequest.header("Authorization", secretKey);
			Response response = httpRequest.get(HOME_URL + "/assets/" + asset);
			String responseBody = response.getBody().asString();
			System.out.println(response.statusCode());
			if (response.statusCode() == 200)
				flag = ReadValueFromJson
						.getStringvalueFromJsonResponse(ReadValueFromJson.getRequestBody(responseBody), "assetId")
						.equals(asset);
			if (response.statusCode() == statusValue)
				flag = true;
			else
				flag = false;
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return flag;
	}

	/**
	 * This method will use for finding asset with parameter.
	 * @param secretKey
	 * @param statusCode
	 * @param parameter
	 * @return
	 */
	public boolean findAssetWithParameter(String secretKey, int statusCode, String parameter) {
		try {
			RestAssured.baseURI = HOME_URL;
			RequestSpecification httpRequest = RestAssured.given();
			httpRequest.header("Authorization", secretKey);
			httpRequest.header("Content-Type", "application/json");
			Response response = httpRequest.get(HOME_URL + "/assets?" + parameter);
			if (response.getStatusCode() == statusCode)
				flag = true;
			else
				flag = false;
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return flag;
	}

}
