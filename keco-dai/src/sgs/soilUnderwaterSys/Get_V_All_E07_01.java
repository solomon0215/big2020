package sgs.soilUnderwaterSys;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.DBConnection;
import common.JsonParser;

public class Get_V_All_E07_01 {

	//토양지하수 정보시스템 - 토양측정망
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		if (args.length == 1) {

			Connection conn = null; // DB연결된 상태(세션)을 담은 객체
			PreparedStatement pstm = null; // SQL 문을 나타내는 객체
			ResultSet rs = null; // 쿼리문을 날린것에 대한 반환값을 담을 객체
			
			//sql 쿼리 에러시 로그 확인용 변수
			String cf = "N";

			try {

				conn = DBConnection.getOraConnection("sgs");

				String query = DBConnection.getScienceProperty("sgs_oracle_sgs02_query");
				System.out.println("query :::" + query);

				pstm = conn.prepareStatement(query);
				pstm.setFetchSize(100);

				System.out.println("start query");
				rs = pstm.executeQuery();
				System.out.println("done query");

				rs.setFetchSize(100);

				if (args[0].equals("_tset")) {

					while (rs.next()) {

						String SPOT_STD_CODE = " ";
						String CL_LARGE = " ";

						SPOT_STD_CODE = rs.getString(1);
						CL_LARGE = rs.getString(2);

						System.out.println("SPOT_STD_CODE::" + SPOT_STD_CODE + "::CL_LARGE::" + CL_LARGE);

					}

					System.out.println("SGS_02 SELECT 프로세스 종료.");

				} else {

					File file = null;

					file = new File(args[0]);

					while (rs.next()) {

						String SPOT_STD_CODE = " "; // 지점표준코드
						String CL_LARGE = " "; // 특성분류_대분류
						String CL_MIDDLE = " "; // 특성분류_중분류
						String CL_SMALL = " "; // 특성분류_소분류
						String JIMOK_CD = " "; // 지목코드
						String YEAR = " "; // 연도
						String PT_NO = " "; // 조사지점(번호)
						String SPOT_NM = " "; // 고유명칭
						String PURPOSE_NM = " "; // 측정목적별 분류
						String ADDR_ORIGINAL = " "; // 본래주소
						String JIMOK_NM = " "; // 지목별분류
						String AREA = " "; // 면적
						String IC_00005 = " "; // Cd
						String IC_00020 = " "; // Cu
						String IC_00006 = " "; // As
						String IC_00008 = " "; // Hg
						String IC_00011 = " "; // Pb
						String IC_00012 = " "; // Cr+6
						String IC_00021 = " "; // Zn
						String IC_00022 = " "; // Ni
						String IC_00023 = " "; // 불소
						String IC_00009 = " "; // 유기인
						String IC_00024 = " "; // PCBs
						String IC_00007 = " "; // CN
						String IC_00010 = " "; // 페놀류
						String IC_00016 = " "; // 벤젠
						String IC_00017 = " "; // 톨루엔
						String IC_00018 = " "; // 에틸벤젠
						String IC_00019 = " "; // 크실렌
						String IC_00025 = " "; // TPH
						String IC_00013 = " "; // TCE
						String IC_00014 = " "; // PCE
						String IC_00026 = " "; // 벤조(a)피렌
						String IC_00001 = " "; // pH
						String BIGO = " "; // 비고

						SPOT_STD_CODE = JsonParser.colWrite_String_eic(rs.getString(1));
						CL_LARGE = JsonParser.colWrite_String_eic(rs.getString(2));
						CL_MIDDLE = JsonParser.colWrite_String_eic(rs.getString(3));
						CL_SMALL = JsonParser.colWrite_String_eic(rs.getString(4));
						JIMOK_CD = JsonParser.colWrite_String_eic(rs.getString(5));
						YEAR = JsonParser.colWrite_String_eic(rs.getString(6));
						PT_NO = JsonParser.colWrite_String_eic(rs.getString(7));
						SPOT_NM = JsonParser.colWrite_String_eic(rs.getString(8));
						PURPOSE_NM = JsonParser.colWrite_String_eic(rs.getString(9));
						ADDR_ORIGINAL = JsonParser.colWrite_String_eic(rs.getString(10));
						JIMOK_NM = JsonParser.colWrite_String_eic(rs.getString(11));
						AREA = JsonParser.colWrite_String_eic(rs.getString(12));
						IC_00005 = JsonParser.colWrite_String_eic(rs.getString(13));
						IC_00020 = JsonParser.colWrite_String_eic(rs.getString(14));
						IC_00006 = JsonParser.colWrite_String_eic(rs.getString(15));
						IC_00008 = JsonParser.colWrite_String_eic(rs.getString(16));
						IC_00011 = JsonParser.colWrite_String_eic(rs.getString(17));
						IC_00012 = JsonParser.colWrite_String_eic(rs.getString(18));
						IC_00021 = JsonParser.colWrite_String_eic(rs.getString(19));
						IC_00022 = JsonParser.colWrite_String_eic(rs.getString(20));
						IC_00023 = JsonParser.colWrite_String_eic(rs.getString(21));
						IC_00009 = JsonParser.colWrite_String_eic(rs.getString(22));
						IC_00024 = JsonParser.colWrite_String_eic(rs.getString(23));
						IC_00007 = JsonParser.colWrite_String_eic(rs.getString(24));
						IC_00010 = JsonParser.colWrite_String_eic(rs.getString(25));
						IC_00016 = JsonParser.colWrite_String_eic(rs.getString(26));
						IC_00017 = JsonParser.colWrite_String_eic(rs.getString(27));
						IC_00018 = JsonParser.colWrite_String_eic(rs.getString(28));
						IC_00019 = JsonParser.colWrite_String_eic(rs.getString(29));
						IC_00025 = JsonParser.colWrite_String_eic(rs.getString(30));
						IC_00013 = JsonParser.colWrite_String_eic(rs.getString(31));
						IC_00014 = JsonParser.colWrite_String_eic(rs.getString(32));
						IC_00026 = JsonParser.colWrite_String_eic(rs.getString(33));
						IC_00001 = JsonParser.colWrite_String_eic(rs.getString(34));
						BIGO = JsonParser.colWrite_String_eic(rs.getString(35));


						// 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(SPOT_STD_CODE);
							pw.write("|^");
							pw.write(CL_LARGE);
							pw.write("|^");
							pw.write(CL_MIDDLE);
							pw.write("|^");
							pw.write(CL_SMALL);
							pw.write("|^");
							pw.write(JIMOK_CD);
							pw.write("|^");
							pw.write(YEAR);
							pw.write("|^");
							pw.write(PT_NO);
							pw.write("|^");
							pw.write(SPOT_NM);
							pw.write("|^");
							pw.write(PURPOSE_NM);
							pw.write("|^");
							pw.write(ADDR_ORIGINAL);
							pw.write("|^");
							pw.write(JIMOK_NM);
							pw.write("|^");
							pw.write(AREA);
							pw.write("|^");
							pw.write(IC_00005);
							pw.write("|^");
							pw.write(IC_00020);
							pw.write("|^");
							pw.write(IC_00006);
							pw.write("|^");
							pw.write(IC_00008);
							pw.write("|^");
							pw.write(IC_00011);
							pw.write("|^");
							pw.write(IC_00012);
							pw.write("|^");
							pw.write(IC_00021);
							pw.write("|^");
							pw.write(IC_00022);
							pw.write("|^");
							pw.write(IC_00023);
							pw.write("|^");
							pw.write(IC_00009);
							pw.write("|^");
							pw.write(IC_00024);
							pw.write("|^");
							pw.write(IC_00007);
							pw.write("|^");
							pw.write(IC_00010);
							pw.write("|^");
							pw.write(IC_00016);
							pw.write("|^");
							pw.write(IC_00017);
							pw.write("|^");
							pw.write(IC_00018);
							pw.write("|^");
							pw.write(IC_00019);
							pw.write("|^");
							pw.write(IC_00025);
							pw.write("|^");
							pw.write(IC_00013);
							pw.write("|^");
							pw.write(IC_00014);
							pw.write("|^");
							pw.write(IC_00026);
							pw.write("|^");
							pw.write(IC_00001);
							pw.write("|^");
							pw.write(BIGO);
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						// System.out.println("진행도 :::" +
						// Integer.toString(rs.getRow()) + "/" +
						// Integer.toString(rowCount) + " 건");

						if(rs.getRow() % 10000 == 0){
							System.out.println("진행도 :::" + Integer.toString(rs.getRow()) + "번째 줄");
						}
					}

					if (file.exists()) {
						System.out.println("파일이 생성되었습니다.");
					} else {
						System.out.println("파일이 생성되지 않았습니다.");
					}

					System.out.println("SGS_02 SELECT 파일 생성 프로세스 종료.");

				}

			} catch (SQLException sqle) {

				System.out.println("SELECT문에서 예외 발생");
				
				cf = "Y";
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
				
				//쿼리에서 에러 발생시에는 종료로 빠진다.
				if(cf.equals("Y")) {
					System.exit(-1);
				}

			}

		} else {
			System.out.println("파라미터 개수 에러!!");
			System.exit(-1);
		}

	}

}
