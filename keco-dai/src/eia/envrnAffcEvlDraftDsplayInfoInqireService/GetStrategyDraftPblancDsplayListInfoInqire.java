package eia.envrnAffcEvlDraftDsplayInfoInqireService;

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

public class GetStrategyDraftPblancDsplayListInfoInqire {

	// 환경영향평가 초안공람 정보 서비스 - 초안공람 전략 환경영향평가 목록 정보 조회
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

			try {

				

				// 서비스 키만 요구함, 실행시 필수 매개변수 없음
				if (args.length == 0) {

					System.out.println("firstLine start..");
					long start = System.currentTimeMillis(); // 시작시간

					// step 0.open api url과 서비스 키.
					String service_url = JsonParser.getProperty(
							"envrnAffcEvlDraftDsplayInfoInqireService_getStrategyDraftPblancDsplayListInfoInqire_url");
					String service_key = JsonParser.getProperty("envrnAffcEvlDraftDsplayInfoInqireService_service_key");

					// step 1.파일의 작성
					File file = new File(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_37.dat");	

					// step 2. 전체 데이터 숫자 파악을 위해 페이지 수 1으로 파싱
					//String json = "";

					int pageNo = 1;
					int pageCount = 0;
					
					//json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(pageNo));
					
					//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
					//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
					//공통 클래스로 로직 빼 놓음
					// 2020.06.02 : 빈 Json을 리턴하도록 롤백
					// 2020.06.05 : String 리턴으로 잡았더니 에러 남.. JSONObject리턴으로 수정하고, 해당 메서드에 빈 json 로직을 넣음
					/*if(json.indexOf("</") > -1){
						System.out.print("공공데이터 서버 비 JSON 응답");
						json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
					}*/
					
					JSONObject count_obj = JsonParser.parseWatJson_obj(service_url, service_key, String.valueOf(pageNo));
					JSONObject count_response = (JSONObject) count_obj.get("response");

					JSONObject count_body = (JSONObject) count_response.get("body");
					JSONObject count_header = (JSONObject) count_response.get("header");

					String count_resultCode = count_header.get("resultCode").toString().trim();
					String count_resultMsg = count_header.get("resultMsg").toString().trim();

					if((count_resultCode.equals("03"))){
						System.out.println("data not exist!!");
					} else if (count_resultCode.equals("00") && count_body.get("items") instanceof String) {
						System.out.println("data not exist!!");
					} else if ((!(count_resultCode.equals("00")) && !(count_resultCode.equals("03")))) {
						System.out.println(
								"공공데이터 서버 비정상 응답!!::resultCode::" + count_resultCode + "::resultMsg::" + count_resultMsg);
						//throw new Exception();
					} else {
						int numOfRows = ((Long) count_body.get("numOfRows")).intValue();
						int totalCount = ((Long) count_body.get("totalCount")).intValue();

						pageCount = (totalCount / numOfRows) + 1;
					}

					// step 2. 위에서 구한 pageCount 숫자만큼 반복하면서 파싱

					for (int i = 1; i <= pageCount; i++) {
						
						//json = JsonParser.parseWatJson(service_url, service_key, String.valueOf(i));
						
						//서버 이슈로 에러가 나서 xml 타입으로 리턴되면 그냥 데이터 없는 json으로 변경해서 리턴하도록 처리
						//원래 에러 처리하려고 했지만 하나라도 에러가 나면 시스템 전체에서 에러로 판단하기에...
						//공통 클래스로 로직 빼 놓음
						// 2020.06.02 : 빈 Json을 리턴하도록 롤백
						// 2020.06.05 : String 리턴으로 잡았더니 에러 남.. JSONObject리턴으로 수정하고, 해당 메서드에 빈 json 로직을 넣음
						/*if(json.indexOf("</") > -1){
							System.out.print("공공데이터 서버 비 JSON 응답");
							json = "{\"response\":{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"items\":\"\"}}}";
						}*/

						JSONObject obj = JsonParser.parseWatJson_obj(service_url, service_key, String.valueOf(i));
						JSONObject response = (JSONObject) obj.get("response");

						JSONObject body = (JSONObject) response.get("body");
						JSONObject header = (JSONObject) response.get("header");

						String resultCode = header.get("resultCode").toString().trim();
						String resultMsg = header.get("resultMsg").toString().trim();

						if (!(resultCode.equals("00"))) {
							System.out.println(
									"공공데이터 서버 비정상 응답!!::resultCode::" + resultCode + "::resultMsg::" + resultMsg);
							//throw new Exception();
						} else if (resultCode.equals("00") && body.get("items") instanceof String) {
							System.out.println("data not exist!!");
						} else if (resultCode.equals("00") && !(body.get("items") instanceof String)) {
							
							String numOfRows_str = body.get("numOfRows").toString();
							String totalCount_str = body.get("totalCount").toString();

							// 입력 파라미터에 따라 하위배열 존재 여부가 달라지므로 분기 처리
							if (body.get("item") instanceof JSONObject) {
								
								String rnum = " "; // 정렬순서
								String perCd = " "; // 사전환경성 검토 코드
								String bizSeq = " "; // 사업고유 번호
								String bizNm = " "; // 사업명
								String ccilOrganCd = " "; // 협의기관
								String drfopTmdt = " "; // 초안공람 기간

								JSONObject items_jsonObject = (JSONObject) body.get("item");

								Set<String> key = items_jsonObject.keySet();

								Iterator<String> iter = key.iterator();

								while (iter.hasNext()) {

									String keyname = iter.next();
									
									rnum = JsonParser.colWrite_String(rnum, keyname, "rnum", items_jsonObject);
									perCd = JsonParser.colWrite_String(perCd, keyname, "perCd", items_jsonObject);
									bizSeq = JsonParser.colWrite_String(bizSeq, keyname, "bizSeq", items_jsonObject);
									bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", items_jsonObject);
									ccilOrganCd = JsonParser.colWrite_String(ccilOrganCd, keyname, "ccilOrganCd", items_jsonObject);
									drfopTmdt = JsonParser.colWrite_String(drfopTmdt, keyname, "drfopTmdt", items_jsonObject);

								}
								
								// step 4. 파일에 쓰기
								try {
									PrintWriter pw = new PrintWriter(
											new BufferedWriter(new FileWriter(file, true)));

									pw.write(resultCode); 
									pw.write("|^");
									pw.write(resultMsg); 
									pw.write("|^");
									pw.write(numOfRows_str); 
									pw.write("|^");
									pw.write(Integer.toString(i)); 
									pw.write("|^");
									pw.write(totalCount_str); 
									pw.write("|^");
									pw.write(rnum); 
									pw.write("|^");
									pw.write(perCd); 
									pw.write("|^");
									pw.write(bizSeq); 
									pw.write("|^");
									pw.write(bizNm); 
									pw.write("|^");
									pw.write(ccilOrganCd); 
									pw.write("|^");
									pw.write(drfopTmdt); 
									pw.println();
									pw.flush();
									pw.close();

								} catch (IOException e) {
									e.printStackTrace();
								}			


							} else if (body.get("item") instanceof JSONArray) {

								JSONArray items_jsonArray = (JSONArray) body.get("item");

								for (int r = 0; r < items_jsonArray.size(); r++) {
									
									String rnum = " "; // 정렬순서
									String perCd = " "; // 사전환경성 검토 코드
									String bizSeq = " "; // 사업고유 번호
									String bizNm = " "; // 사업명
									String ccilOrganCd = " "; // 협의기관
									String drfopTmdt = " "; // 초안공람 기간

									JSONObject item_obj = (JSONObject) items_jsonArray.get(r);

									Set<String> key = item_obj.keySet();

									Iterator<String> iter = key.iterator();

									while (iter.hasNext()) {

										String keyname = iter.next();
										
										rnum = JsonParser.colWrite_String(rnum, keyname, "rnum", item_obj);
										perCd = JsonParser.colWrite_String(perCd, keyname, "perCd", item_obj);
										bizSeq = JsonParser.colWrite_String(bizSeq, keyname, "bizSeq", item_obj);
										bizNm = JsonParser.colWrite_String(bizNm, keyname, "bizNm", item_obj);
										ccilOrganCd = JsonParser.colWrite_String(ccilOrganCd, keyname, "ccilOrganCd", item_obj);
										drfopTmdt = JsonParser.colWrite_String(drfopTmdt, keyname, "drfopTmdt", item_obj);

									}

									// step 4. 파일에 쓰기
									try {
										PrintWriter pw = new PrintWriter(
												new BufferedWriter(new FileWriter(file, true)));

										pw.write(resultCode); 
										pw.write("|^");
										pw.write(resultMsg); 
										pw.write("|^");
										pw.write(numOfRows_str); 
										pw.write("|^");
										pw.write(Integer.toString(i)); 
										pw.write("|^");
										pw.write(totalCount_str); 
										pw.write("|^");
										pw.write(rnum); 
										pw.write("|^");
										pw.write(perCd); 
										pw.write("|^");
										pw.write(bizSeq); 
										pw.write("|^");
										pw.write(bizNm); 
										pw.write("|^");
										pw.write(ccilOrganCd); 
										pw.write("|^");
										pw.write(drfopTmdt); 
										pw.println();
										pw.flush();
										pw.close();

									} catch (IOException e) {
										e.printStackTrace();
									}			

								}

							} else if (body.get("item") instanceof String) {
								System.out.println("data not exist!!");
							} else {
								System.out.println("parsing error!!");
							}

						} else {
							System.out.println("parsing error!!");
						}

						System.out.println("진행도::::::" + i + "/" + pageCount);

						//Thread.sleep(1000);

					}

					System.out.println("parsing complete!");

					// step 5. 대상 서버에 sftp로 보냄

					//TransSftp.transSftp(JsonParser.getProperty("file_path") + "EIA/TIF_EIA_37.dat", "EIA");

					long end = System.currentTimeMillis();
					System.out.println("실행 시간 : " + (end - start) / 1000.0 + "초");

				} else {
					System.out.println("파라미터 개수 에러!!");
					System.exit(-1);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("클래스명 : "+ Thread.currentThread().getStackTrace()[1].getClassName());
				System.exit(-1);
			}


	}

}
