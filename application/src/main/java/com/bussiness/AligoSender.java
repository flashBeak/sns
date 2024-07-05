package com.bussiness;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.json.JSONException;

public class AligoSender {
	
	private String kakaoMessageFormat = null;
	private static final Logger logger = LogManager.getLogger("KakaoSender");

	public AligoSender() {
		kakaoMessageFormat = "워킹페이 임시 비밀번호 발급 안내\r\n" + 
				"\r\n" + 
				"임시 비밀번호는 %s입니다.";
	}

	/**
	 * 토큰 발급
	 */
	private String getToken() throws Exception {
		// 인증 정보 구성
		String apiURL = "https://kakaoapi.aligo.in/akv10/token/create/1/m/"; 
		String aligoID = "workinglabs"; // SMS 아이디
		String apiKEY =  "01pc41visd7kykgahq3nme675xz73tem";//인증키
		String token = null;
		
		URL url;

		try {
			// 사용성 문제로 인해 Java 20부터 URL 클래스 생성자 사용 불가
			// url = new URL(sendAPIURL);

			URI uri = new URI(apiURL);
			url = uri.toURL();
		
			Map<String,Object> params = new LinkedHashMap<>(); // 파라미터 세팅
			params.put("apikey", apiKEY);
			params.put("userid", aligoID);
			
			// 전송 정보 구성
			StringBuilder postData = new StringBuilder();
			for (Map.Entry<String,Object> param : params.entrySet()) {
				if (postData.length() != 0) {
					postData.append('&');	
				}

				postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
				postData.append('=');
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			}

			byte[] postDataBytes = postData.toString().getBytes("UTF-8");
			
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			conn.setDoOutput(true);
			conn.getOutputStream().write(postDataBytes); // POST 호출
		
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) { // response 출력
				response.append(inputLine);
			}
		
			in.close();
			
			// 결과 JSON Parsing
			JSONObject tokenInfo = new JSONObject(response.toString());
			
			System.out.println(tokenInfo.get("code"));

			int code = (int)tokenInfo.get("code");
			
			if (code == 0) {	// 성공
				token = (String)tokenInfo.get("token");
				logger.debug("카카오톡 전송 토큰 얻기 성공 : " + token);
			}
		} catch (MalformedURLException e) {
			token = null;
		} catch (UnsupportedEncodingException e) {
			token = null;
		} catch (IOException e) {
			token = null;
		} catch (JSONException e) {
			token = null;
		}
		
		return token;
	}

	public void send(String phone, String password) throws Exception {

		String sendAPIURL = "https://kakaoapi.aligo.in/akv10/alimtalk/send/";
		String aligoID = "workinglabs";		// 알리고 아이디
		String apiKey = "01pc41visd7kykgahq3nme675xz73tem";	// 인증키
		String senderKey = "7c9584792754b70fbe2a429f5c4c83350f8308ef";	// 워킹랩스 카카오채널 Senderkey 전송키
		String templateCode = "TO_1663";	// 템플릿코드
		String sender = "010-5424-6808";	// 발송자 전화번호
		String subject = "임시 비밀번호 발급";	// 제목

		// 전송 내용 설정
		String message = String.format(kakaoMessageFormat, password);
		
		// 토큰 가져오기
		String token = getToken();
		
		if (token != null) {
			URL url;
			try {
				// 사용성 문제로 인해 Java 20부터 URL 클래스 생성자 사용 불가
				// url = new URL(sendAPIURL);

				URI uri = new URI(sendAPIURL);
				url = uri.toURL();
			
				Map<String,Object> params = new LinkedHashMap<>(); // 파라미터 세팅
				params.put("apikey", apiKey);
				params.put("userid", aligoID);
				params.put("token", token);
				params.put("senderkey", senderKey);
				params.put("tpl_code", templateCode);
				params.put("sender", sender);
				params.put("receiver_1", phone);
				params.put("subject_1", subject);
				params.put("message_1", message);
				params.put("failover", "Y");
				params.put("fsubject_1", subject);
				params.put("fmessage_1", message);
				
				StringBuilder postData = new StringBuilder();
				for (Map.Entry<String,Object> param : params.entrySet()) {
					if (postData.length() != 0)  {
						postData.append('&');
					}

					postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
					postData.append('=');
					postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
				}

				byte[] postDataBytes = postData.toString().getBytes("UTF-8");
				
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
				conn.setDoOutput(true);
				conn.getOutputStream().write(postDataBytes); // POST 호출
			
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			
				String inputLine;
				StringBuffer response = new StringBuffer();
				
				while ((inputLine = in.readLine()) != null) { // response 출력
					logger.debug("카카오톡 전송 결과 " + inputLine);
					response.append(inputLine);
				}
			
				in.close();

				// 결과 JSON Parsing
				JSONObject sendInfo = new JSONObject(response.toString());

				int code = (int)sendInfo.get("code");
				if (code == 0) {
					logger.debug("카카오톡 전송 성공");
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}