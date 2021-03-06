package day3;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;
public class SerializeTest {
	
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
		
		
		Response res = given().queryParam("key","qaclick123").body(p)
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).extract().response();
		
		String responseString = res.asString();
		System.out.println(responseString);
	}

}
