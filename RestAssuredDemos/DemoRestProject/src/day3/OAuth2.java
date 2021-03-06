package day3;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojoDay3.Api;
import pojoDay3.GetCourse;
import pojoDay3.WebAutomation;

public class OAuth2 {

	public static void main(String[] args) throws InterruptedException {

		System.setProperty("webdriver.chrome.driver", "C:\\Testing\\Drivers\\chromedriver_win32\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		// login using the gmail/fb
		driver.get(
				"https://accounts.google.com/signin/v2/challenge/pwd?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&auth_url=https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fv2%2Fauth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https%3A%2F%2Frahulshettyacademy.com%2FgetCourse.php&flowName=GeneralOAuthFlow&cid=1&navigationDirection=forward&TL=AM3QAYYEZdMTjUR83LXlxjUXDoQICMFrjec-DpPFGDgStTyHa6Q5Y3Oc_yAdfAb2");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("apple123");
		Thread.sleep(2000);

		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("password");
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);

		// get the url and extract the code
		String url = driver.getCurrentUrl();
//		in new google chrome version automatic chrome invocation and getting code from it is blocked should be done manually up to this step
//		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWg922To0mrjFgQ3y9M1qXFlndcRT-FtCndS82QrpIgLnZm0OoPt40Pie4o3GAe3jA&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=1&prompt=consent"
		String partialcode = url.split("code=")[1];
		String code = partialcode.split("&scope")[0];
		System.out.println(code);

		// send the code and extract access token
		String accessTokenResponse = given().urlEncodingEnabled(false).queryParam("code", code)
				.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParam("grant_type", "autorization_code").when().log().all()
				.post("https://www.googleapis.com/oauth2/v4/token").asString();

		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");

		// send the access token
		String response = given().queryParam("access_token", accessToken).when().log().all()
				.get("https://rahulshettyacademy.com/getCourse.php").asString();

		System.out.println(response);
		
		
		
		//deserialization - use get methods
		GetCourse gc =  given().queryParam("access_token",accessToken).expect().defaultParser(Parser.JSON)
							.when()
							.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
																					//className.class
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		
		
		//quereying the nested json
		gc.getCourses().getApi().get(1).getCourseTitle();
		
		//iterating and finding the required match
		List<Api> apiCourses = gc.getCourses().getApi();
		for(int i=0;i<apiCourses.size();i++) {
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(apiCourses.get(i).getPrice());
			}
		}
		
		
		String[] courseTitles = {"Selenium Webdriver Java","Cypress","Protractor"};
		ArrayList<String> a = new ArrayList<String>();
		//print all course names of WebAutomation
		List<WebAutomation> w = gc.getCourses().getWebAutomation();
		for(int j=0;j<w.size();j++) {
			a.add(w.get(j).getCourseTitle());
			System.out.println(w.get(j).getCourseTitle());
		}
		
		List<String> expectedList = Arrays.asList(courseTitles);
		Assert.assertTrue(a.equals(expectedList));
		
	}

}

//urlEncodingEnabled(false)  -  should be used to stop automatic conversion of codes

/*

 public class Message {
	private String message;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
 
 
 //create java object
 Message m = new Message();
 m.setMessage("Hello");
 
 
 //rest assured
 Message msg = new Message();
 msg.setMessage("My message");
 given().
 body(m).
 when().
 post("/message");
 
 //run time request creation
 
 
 //json
 {
	 "message":"Hello"
 }

*/