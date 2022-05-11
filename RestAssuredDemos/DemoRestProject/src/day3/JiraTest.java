package day3;

import static io.restassured.RestAssured.*;

import java.io.File;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
public class JiraTest {

	public static void main(String[] args) {
		
		
		RestAssured.baseURI = "http://localhost:8080";
		
		//importance of session filter cookie in rest assured code
		//login scenario
		//we can extract the session id either from JsonPath and then extracting from it
		// or we can use SessionFilter class object and pass it in given as filter(session)
		//we can pass this session for subsequent operations as well
		SessionFilter session = new SessionFilter();
		String response = given().header("Content-Type","application/json").body("").log().all().filter(session)
		.when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
		
		
		// lets first add a comment to existing issue
		
		// if something in {} it is path parameter , and ? it is query parameter , passing session using the sessionfilter
		String addCommentResponse = given().pathParam("key","10101").log().all().header("Content-Type","application/json").body("").filter(session)
		.when().post("/rest/api/2/issue/{key}/comment").then().log().all().assertThat().statusCode(201).extract().response().asString();
		JsonPath js = new JsonPath(addCommentResponse);
		String commentId = js.getString("id");
		//whatever we pass in pathParam as key value will fall at this {} matching pathParam name
		//here above we r passing pathParam key in- pathParam("key","10101") , which at run time passed to the {key}
		// in post("/rest/api/2/issue/{key}/comment")
		
		
		//3.add an attachment to rest api using multipart method in rest assured
		//first in jira page - on top right - go to settings icon - system - attachments - Set allow attachments to ON
		//add an attachment as per document
		given().header("X-Atlassian-Token","no-check").filter(session).pathParam("key","10101")
		.header("Content-Type","multipart/form-data")
		.multiPart("file",new File("jira.txt"))
		.when().post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
		
		
		//4 . integrating query params and path params in single test to restrict results
		//get issue
		String issueDetails = given().filter(session).pathParam("key","10101").queryParam("fields","comment").
		log().all(). when().get("/rest/api/2/issue/{key}").then()
		.log().all().extract().response().asString();
		System.out.println(issueDetails);
		
		JsonPath js1 = new JsonPath(issueDetails);
		int commentsCount = js1.getInt("fields.comment.comments.size()");
		for(int i=0;i<commentsCount;i++) {
			String commentIdIssue = js1.get("fields.comment.comments["+i+"].id").toString();
			if(commentIdIssue.equalsIgnoreCase(commentId)) {
				String message = js1.get("fields.comment.comments["+i+"].body").toString();	
			}
		}
	}

}
