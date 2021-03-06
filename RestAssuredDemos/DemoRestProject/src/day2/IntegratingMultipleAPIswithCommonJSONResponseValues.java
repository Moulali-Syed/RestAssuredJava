package day2;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;

import files.Payload;
import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class IntegratingMultipleAPIswithCommonJSONResponseValues {
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
		
		
		//update the place , use the put to update
		String newAddress = "Summer Walk, Africa";//updating place using a variable
		
		given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+place_id+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		
		//Get Place - we need not give any header for get method
		String getPlaceResponse = given().log().all().queryParam("key","qaclick123")
		.queryParam("place_id", place_id)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		
		//we can use a reusable method instead of JsonPath directly , create a class ReusableMethods with a static method rawToJson 
		//inside files package
		JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
		//JsonPath js1 = new JsonPath(getPlaceResponse);
		String actualAddress = js1.getString("address");
		System.out.println(actualAddress);
		
		//testng assertion
		Assert.assertEquals(actualAddress, "Summer Walk, Africa");
		
		
	}
}
