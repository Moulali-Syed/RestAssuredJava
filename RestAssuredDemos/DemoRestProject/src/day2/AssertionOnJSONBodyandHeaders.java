package day2;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import files.Payload;
import io.restassured.RestAssured;

public class AssertionOnJSONBodyandHeaders {

	public static void main(String[] args) {
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(Payload.AddPlace())
		.when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP"))
		.header("server", "Apache/2.4.41 (Ubuntu)");
		
		
		
		//assertions should be placed after then 
		//1.asserting on body - body("key", equalTo("Value")) - import the static org.hamcrest.Matchers.*
		//2.asserting on headers - header("key","value")

		
		//the payload we are passing in body can be sent from a different file , instead of directly giving in the same file body
		//create a package and a file payload.java with a static method and use that inside the body className.methodName
	}

}
