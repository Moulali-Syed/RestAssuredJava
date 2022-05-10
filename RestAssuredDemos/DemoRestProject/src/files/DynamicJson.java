package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {

	@Test(dataProvider="BooksData")
	public void addBook(String isbn,String aisle) {
		RestAssured.baseURI="http://216.10.245.166";
		String response = given().header("Content-Type","application/json")
		.body(Payload.addBook(isbn,aisle)).
		when().
		post("/Library/Addbook.php")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath js = ReusableMethods.rawToJson(response);
		String id = js.getString("ID");
		System.out.println(id);
		
		
		//in real time we should delete the book so that it can run daily
	}
	
	
	@DataProvider(name="BooksData")
	public Object[][] getData() {
		//collections of arrays = multi dimensional array
		return new Object[][] {{"sffaa","349193"},{"dvvvfv","93949"},{"svvdd","1384"}};
	}
}
