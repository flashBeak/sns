package com.bussiness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NaverMapManager {

	private static final String KEY_ID = "2ssqvver4b";
	private static final String SECRET = "toFnVbLSDY72hwaiFDyf0XiwFjzSVp5f4ebj40QE";
	
	public NaverMapManager() {
	}

	/**
	 * gps 정보를 주소 정보로 변환 
	 * @param lat
	 * @param lon
	 * @return JSON
	 */
	@SuppressWarnings("unchecked")
	static public Map<String, Object> convertGpsToAddress(String lat, String lon) {

		try {
	        String url = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";
	        
	        Map<String, String> headers = new HashMap<String, String>();
	        headers.put("X-NCP-APIGW-API-KEY-ID", KEY_ID);
	        headers.put("X-NCP-APIGW-API-KEY", SECRET);
	        
	        Map<String, Object> queries = new HashMap<String, Object>();
	        queries.put("coords", lon + "," + lat);
	        queries.put("output", "json");
			
	        String result = SendHttp.get(url, headers, queries);
	        
	        ObjectMapper mapper = new ObjectMapper();
	        Map<String, Object> returnMap = mapper.readValue(result, Map.class);
	        
	        return returnMap;
	        
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", "error");
		
		return returnMap;
	}

	/**
	 * 주소 찾기
	 * @param address
	 * @return JSON
	 */
	static public Map<String, Object> searchAddress(String address) {
		return searchAddressList(address, "", "", 1);
	}

	/**
	 * 주소 찾기
	 * @param address
	 * @return JSON
	 */
	static public Map<String, Object> searchAddressList(String address, int itemCount) {
		return searchAddressList(address, "", "", itemCount);
	}

	/**
	 * 주소 찾기
	 * 위치 정보가 있을 경우 해당 위치까지의 거리 반환. 
	 * @param address
	 * @param lat
	 * @param lon
	 * @param itemCount 반환 개수
	 * @return JSON
	 */
	@SuppressWarnings("unchecked")
	static public Map<String, Object> searchAddressList(String address, String lat, String lon, int itemCount) {

		try {
	        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
	        
	        Map<String, String> headers = new HashMap<String, String>();
	        headers.put("X-NCP-APIGW-API-KEY-ID", KEY_ID);
	        headers.put("X-NCP-APIGW-API-KEY", SECRET);
	        
	        Map<String, Object> queries = new HashMap<String, Object>();
	        queries.put("query", address);
	        
	        if (!Utils.isNullOrEmpty(lat) && !Utils.isNullOrEmpty(lon)) {
		        queries.put("coordinate", lon + "," + lat);
	        }
			
	        String result = SendHttp.get(url, headers, queries);
	        
	        ObjectMapper mapper = new ObjectMapper();
	        Map<String, Object> returnMap = mapper.readValue(result, Map.class);

			if (itemCount <= 1) {
				if ("OK".equals((String)returnMap.get("status"))) {
					Map<String, Object> meta = (Map<String, Object>) returnMap.get("meta");
					if ((int)meta.get("totalCount") == 0) {
						return null;
					}
	
					List<Map<String, Object>> addressesList = (ArrayList<Map<String, Object>>)returnMap.get("addresses");
					
					return ((Map<String, Object>) addressesList.get(0));
				} else {
					return null;
				}
			}
	        
	        return returnMap;
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("status", "error");
		
		return returnMap;
	}
}
