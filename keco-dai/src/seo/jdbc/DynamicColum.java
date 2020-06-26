package seo.jdbc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import common.DBConnection;
import common.JsonParser;

/**
 * @ClassName	:	seo.jdbc.DynamicColum.java
 * @SinceDate	:	2020. 6. 25.
 * @Desc		:	Dynamic Column 적용 Test 클래스
 * ----------------------------------------------------------
 * ModifyDate				User				Desc
 * ----------------------------------------------------------
 * 2020. 6. 25.				서민재				create
 * ----------------------------------------------------------
 */
public class DynamicColum {

	/**
	 * @Method	:	"main" 메소드
	 * @Since	:	2020. 6. 25.
	 * @Param	:	경로/파일
	 * @Return	:	void
	 * @Desc	:	Test로 떨어 뜨리는 메인
	 */
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Connection conn = null; // DB연결된 상태(세션)을 담은 객체
		PreparedStatement pstm = null; // SQL 문을 나타내는 객체
		ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
		try {

			conn = DBConnection.getMysqlConnection("seo");

			String query = DBConnection.getProperty("seo_mysql_query_seo01");
			System.out.println("query :::" + query);

			pstm = conn.prepareStatement(query);
			pstm.setFetchSize(100);

			System.out.println("start query");
			rs = pstm.executeQuery();
			System.out.println("done query");

			rs.setFetchSize(100);
			
			// 동적 column 숫자를 사용하기 위한 메타데이터 가져오기 및 컬럼수
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			
			File file = null;

			file = new File("TIF_SEO_01.dat");
			
			while (rs.next()) {
				
				// 파일에 쓰기
				try {
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

					for(int i = 1; i<=colCount; i ++ ) {
						if(i == colCount) {
							pw.write(JsonParser.colWrite_String_eic(rs.getString(i)));
							pw.println();
							pw.flush();
							pw.close();
							break;
						}else {
							pw.write(JsonParser.colWrite_String_eic(rs.getString(i)));
							pw.write("|^");
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

				// System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "/" + Integer.toString(rowCount) + " 건");
				if(rs.getRow() % 10000 == 0){
					System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "번째 줄");
				}

			}

			if (file.exists()) {
				System.out.println("파일이 생성되었습니다.");
			} else {
				System.out.println("파일이 생성되지 않았습니다.");
			}
			System.out.println("TMD_01 SELECT 파일 생성 프로세스 종료.");


		} catch (SQLException sqle) {
			System.out.println("SELECT문에서 예외 발생");
			sqle.printStackTrace();
		} finally {
			// DB 연결을 종료한다.
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstm != null) {
					pstm.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}
}
