package com.bussiness;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.net.URLEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SendHttp {
	
	public SendHttp() {
	}
	
	static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
	
	static String urlEncodeUTF8(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                urlEncodeUTF8(entry.getKey().toString()),
                urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();       
    }

	/**
	 * get 전송
	 * @return JSON
	 * Can be called by Nomal Page.
	 */
	static public String get(String urlStr, Map<String, String> headers, Map<String, Object> queries) {

		try {
	        
	        if (queries.size() > 0) {	// 파라미터가 있을 경우
	        	urlStr += (urlStr.contains("?") ? "&" : "?") + urlEncodeUTF8(queries);
//				ObjectMapper rootObjectMapper = new ObjectMapper();
//				String json = rootObjectMapper.writeValueAsString(params);
//
//		        byte[] postDataBytes = json.toString().getBytes("UTF-8");
//		        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		        //conn.getOutputStream().write(postDataBytes); // POST 호출
	        }
	        
			URI uri = new URI(urlStr);
			URL url = uri.toURL();

	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setDoOutput(true);
	        
	        if (headers.size() > 0) {	// 설정된 헤더가 있을 경우
	        	for( Map.Entry<String, String> entry : headers.entrySet() ){
	        		String key = entry.getKey();
	        		String value = entry.getValue();
	        		
		        	conn.setRequestProperty(key, value);
	        	}
	        }
	 
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	 
	        String inputLine;
			StringBuffer response = new StringBuffer();
			
	        while((inputLine = in.readLine()) != null) { // response 출력
	        	response.append(inputLine);
	        }
	 
	        in.close();
	        
			return response.toString();
//		} catch (MalformedURLException e) {
//			//e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			//e.printStackTrace();
//		} catch (IOException e) {
//			//e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * post 전송
	 * @return JSON
	 * Can be called by Nomal Page.
	 */
	static public String post(String urlStr, Map<String, String> headers, Map<String, Object> params) {

		try {
			//URL url = new URL(urlStr);
	        
			URI uri = new URI(urlStr);
			URL url = uri.toURL();

	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);
	        
	        if (headers.size() > 0) {	// 설정된 헤더가 있을 경우
	        	for( Map.Entry<String, String> entry : headers.entrySet() ){
	        		String key = entry.getKey();
	        		String value = entry.getValue();
	        		
		        	conn.setRequestProperty(key, value);
	        	}
	        }
	        
	        if (params.size() > 0) {	// 파라미터가 있을 경우
				ObjectMapper rootObjectMapper = new ObjectMapper();
				String json = rootObjectMapper.writeValueAsString(params);

		        byte[] postDataBytes = json.toString().getBytes("UTF-8");
		        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		        conn.getOutputStream().write(postDataBytes); // POST 호출
	        }
	 
	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	 
	        String inputLine;
			StringBuffer response = new StringBuffer();
			
	        while((inputLine = in.readLine()) != null) { // response 출력
	        	response.append(inputLine);
	        }
	 
	        in.close();
	        
			return response.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
