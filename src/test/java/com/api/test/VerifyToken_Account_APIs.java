package com.api.test;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.amb.api.Accounts;
import com.amb.api.CreateToken;
import com.amb.commonFunctions.ReadTextFile;
import com.amb.commonFunctions.ReadValueFromJson;

public class VerifyToken_Account_APIs {

	public VerifyToken_Account_APIs() throws Exception {
	}

	String validSecretKey, invalidSecretKey, validAMB_Token, responseBodyWithInvalidTimestamp,
			responseBodyWithInvalidSecretKey, responseBodyWithoutSecretKey, responseBodyWithoutContentType, address,
			responseBodyWithInvalidAuthorization, responseBodyWithoutAuthorizationKey, responseBodyWithExtraParameter;
	String filepath = ReadTextFile.getFilePath() + "\\src\\main\\resources\\com\\api\\jsonBody\\json.txt";

	CreateToken createToken = new CreateToken();
	Accounts account = new Accounts();

	@BeforeClass
	public void readValuesFromConfigFile() throws Exception {
		validSecretKey = ReadTextFile.readApplicationFile("secretKey");
		invalidSecretKey = ReadTextFile.readApplicationFile("InvalidSecretKey");
		responseBodyWithInvalidTimestamp = ReadTextFile.readApplicationFile("ResponseBodyWithInvalidTimestamp");
		responseBodyWithoutSecretKey = ReadTextFile.readApplicationFile("ResponseBodyWithoutSecretKey");
		responseBodyWithInvalidSecretKey = ReadTextFile.readApplicationFile("ResponseBodyWithInvalidSecretKey");
		responseBodyWithoutContentType = ReadTextFile.readApplicationFile("ResponseBodyWithoutContentType");
		responseBodyWithInvalidAuthorization = ReadTextFile.readApplicationFile("ResponseBodyWithInvalidAuthorization");
		responseBodyWithoutAuthorizationKey = ReadTextFile.readApplicationFile("ResponseBodyWithoutAuthorizationKey");
		responseBodyWithExtraParameter = ReadTextFile.readApplicationFile("ResponseBodyWithExtraParameter");
	}

	@Test
	public void verifyTokenWithValidTimestamp() {
		String validTokenBody = ReadValueFromJson.readValueFromJson(filepath, "token");
		validAMB_Token = createToken.createToken(validTokenBody, validSecretKey);
		System.out.println("Valid tiken is:  " + validAMB_Token);
		Reporter.log("Token has been successfully created..!!");
	}

	@Test
	public void verifyTokenWithInvalidTimestemp() {
		String invalidTimestampToken = ReadValueFromJson.readValueFromJson(filepath, "InvalidTimestampToken");
		Assert.assertTrue(createToken.createTokenWithInvalidData(invalidTimestampToken, validSecretKey, 400,
				responseBodyWithInvalidTimestamp));
		Reporter.log("Verify Token With invalid Stamp");
	}

	@Test
	public void verifyTokenWithInvalidSecretKey() {
		String validTokenBody = ReadValueFromJson.readValueFromJson(filepath, "token");
		Assert.assertTrue(createToken.createTokenWithInvalidData(validTokenBody, invalidSecretKey, 400,
				responseBodyWithInvalidSecretKey));
		Reporter.log("Verify Token with invalid Secret key");
	}

	@Test
	public void verifyTokenWithoutSecretKey() {
		String validTokenBody = ReadValueFromJson.readValueFromJson(filepath, "token");
		Assert.assertTrue(createToken.createTokenWithoutSecretKey(validTokenBody, 401, responseBodyWithoutSecretKey));
		Reporter.log("Verify Token without Secret key");
	}

	@Test
	public void verifyTokenWithoutContentType() {
		String validTokenBody = ReadValueFromJson.readValueFromJson(filepath, "token");
		Assert.assertTrue(createToken.createTokenWithInvalidData(validTokenBody, validSecretKey, 400,
				responseBodyWithoutContentType));
		Reporter.log("Verify Tiken Without Content Type.");
	}

	@Test(dependsOnMethods = "verifyTokenWithValidTimestamp")
	public void createAccount() {
		String secretKey = "AMB_TOKEN " + validAMB_Token;
		String validAddressBody = ReadValueFromJson.readValueFromJson(filepath, "accounts");
		address = account.createAccount(validAddressBody, secretKey);
		Reporter.log("Verify create Account with Valid data");
	}

	@Test(dependsOnMethods = "verifyTokenWithValidTimestamp")
	public void createAccountWithInvalidAuthorizationkey() {
		String secretKey = "AMB_TOKEN p" + validAMB_Token;
		String validAddressBody = ReadValueFromJson.readValueFromJson(filepath, "accounts");
		Assert.assertTrue(account.createAccountwithInvalidData(validAddressBody, secretKey, 401,
				responseBodyWithInvalidAuthorization), "Error..!! Some error while creating account With Invalid Authorization");
		Reporter.log("Verify Create Token with invalid Authorization key.");
	}

	@Test
	public void createAccountWithoutAuthorizationKey() {
		String validAddressBody = ReadValueFromJson.readValueFromJson(filepath, "accounts");
		Assert.assertTrue(account.createAccountWithoutAuthorizationKey(validAddressBody, 401,
				responseBodyWithoutAuthorizationKey),"Error..!! Some error while creating account Without Authorozation key");
		Reporter.log("Verify create Account Without Authorization Key");
	}

	@Test(dependsOnMethods = "verifyTokenWithValidTimestamp")
	public void createAccountwithoutContentType() {
		String secretKey = "AMB_TOKEN " + validAMB_Token;
		String validAddressBody = ReadValueFromJson.readValueFromJson(filepath, "accounts");
		Assert.assertTrue(account.createAccountwithoutContentType(validAddressBody, secretKey, 400,
				responseBodyWithoutContentType), "Error..!! Some error while creating account Without content-Type");
		Reporter.log("Verify create Account without Content Type");

	}
	
	@Test(dependsOnMethods = "verifyTokenWithValidTimestamp")
	public void createAccountwithExtraKeyWord() {
		String secretKey = "AMB_TOKEN " + validAMB_Token;
		String validAddressBody = ReadValueFromJson.readValueFromJson(filepath, "accounts");
		Assert.assertTrue(account.createAccountwithoutContentType(validAddressBody, secretKey, 400,
				responseBodyWithoutContentType), "Error..!! Some error while creating account with extra keyword.");
		Reporter.log("Verify create Account with extra keyword.");
	}
	
	@Test(dependsOnMethods = "createAccount")
	public void verifyFindAccountWithAccount() {
		String secretKey = "AMB_TOKEN " + validAMB_Token;
		Assert.assertEquals(account.findCreatedAccount(address, secretKey), address);
		Reporter.log("Verify find account with valid data.");
	}
	
	@Test(dependsOnMethods = "createAccount")
	public void verifyFindAccountWithInvalidAccount() {
		String invalidAssress=address+"lk";
		String responseBody="{\"reason\":\"Entity not found: Account "+invalidAssress+" not found.\"}";
		String secretKey = "AMB_TOKEN " + validAMB_Token;
		Assert.assertTrue(account.findCreatedAccountWithInvalidData(invalidAssress, secretKey, 404, responseBody));
		Reporter.log("Verify find account with invalid account.");
	}
	
	@Test(dependsOnMethods = "createAccount")
	public void verifyFindAccountWithInvalidAuthorization() {
		String secretKey = "AMB_TOKEN p" + validAMB_Token;
		Assert.assertTrue(account.findCreatedAccountWithInvalidData(address, secretKey, 401, responseBodyWithInvalidAuthorization));
		Reporter.log("Verify find account with invalid authorization.");
	}
	
	@Test(dependsOnMethods = "createAccount")
	public void verifyFindAccountWithoutAuthorization() {
		Assert.assertTrue(account.findCreatedAccountWithoutAuthorization(address, 401, responseBodyWithoutAuthorizationKey));
		Reporter.log("Verify find account without authorization.");
	}
	
	@Test(dependsOnMethods = "createAccount")
	public void verifyFindAccountWithInvalidParameter() {
		String invalidAssress="?access/"+address;
		String responseBody="{\"reason\":\"Invalid data: Some fields ("+ invalidAssress +") are outside of the allowed set of fields (accessLevel,page,perPage,registeredBy)\"}";
		String secretKey = "AMB_TOKEN " + validAMB_Token;
		Assert.assertTrue(account.findCreatedAccountWithInvalidData(invalidAssress, secretKey, 400, responseBody));
		Reporter.log("Verify find account with invalid Parameter.");
	}
	
	@Test(dependsOnMethods = "verifyTokenWithValidTimestamp")
	public void verifyFindAccountWithvalidParameter() {
		String secretKey = "AMB_TOKEN " + validAMB_Token;
		Assert.assertTrue(account.findCreatedAccountWithvalidParameter(secretKey, 200, 0));
		Reporter.log("Verify find account with valid Parameter.");
	}
	
	@Test
	public void verifyFindAccountWithParameterAndWithoutAuthorization() {
		String secretKey = "";
		Assert.assertTrue(account.findCreatedAccountWithvalidParameter(secretKey, 401, 0));
		Reporter.log("Verify find account without authorization.");
	}
	
	@Test(dependsOnMethods = "verifyTokenWithValidTimestamp", dataProvider="InvalidParameter")
	public void verifyFindAccountWithInvalidAccountParameter(String parameter) {
		String secretKey = "AMB_TOKEN " + validAMB_Token;
		Assert.assertTrue(account.findCreatedAccountWithInvalidParameter(secretKey, 400, parameter));
		Reporter.log("Verify find account with valid Parameter.");
	}
	
	@Test(dependsOnMethods = "verifyTokenWithValidTimestamp")
	public void verifyFindAccountWithNegetiveParameter() {
		String secretKey = "AMB_TOKEN " + validAMB_Token;
		Assert.assertTrue(account.findCreatedAccountWithvalidParameter(secretKey, 400, -12));
		Reporter.log("Verify find account without authorization.");
	}
	
	
	@DataProvider(name = "InvalidParameter")	 
	  public static Object[][] credentials() {	 
	        return new Object[][] { {"test" }, { "0test" }};
	  }
}
