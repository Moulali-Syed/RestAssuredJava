package day3;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class SpecBuilderTest {
	public static void main(String[] args) {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		
		AddPlacePojo p = new AddPlacePojo();
		p.setAccuracy(50);
		p.setAddress("29,side layout , cohen 09");
		p.setLanguage("Frnch-IN");
		p.setPhoneNumber("(+91) 1234578921");
		p.setWebsite("https://rahulshettyacademy.com");
		p.setName("Frontline house");
		List<String> myList = new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
		p.setTypes(myList);
		
		LocationPojo l = new LocationPojo();
		l.setLat(-38.38);
		l.setLng(33.427);
		p.setLocation(l);
		
		
		//create request SpecBuilder
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123")
		.setContentType(ContentType.JSON).build();
		
		ResponseSpecification resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		Response res = given().spec(req).body(p)
		.when().post("/maps/api/place/add/json")
		.then().spec(resspec).extract().response();
		
		String responseString = res.asString();
		System.out.println(responseString);
	}
}
