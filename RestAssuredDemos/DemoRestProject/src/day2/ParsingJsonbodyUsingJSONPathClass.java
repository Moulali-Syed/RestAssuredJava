package day2;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class ParsingJsonbodyUsingJSONPathClass {
	public static void main(String[] args) {
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(Payload.AddPlace())
		.when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope",equalTo("APP"))
		.header("server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();
		
		
		//to store whole response in a string
		//use --> .extract().response().asString()  and store it in a string variable as above
		//lets remove the log().all() after then and try to print the stored response in string
		
		System.out.println(response);
		
		//To extract place_id from the response
		//we have JsonPath , create object for it and pass the response
		JsonPath js = new JsonPath(response);
		String place_id  = js.getString("place_id");//use this js.getString("keyHere")
		System.out.println(place_id);
		
	}
}
