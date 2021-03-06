package day2;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JsonPath js = new JsonPath(Payload.CoursePrice());

		// 1.print number of courses returned by API
		// size should be used on array only
		int count = js.getInt("courses.size()");
		System.out.println(count);

		// 2.print price
		int price = js.getInt("dashboard.purchaseAmount");
		System.out.println(price);

		// 3.Print Title of the first course
		String titleFirstCourse = js.get("courses[0].title");
		System.out.println(titleFirstCourse);

		// 4. Print All course titles and their respective Prices
		// we can get total courses using the size
		for (int i = 0; i < count; i++) {
			String courseTitles = js.get("courses[" + i + "].title");
			System.out.println(js.get("courses[" + i + "].price").toString());
			System.out.println(courseTitles);

		}
		
		//5. Print no of copies sold by RPA Course
		System.out.println("print no of courses sold by RPA course");
		for(int i=0;i<count;i++) {
			String courseTitles = js.get("courses[" + i + "].title");
			if(courseTitles.equalsIgnoreCase("RPA")) {
				int copies = js.get("courses["+i+"].copies");
				System.out.println(copies);
				break;
			}
		}
		
		

	}

}
