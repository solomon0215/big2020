package wem.spWaterHarmfulSubstance;

import common.DBConnection;

/**
 * @ClassName	:	wem.spWaterHarmfulSubstance.PathTest.java
 * @SinceDate	:	2020. 6. 17.
 * @Desc		:	경로테스트를 위한 TEST클래스
 * ----------------------------------------------------------
 * ModifyDate				User				Desc
 * ----------------------------------------------------------
 * 2020. 6. 17.				박형준				create
 * ----------------------------------------------------------
 * 2020. 6. 18.				서민재				JavaDoc 추가
 * ----------------------------------------------------------
 */
public class PathTest {
	
	/**
	 * @Method	:	"main" 메소드
	 * @Since	:	2020. 6. 17.
	 * @Param	:	argument
	 * @Return	:	void
	 * @Desc	:	경로를 찍기위한  main
	 */
	public static void main(String[] args) {

		/*System.out.println("path :::" + System.getenv("USERNAME"));
		
		System.out.println("path2 :::" + System.getenv("APP_ROOT"));*/
		
		String Sciencequery = DBConnection.getScienceProperty("wem_oracle_wem01_query");
		System.out.println("Sciencequery :::" + Sciencequery);
		
		/*String query = DBConnection.getProperty("wem_oracle_wem01_query");
		System.out.println("query :::" + query);*/
		
		
	}

}
