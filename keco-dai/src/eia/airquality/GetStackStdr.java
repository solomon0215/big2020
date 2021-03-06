package eia.airquality;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.JsonParser;
//import common.TransSftp;

public class GetStackStdr {

	// 대기질 정보 조회 - 연돌기준속성조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 필수 파라미터로 사업 코드를 넣으면 그 사업 코드에 대한 데이터를 파싱해서 출력한다. 사업코드는 1개만 넣을 수 있다.
				if (args.length == 1) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간
					String mgtNo = args[0];

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty("airquality_getStackStdr_url");
					String service_key = JsonParser.getProperty("airquality_service_key");

					// step 1.파일의 작성

					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_01.dat");
					
					try {
						
						PrintWriter pw = new PrintWriter(
								new BufferedWriter(new FileWriter(file, true)));

						pw.flush();
						pw.close();

					} catch (IOException e) {
						e.printStackTrace();
					}

					//String json = "";
					
					// step 3.필요에 맞게 파싱
					
					//json = JsonParser.parseEiaJson(service_url, service_key, mgtNo);
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					// 2020.06.02 : 빈 Json을 리턴하도록 롤백
					// 2020.06.05 : String 리턴으로 잡았더니 에러 남.. JSONObject리턴으로 수정하고, 해당 메서드에 빈 json 로직을 넣음
					/*if(json.indexOf("</") > -1){
						System.out.print("공공데이터 서버 비 JSON 응답 : mgtNo :" + mgtNo);
						json ="{\"response\": {\"header\": {\"resultCode\": \"03\",\"resultMsg\": \"NODATA_ERROR\"}}}";
					}*/

					JSONObject obj = JsonParser.parseEiaJson_obj(service_url, service_key, mgtNo);
					JSONObject response = (JSONObject) obj.get("response");

					// response는 결과값 코드와 메시지를 가지는 header와 데이터 부분인 body로 구분
					JSONObject header = (JSONObject) response.get("header");
					JSONObject body = (JSONObject) response.get("body");

					String resultCode = header.get("resultCode").toString().trim();
					String resultMsg = header.get("resultMsg").toString().trim();

					if (resultCode.equals("00")) {
						

						JSONArray stacks = (JSONArray) body.get("stacks");

						for (int i = 0; i < stacks.size(); i++) {

							JSONObject stack = (JSONObject) stacks.get(i);

							Set<String> key = stack.keySet();

							Iterator<String> iter = key.iterator();

							String stackNm = " "; // 연돌명
							String adres = " "; // 주소
							String xcnts = " "; // X좌표
							String ydnts = " "; // Y좌표
							String stackHg = " "; // 연돌높이
							String stackDm = " "; // 연돌직경
							String stackIndm = " "; // 연돌내경
							String stackTp = " "; // 배출가스온도
							String dgsnStdrPm10Val = " "; // 미세먼지(10) 설계기준
							String dgsnStdrPm25Val = " "; // 미세먼지(2.5) 설계기준
							String dgsnStdrNo2Val = " "; // 이산화질소 설계기준
							String dgsnStdrSo2Val = " "; // 아황산가스 설계기준
							String dgsnStdrCoVal = " "; // 일산화탄소 설계기준
							String dscamtPm10Val = " "; // 미세먼지(10) 배출량
							String dscamtPm25Val = " "; // 미세먼지(2.5) 배출량
							String dscamtNo2Val = " "; // 이산화질소 배출량
							String dscamtSo20Val = " "; // 아황산가스 배출량
							String dscamtCoVal = " "; // 일산화탄소 배출량

							while (iter.hasNext()) {
								String keyname = iter.next();

								if (keyname.equals("stackNm")) {
									stackNm = stack.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("adres")) {
									adres = stack.get(keyname).toString().replaceAll("(\r\n|\r|\n|\n\r)", " ").trim();
								}
								if (keyname.equals("xcnts")) {
									xcnts = stack.get(keyname).toString().trim();

									// 시분초 표기일 경우 drgree 표기로 전환
									if ((xcnts.indexOf("°") > -1)) {
										xcnts = JsonParser.dmsTodecimal_split(xcnts);
									}

								}
								if (keyname.equals("ydnts")) {
									ydnts = stack.get(keyname).toString().trim();

									// 시분초 표기일 경우 drgree 표기로 전환
									if ((ydnts.indexOf("°") > -1)) {
										ydnts = JsonParser.dmsTodecimal_split(xcnts);
									}
								}
								if (keyname.equals("stackHg")) {
									stackHg = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("stackDm")) {
									stackDm = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("stackIndm")) {
									stackIndm = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("stackTp")) {
									stackTp = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("dgsnStdrPm10Val")) {
									dgsnStdrPm10Val = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("dgsnStdrPm25Val")) {
									dgsnStdrPm25Val = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("dgsnStdrNo2Val")) {
									dgsnStdrNo2Val = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("dgsnStdrSo2Val")) {
									dgsnStdrSo2Val = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("dgsnStdrCoVal")) {
									dgsnStdrCoVal = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("dscamtPm10Val")) {
									dscamtPm10Val = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("dscamtPm25Val")) {
									dscamtPm25Val = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("dscamtNo2Val")) {
									dscamtNo2Val = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("dscamtSo20Val")) {
									dscamtSo20Val = stack.get(keyname).toString().trim();
								}
								if (keyname.equals("dscamtCoVal")) {
									dscamtCoVal = stack.get(keyname).toString().trim();
								}

							}

							// step 4. 파일에 쓰기
							try {
								PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

								pw.write(mgtNo); // 사업코드
								pw.write("|^");
								pw.write(stackNm); // 연돌명
								pw.write("|^");
								pw.write(adres.replaceAll("(\r\n|\r|\n|\n\r)", " ")); // 주소
								pw.write("|^");
								pw.write(xcnts); // X좌표
								pw.write("|^");
								pw.write(ydnts); // Y좌표
								pw.write("|^");
								pw.write(stackHg); // 연돌높이
								pw.write("|^");
								pw.write(stackDm); // 연돌직경
								pw.write("|^");
								pw.write(stackIndm); // 연돌내경
								pw.write("|^");
								pw.write(stackTp); // 배출가스온도
								pw.write("|^");
								pw.write(dgsnStdrPm10Val); // 미세먼지(10) 설계기준
								pw.write("|^");
								pw.write(dgsnStdrPm25Val); // 미세먼지(2.5) 설계기준
								pw.write("|^");
								pw.write(dgsnStdrNo2Val); // 이산화질소 설계기준
								pw.write("|^");
								pw.write(dgsnStdrSo2Val); // 아황산가스 설계기준
								pw.write("|^");
								pw.write(dgsnStdrCoVal); // 일산화탄소 설계기준
								pw.write("|^");
								pw.write(dscamtPm10Val); // 미세먼지(10) 배출량
								pw.write("|^");
								pw.write(dscamtPm25Val); // 미세먼지(2.5) 배출량
								pw.write("|^");
								pw.write(dscamtNo2Val); // 이산화질소 배출량
								pw.write("|^");
								pw.write(dscamtSo20Val); // 아황산가스 배출량
								pw.write("|^");
								pw.write(dscamtCoVal); // 일산화탄소 배출량
								pw.println();
								pw.flush();
								pw.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						System.out.println("parsing complete!");

						// step 5. 대상 서버에 sftp로 보냄 (대상 파일 경로, 목적지 폴더명)

						//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_01.dat", "EIA");
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
