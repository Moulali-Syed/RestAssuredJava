package demo;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;//this import should be added manually as it is static

public class Basics {

	public static void main(String[] args) {
		
		//validate if Add place API is working as expected
		
		//Rest API works on 3 things
		
		//Given - all input details
		//When - Submit API details
		//Then - Validate the response
		
		//step1: set the baseURL
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		//step2:
		//given will take what all details we need to provide [all input details in when]
		//example query params , header - content type-application/json ,authenticate/
		// authorization details and the json request inside 
		//body as string 
		
		//when - this will take the resource and http method [all input details in when except resource and http method]
		
		//then - any validations will be in then , like assert statements
		
		//step3: to see all the request ,response we can use log().all() after the given() , then()
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "  \"location\": {\r\n"
				+ "    \"lat\": -38.383494,\r\n"
				+ "    \"lng\": 33.427362\r\n"
				+ "  },\r\n"
				+ "  \"accuracy\": 50,\r\n"
				+ "  \"name\": \"Frontline house\",\r\n"
				+ "  \"phone_number\": \"(+91) 983 893 3937\",\r\n"
				+ "  \"address\": \"29, side layout, cohen 09\",\r\n"
				+ "  \"types\": [\r\n"
				+ "    \"shoe park\",\r\n"
				+ "    \"shop\"\r\n"
				+ "  ],\r\n"
				+ "  \"website\": \"http://google.com\",\r\n"
				+ "  \"language\": \"French-IN\"\r\n"
				+ "}\r\n"
				+ "")
		.when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200);
		
		//on pasting json in string inside body if escape sequences are not generated automatically
		//go to window-preferences-Java-Editor-Typing - check mark
		//escape text when pasting into string literal
		
	}
}
