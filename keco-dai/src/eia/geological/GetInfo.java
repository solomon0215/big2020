package eia.geological;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONObject;

import common.JsonParser;
//import common.TransSftp;

public class GetInfo {

	// 지형지질 정보 조회 - 개요속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("geological_getInfo_url");
					String service_key = JsonParser.getProperty("geological_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_21.dat");
					
					//String json = "";

					//json = JsonParser.parseEiaJson(service_url, service_key, mgtNo);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					// 2020.06.02 : 빈 Json을 리턴하도록 롤백
					// 2020.06.05 : String 리턴으로 잡았더니 에러 남.. JSONObject리턴으로 수정하고, 해당 메서드에 빈 json 로직을 넣음
					/*if(json.indexOf("</") > -1){
						System.out.print("공공데이터 서버 비 JSON 응답, mgtNo :" + mgtNo);
						json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";
					}*/

					// step 3.필요에 맞게 파싱

					JSONObject obj = JsonParser.parseEiaJson_obj(service_url, service_key, mgtNo);
					JSONObject response = (JSONObject) obj.get("response");

					// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
					JSONObject header = (JSONObject) response.get("header");
					JSONObject body = (JSONObject) response.get("body");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (resultCode.equals("00")) {
						
						

						Set<String> key = body.keySet();

						Iterator<String> iter = key.iterator();

						String coeQy = " "; // 절토량(땅깎기)
						String bkQy = " "; // 성토량(흙쌇기)
						String mcoeHg = " "; // 최대깎기고
						String unlmYn = " "; // 최대깎기고 무한사면 유무
						String mbkHg = " "; // 최대쌓기고
						String mcoeShg = " "; // 최대절토사면고
						String mbkShg = " "; // 최대성토사면고
						String tpgrphChangeInex = " "; // 지형변화지수
						String frtltySoilOcty = " "; // 비옥토 발생량
						String ridgeAnals = " "; // 능선분할분석
						String mbrewalHg = " "; // 최대옹벽 높이

						while (iter.hasNext()) {
							String keyname = iter.next();

							if (keyname.equals("coeQy")) {
								coeQy = body.get(keyname).toString().trim();
							}
							if (keyname.equals("bkQy")) {
								bkQy = body.get(keyname).toString().trim();
							}
							if (keyname.equals("mcoeHg")) {
								mcoeHg = body.get(keyname).toString().trim();
							}
							if (keyname.equals("unlmYn")) {
								unlmYn = body.get(keyname).toString().trim();
							}
							if (keyname.equals("mbkHg")) {
								mbkHg = body.get(keyname).toString().trim();
							}
							if (keyname.equals("mcoeShg")) {
								mcoeShg = body.get(keyname).toString().trim();
							}
							if (keyname.equals("mbkShg")) {
								mbkShg = body.get(keyname).toString().trim();
							}
							if (keyname.equals("tpgrphChangeInex")) {
								tpgrphChangeInex = body.get(keyname).toString().trim();
							}
							if (keyname.equals("frtltySoilOcty")) {
								frtltySoilOcty = body.get(keyname).toString().trim();
							}
							if (keyname.equals("ridgeAnals")) {
								ridgeAnals = body.get(keyname).toString().trim();
							}
							if (keyname.equals("mbrewalHg")) {
								mbrewalHg = body.get(keyname).toString().trim();
							}

						}

						// step 4. 파일에 쓰기
						try {
							PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

							pw.write(mgtNo); // 사업 코드
							pw.write("|^");
							pw.write(coeQy); // 절토량(땅깎기)
							pw.write("|^");
							pw.write(bkQy); // 성토량(흙쌇기)
							pw.write("|^");
							pw.write(mcoeHg); // 최대깎기고
							pw.write("|^");
							pw.write(unlmYn); // 최대깎기고 무한사면 유무
							pw.write("|^");
							pw.write(mbkHg); // 최대쌓기고
							pw.write("|^");
							pw.write(mcoeShg); // 최대절토사면고
							pw.write("|^");
							pw.write(mbkShg); // 최대성토사면고
							pw.write("|^");
							pw.write(tpgrphChangeInex); // 지형변화지수
							pw.write("|^");
							pw.write(frtltySoilOcty); // 비옥토 발생량
							pw.write("|^");
							pw.write(ridgeAnals); // 능선분할분석
							pw.write("|^");
							pw.write(mbrewalHg); // 최대옹벽 높이
							pw.println();
							pw.flush();
							pw.close();

						} catch (IOException e) {
							e.printStackTrace();
						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_21.dat", "EIA");

						long end = System.currentTimeMillis();
						System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

					} else if (resultCode.equals("03")) {
						System.out.println("data not exist!! mgtNo :" + mgtNo);
					} else {
						System.out.println("공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg
								+ "::mgtNo::" + mgtNo);
						//throw new Exception();
					}

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}


			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName() +", mgtNo :" + args[0]);
				System.exit(-1);
			}


	}

}
