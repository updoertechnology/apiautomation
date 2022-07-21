package com.api.test;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.amb.api.Asset;
import com.amb.api.Bundle;
import com.amb.api.Events;
import com.amb.commonFunctions.CommonFunction;
import com.amb.commonFunctions.ReadTextFile;

public class VerifyAssets_Events_Bundle_APIs {

	public VerifyAssets_Events_Bundle_APIs() throws Exception {
	}

	String validSecretKey, invalidSecretKey, assetBody, assetValue, responseBodyInvalidSecretKey,
			invalidFormatSecretKey, invalidSecreyKeyFormat, responseBodyWithoutSecretKey,
			responseBodyWithoutContentType, eventBody, eventValue, bundleID;

	static String createdBy, timeStamp, perPageCount, invalidCreatedBy, invalidTimeStamp, invalidPerPageCount, assetID;

	Asset asset = new Asset();
	Events event = new Events();
	Bundle bundle = new Bundle();

	@BeforeClass
	public void readValuesFromConfigFile() throws Exception {
		validSecretKey = ReadTextFile.readApplicationFile("secretKey");
		invalidSecretKey = ReadTextFile.readApplicationFile("invalidSercetKeyForAsset");
		invalidFormatSecretKey = ReadTextFile.readApplicationFile("InvalidFormatSecretKey");
		responseBodyWithoutSecretKey = ReadTextFile.readApplicationFile("ResponseBodyWithoutSecretKey");
		responseBodyInvalidSecretKey = ReadTextFile.readApplicationFile("ResponseBodyInvalidSecretKey");
		responseBodyWithoutContentType = ReadTextFile.readApplicationFile("ResponseBodyWithoutContentType");
		invalidSecreyKeyFormat = ReadTextFile.readApplicationFile("InvalidSecreyKeyFormat");
		createdBy = ReadTextFile.readApplicationFile("CreatedBy");
		timeStamp = ReadTextFile.readApplicationFile("TimeStamp");
		perPageCount = ReadTextFile.readApplicationFile("PerPageCount");
		invalidCreatedBy = ReadTextFile.readApplicationFile("InvalidCreatedBy");
		invalidTimeStamp = ReadTextFile.readApplicationFile("InvalidTimeStamp");
		invalidPerPageCount = ReadTextFile.readApplicationFile("InvalidPerPageCount");
		assetID = ReadTextFile.readApplicationFile("assetID");
		int randomNum = CommonFunction.getRandomNumber(4);
		assetBody = "{\r\n" + "	    \"content\": {\r\n" + "	        \"idData\": {\r\n"
				+ "	            \"createdBy\": \"0xC3Df63EddB0b81438EAfef2eAFCB6e7f915fbeC5\",\r\n"
				+ "	            \"timestamp\": 1533837038,\r\n" + "	            \"sequenceNumber\": " + randomNum
				+ "\r\n" + "	        }\r\n" + "	    }\r\n" + "	}";

	}

	@Test
	public void verifyAssetCreationWithValidData() {
		assetValue = asset.createAsset(assetBody, validSecretKey);
		System.out.println(assetValue);
		Reporter.log("Asset has been successfully created..!!");
	}

	@Test
	public void verifyAssetCreationWithoutSecretKey() {
		Assert.assertTrue(asset.createAssetWithoutSecretKey(assetBody, 400, responseBodyWithoutSecretKey));
		Reporter.log("Asset Creation without Secret key.");
	}

	@Test
	public void verifyAssetCreationWithoutContentType() {
		Assert.assertTrue(
				asset.createAssetWithoutContentType(assetBody, validSecretKey, 400, responseBodyWithoutContentType));
		Reporter.log("Asset Creation without Content type.");
	}

	@Test(dependsOnMethods = "verifyAssetCreationWithValidData")
	public void verifyAssetCreationWithDuplicateValidData() {
		String responseValue = "{\"reason\":\"Invalid data: Asset with assetId=" + assetValue + " already exists\"}";
		Assert.assertTrue(asset.createAssetWithInvalidData(assetBody, validSecretKey, 400, responseValue),
				"Error..!! Getting Error while creation Asset with duplicate data.");
		Reporter.log("Asset Creation with duplicate Data.");
	}

	@Test
	public void verifyAssetCreationWithInvalidSecretKey() {
		Assert.assertTrue(
				asset.createAssetWithInvalidData(assetBody, invalidSecretKey, 401, responseBodyInvalidSecretKey),
				"Error..!! Getting Error while creation Asset with duplicate data.");
		Reporter.log("Asset Creation with invalid secret key.");
	}

	@Test
	public void verifyAssetCreationWithInvalidFormatSecretKey() {
		Assert.assertTrue(
				asset.createAssetWithInvalidData(assetBody, invalidFormatSecretKey, 400, invalidSecreyKeyFormat),
				"Error..!! Getting Error while creation Asset with Invalid data.");
		Reporter.log("Asset Creation with invaild format secret key.");
	}

	@Test(dependsOnMethods = "verifyAssetCreationWithValidData")
	public void verifyFindValidAsset() {
		Assert.assertTrue(asset.findValidCreatedAsset(validSecretKey, assetValue, 200),
				"Error..!! Getting Error while finding Asset with valid ID");
		Reporter.log("Asset Creation with invaild format secret key.");
	}

	@Test(dependsOnMethods = "verifyAssetCreationWithValidData")
	public void verifyFindAssetInvalidID() {
		String invalidAssetID = assetValue + "12";
		Assert.assertTrue(asset.findValidCreatedAsset(validSecretKey, invalidAssetID, 404),
				"Error..!! Getting Error while finding Asset with invalid ID");
		Reporter.log("Asset Creation with invaild format secret key.");
	}

	@Test(dataProvider = "validParameter")
	public void verifyFindAssetWithValidParameter(String parameter) {
		Assert.assertTrue(asset.findAssetWithParameter(validSecretKey, 200, parameter),
				"Error..!! Getting Error while finding Asset with valid parameter ID");
		Reporter.log("Asset Creation with invaild format secret key.");
	}

	@Test(dataProvider = "inValidParameter")
	public void verifyFindAssetWithInValidParameter(String parameter) {
		Assert.assertTrue(asset.findAssetWithParameter(validSecretKey, 400, parameter),
				"Error..!! Getting Error while finding Asset with Invalid parameter's");
		Reporter.log("Asset Creation with invaild format secret key.");
	}

	@Test(dependsOnMethods = "verifyAssetCreationWithValidData")
	public void verifyEventCreationWithValidData() {
		eventBody = "{\r\n" + "    \"content\": {\r\n" + "        \"idData\": {\r\n" + "            \"assetId\": \""
				+ assetValue + "\",\r\n"
				+ "            \"createdBy\": \"0xC3Df63EddB0b81438EAfef2eAFCB6e7f915fbeC5\",\r\n"
				+ "            \"accessLevel\": 4,\r\n" + "            \"timestamp\": 1533837038\r\n" + "        },\r\n"
				+ "        \"data\": [\r\n" + "{\r\n" + "\"type\": \"demo.entrytype\",\r\n"
				+ "\"value\": \"some value 28\" \r\n" + "}\r\n" + "]\r\n" + "    }\r\n" + "}";
		eventValue = event.createEvent(eventBody, validSecretKey, assetValue);
		Reporter.log("Events has been successfully created..!!");
	}

	@Test(dependsOnMethods = "verifyEventCreationWithValidData")
	public void verifyEventCreationWithDuplicateValidData() {
		eventBody = "{\r\n" + "    \"content\": {\r\n" + "        \"idData\": {\r\n" + "            \"assetId\": \""
				+ assetValue + "\",\r\n"
				+ "            \"createdBy\": \"0xC3Df63EddB0b81438EAfef2eAFCB6e7f915fbeC5\",\r\n"
				+ "            \"accessLevel\": 4,\r\n" + "            \"timestamp\": 1533837038\r\n" + "        },\r\n"
				+ "        \"data\": [\r\n" + "{\r\n" + "\"type\": \"demo.entrytype\",\r\n"
				+ "\"value\": \"some value 28\" \r\n" + "}\r\n" + "]\r\n" + "    }\r\n" + "}";
		String responseBody = "{\"reason\":\"Invalid data: Event with eventId=" + eventValue + " already exists\"}";
		Assert.assertTrue(event.createEventWithInvalidData(eventBody, validSecretKey, assetValue, 400, responseBody));
		Reporter.log("Try to create events with duplicate data.");
	}

	@Test(dependsOnMethods = "verifyEventCreationWithValidData")
	public void verifyEventCreationWithoutSecretKey() {
		eventBody = "{\r\n" + "    \"content\": {\r\n" + "        \"idData\": {\r\n" + "            \"assetId\": \""
				+ assetValue + "\",\r\n"
				+ "            \"createdBy\": \"0xC3Df63EddB0b81438EAfef2eAFCB6e7f915fbeC5\",\r\n"
				+ "            \"accessLevel\": 4,\r\n" + "            \"timestamp\": 1533837038\r\n" + "        },\r\n"
				+ "        \"data\": [\r\n" + "{\r\n" + "\"type\": \"demo.entrytype\",\r\n"
				+ "\"value\": \"some value 28\" \r\n" + "}\r\n" + "]\r\n" + "    }\r\n" + "}";
		Assert.assertTrue(event.createEventWithoutSecretKey(eventBody, assetValue, 400, responseBodyWithoutSecretKey));
		Reporter.log("Try to create events without Secrey key.");
	}

	@Test(dependsOnMethods = "verifyEventCreationWithValidData")
	public void verifyEventCreationWithoutContentType() {
		eventBody = "{\r\n" + "    \"content\": {\r\n" + "        \"idData\": {\r\n" + "            \"assetId\": \""
				+ assetValue + "\",\r\n"
				+ "            \"createdBy\": \"0xC3Df63EddB0b81438EAfef2eAFCB6e7f915fbeC5\",\r\n"
				+ "            \"accessLevel\": 4,\r\n" + "            \"timestamp\": 1533837038\r\n" + "        },\r\n"
				+ "        \"data\": [\r\n" + "{\r\n" + "\"type\": \"demo.entrytype\",\r\n"
				+ "\"value\": \"some value 28\" \r\n" + "}\r\n" + "]\r\n" + "    }\r\n" + "}";
		Assert.assertTrue(event.createEventWithoutContentType(eventBody, validSecretKey, assetValue, 400,
				responseBodyWithoutContentType));
		Reporter.log("Try to create events without ContentType..");
	}

	@Test(dependsOnMethods = "verifyEventCreationWithValidData")
	public void verifyEventCreationWithInvalidSecretKey() {
		eventBody = "{\r\n" + "    \"content\": {\r\n" + "        \"idData\": {\r\n" + "            \"assetId\": \""
				+ assetValue + "\",\r\n"
				+ "            \"createdBy\": \"0xC3Df63EddB0b81438EAfef2eAFCB6e7f915fbeC5\",\r\n"
				+ "            \"accessLevel\": 4,\r\n" + "            \"timestamp\": 1533837038\r\n" + "        },\r\n"
				+ "        \"data\": [\r\n" + "{\r\n" + "\"type\": \"demo.entrytype\",\r\n"
				+ "\"value\": \"some value 28\" \r\n" + "}\r\n" + "]\r\n" + "    }\r\n" + "}";
		Assert.assertTrue(event.createEventWithInvalidData(eventBody, invalidSecretKey, assetValue, 401,
				responseBodyInvalidSecretKey));
		Reporter.log("Try to create events with duplicate data.");
	}

	@Test(dependsOnMethods = "verifyEventCreationWithValidData")
	public void verifyFindValidEvents() {
		bundleID = event.findValidCreatedEvents(validSecretKey, eventValue, 200);
		System.out.println(bundleID);
		Reporter.log("Finding events with valid eventID.");
	}

	@Test(dependsOnMethods = "verifyEventCreationWithValidData")
	public void verifyFindInvalidEvents() {
		String invalidEventsId = eventValue + "12";
		event.findValidCreatedEvents(validSecretKey, invalidEventsId, 404);
		Reporter.log("Finding events with Invalid eventID.");
	}

	@Test(dataProvider = "validEventParameter")
	public void verifyFindValidEventsWithParameter(String parameter) {
		Assert.assertTrue(event.findEventsWithParameter(validSecretKey, 200, parameter),
				"Error..!! Getting Error while finding events with valid ID");
		Reporter.log("Finding events with valid parameters.");
	}

	@Test(dependsOnMethods = "verifyFindValidEvents")
	public void verifyFindValidBundle() {
		Assert.assertTrue(bundle.findValidCreatedBundleID(validSecretKey, bundleID, 200),
				"Error..!! Getting Error while finding bundle with valid ID");
		Reporter.log("Finding bundle with valid bundleID.");
	}

	@Test(dependsOnMethods = "verifyFindValidEvents")
	public void verifyFindInvalidBundle() {
		String invalidBundleId = bundleID + "12";
		Assert.assertTrue(bundle.findValidCreatedBundleID(validSecretKey, invalidBundleId, 404),
				"Error..!! Getting Error while finding bundle with valid ID");
		Reporter.log("Finding bundle with valid bundleID.");
	}

	@DataProvider(name = "validParameter")
	public static Object[][] validParameter() {
		return new Object[][] { { "perPage=" + perPageCount }, { "createdBy=" + createdBy },
				{ "fromTimestamp=" + timeStamp + "&toTimestamp=" + timeStamp }, { "identifier" },
				{ "perPage=" + perPageCount + "&page=3&createdBy=" + createdBy + "&fromTimestamp=" + timeStamp
						+ "&toTimestamp=" + timeStamp + "&identifier" } };
	}

	@DataProvider(name = "inValidParameter")
	public static Object[][] inValidParameter() {
		return new Object[][] { { "perPage=" + invalidPerPageCount }, { "createdBy=" + invalidCreatedBy },
				{ "fromTimestamp=" + invalidTimeStamp + "&toTimestamp=" + invalidTimeStamp },
				{ "perPage=" + invalidPerPageCount + "&page=3&createdBy=" + invalidCreatedBy + "&fromTimestamp="
						+ invalidTimeStamp + "&toTimestamp=" + invalidTimeStamp + "&identifier" } };
	}

	@DataProvider(name = "validEventParameter")
	public static Object[][] validEventParameter() {
		return new Object[][] { { "perPage=" + perPageCount }, { "createdBy=" + createdBy }, { "assetId=" + assetID },
				{ "fromTimestamp=" + timeStamp + "&toTimestamp=" + timeStamp },
				{ "perPage=" + perPageCount + "&page=3&createdBy=" + createdBy + "&fromTimestamp=" + timeStamp
						+ "&toTimestamp=" + timeStamp } };
	}
}
