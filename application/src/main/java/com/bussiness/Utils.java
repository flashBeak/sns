package com.bussiness;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import javax.crypto.Cipher;

public class Utils {
	public static String DefaultDateFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static String encodeSHA256(String str){
		String SHA = ""; 
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
			sh.update(str.getBytes());

			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer(); 

			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}

			SHA = sb.toString();
		}catch(Exception e){
			e.printStackTrace(); 
			SHA = null; 
		}

		return SHA;
	}
	
	// 문자열이 null이거나 ""인 경우 true를 리턴한다.
	public static boolean isNullOrEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
	
	// 문자열이 null이 아니고 같으면 true 리턴
	public static boolean isEqual(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return false;
		} else {
			if (str1.equals(str2)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	// 문자열이 null이 아니고 같으면 true 리턴
	public static boolean isEqual(int int1, String str2) {
		if (str2 == null) {
			return false;
		} else {
			if (String.valueOf(int1).equals(str2)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	// 문자열이 null이 아니고 같으면 true 리턴
	public static boolean isEqual(String str2, int int1) {
		if (str2 == null) {
			return false;
		} else {
			if (String.valueOf(int1).equals(str2)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	// 문자열을 숫자로 변경해준다.
	// 문자열에 오류가 있을 경우 default 값을 반환한다.
	public static int parseInt(String str, int defaultValue) {
		int returnValue = defaultValue;
		try {
			if (str == null) {
				return returnValue;
			}
			
			returnValue = Integer.parseInt(str);
		}
		catch(Exception e) {
			return returnValue;
		}
		return returnValue;
	}

	// 문자열을 Float형으로 변경해준다.
	// 문자열에 오류가 있을 경우 default 값을 반환한다.
	public static float parseFloat(String str, float defaultValue) {
		float returnValue = defaultValue;
		try {
			if (str == null) {
				return returnValue;
			}
			
			returnValue = Float.parseFloat(str);
		}
		catch(Exception e) {
			return returnValue;
		}
		return returnValue;
	}
	
	// 문자열 변환
	public static String replace(String str, String regex, String replacement) {
		if (str == null) {
			return null;
		}
		
		return str.replaceAll(regex, replacement);
	}

	// 어느 날이 더 빠른지 두 날짜비교
	// 왼쪽날이 더 빠르면 -1, 같으면 0, 오른쪽이 빠르면 1
	public static int compareDate(String left, String right, String dateFormat) throws ParseException {

        SimpleDateFormat simpleDateFormatLeft = new SimpleDateFormat(dateFormat);
        Date dateLeft = simpleDateFormatLeft.parse(left);
        
        SimpleDateFormat simpleDateFormatRight = new SimpleDateFormat(dateFormat);
        Date dateRight = simpleDateFormatRight.parse(right);

		Calendar calendarLeft = Calendar.getInstance();
		calendarLeft.setTime(dateLeft);

		int result = dateLeft.compareTo(dateRight);
        return result;
	}
	
	// 문자열 시간(ex 10:00, 20:00)을 입력 받아 두 시간의 차이를 계산한다.
	// 시간 차이를 분으로 반환한다.
	public static String timeTermMinute(String startTime, String endTime) {
		int returnMinute = 0;
		if (Utils.isNullOrEmpty(startTime) || Utils.isNullOrEmpty(endTime) )
			return null;

		String [] startTimeArray = startTime.split(":");
		if (startTimeArray.length != 2 )
			return null;
		String [] endTimeArray = endTime.split(":");
		if (endTimeArray.length != 2 )
			return null;

		int startTimeHour = Utils.parseInt(startTimeArray[0], 0);
		int startTimeMinute = Utils.parseInt(startTimeArray[1], 0);
		int endTimeHour = Utils.parseInt(endTimeArray[0], 0);
		int endTimeMinute = Utils.parseInt(endTimeArray[1], 0);
		
		returnMinute = ((endTimeHour - startTimeHour) * 60) + endTimeMinute - startTimeMinute;

		return String.valueOf(returnMinute);
	}
	
	public static String convertDateFormat6To10(String str6) throws ParseException {
		if (Utils.isNullOrEmpty(str6) )
			return null;
		
        // 생일 6자리 -> 10자리로 변경 831130 -> 1983-11-30
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
	    SimpleDateFormat dateFormat8 = new SimpleDateFormat("yyyy-MM-dd");
	    Date nDate = dateFormat.parse(str6);
	    String str10 = dateFormat8.format(nDate);
	    
	    return str10;
	}
	
    // 이미지 파일인지 검사 - 전체 파일명 검사
    public static boolean isImage(String fileName) {
    	if (fileName == null )
    		return false;
    	
    	if (fileName.toLowerCase().contains("png") || fileName.toLowerCase().contains("jpg") ||
    			fileName.toLowerCase().contains("bmp") || fileName.toLowerCase().contains("gif") ||
    			fileName.toLowerCase().contains("jpeg")) {
    		return true;
    	}
    	
    	return false;
    }


    public static BufferedImage resizeImage(final BufferedImage bufferedImageParam, int width, int height) {
    	// w, y : 원본이미지 크기
    	// width, height : 줄일 이미지 크기
    	Image image = (BufferedImage)bufferedImageParam;
    	
    	int w = bufferedImageParam.getWidth();
    	int h = bufferedImageParam.getHeight();
    	if (height < 0 )
    		height = h;
    	
        if (w <= width) {	// 줄일 크기가 원본 크기 보다 작으므로, 아무 작업 안함
        	return bufferedImageParam;
        }
        else {
            float per = (float)width / (float)w;
            height = (int)(h * per);
        }

        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();

        return bufferedImage;
    }
    
    // 문자열 오름차순 정렬을 위한 비교 클래스
    public static class NameAscCompare implements Comparator<String> {
    	
		@Override
		public int compare(String arg0, String arg1) {
			return arg0.compareTo(arg1);
		}
	}

	/**
	 * 공통 컴포넌트 utl.fcc 패키지와 Dependency제거를 위해 내부 메서드로 추가 정의함
	 * 응용어플리케이션에서 고유값을 사용하기 위해 시스템에서17자리의TIMESTAMP값을 구하는 기능
	 *
	 * @param dateformat
	 * @return Timestamp 값
	 * @see
	 */
    public static String getNow(String dateFormat) {
    	
    	if (Utils.isNullOrEmpty(dateFormat)) {
    		dateFormat = Utils.DefaultDateFormat;
    	}

		String rtnStr = null;

		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		//String pattern = "yyyyMMddhhmmssSSS";

		SimpleDateFormat sdfCurrent = new SimpleDateFormat(dateFormat, Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		rtnStr = sdfCurrent.format(ts.getTime());

		return rtnStr;
	}
    
    public static String [] getBetweenDate(int type, String startDate, String endDate) {

        int startYear = Integer.parseInt(startDate.substring(0,4));
        int startMonth= Integer.parseInt(startDate.substring(5,7));
        int startDay = 0;

        Calendar cal = Calendar.getInstance();
        if (type == 0) {	// 일별
        	startDay = Integer.parseInt(startDate.substring(8,10));
            cal.set(startYear, startMonth -1, startDay);	// Calendar의 Month는 0부터 시작하므로 -1 해준다.
        }
        else {	// 월별
            cal.set(startYear, startMonth -1, 1);	// Calendar의 Month는 0부터 시작하므로 -1 해준다.
        }

        int start = 0;
        int end = 0;
        SimpleDateFormat sdf;
        
        if (type == 0) {	// 일별
        	sdf = new SimpleDateFormat("yyyy-MM-dd");
        	start = Integer.parseInt("" + startYear + startMonth + startDay);
        	end = Integer.parseInt(endDate.replaceAll("-", ""));
        }
        else { // 월별
        	sdf = new SimpleDateFormat("yyyy-MM");
        	start = Integer.parseInt("" + startYear + startMonth);
        	end = Integer.parseInt(endDate.substring(0,7).replaceAll("-", ""));
        }
        
        ArrayList<String> dayList = new ArrayList<String>();
        while(start <= end) {
        	
        	dayList.add(sdf.format(cal.getTime()));

            if (type == 0 )	{ // 일별
	            // Calendar의 날짜를 하루씩 증가한다.
	            cal.add(Calendar.DATE, 1);
            }
            else {
	            // Calendar의 날짜를 한달씩 증가한다.
	            cal.add(Calendar.MONTH, 1);
            }

            start = Integer.parseInt(sdf.format(cal.getTime()).replaceAll("-", ""));
        }
        
        String [] returnDayList = new String[dayList.size()];
        int i = 0;
        for( String day : dayList) {
        	returnDayList[i++] = day;
        }
        return returnDayList;
    }
	
	// 새로고침 방지
	// 방지하려는 페이지에 접근하기 전의 컨트롤러에서 세션에 랜덤 값을 넣고,
	// 방지하려는 페이지에서 해당 값이 있는지 확인하는 방식 
	public static boolean preventionRefresh(HttpSession session, String tokenName) {
		if (session == null )
			return false;
		
		// 토큰가져오기
		String token = (String) session.getAttribute(tokenName);
		if (!Utils.isNullOrEmpty(token)) {	// 토큰이 있으면 삭제
			session.removeAttribute(tokenName);
			return true;
		}
		else {
            System.out.println("invalid approach : no token - " + tokenName);
            return false;
		}
	}
	
	public static boolean isMobile(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        boolean mobile1 = userAgent.matches(".*(iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
        boolean mobile2 = userAgent.matches(".*(LG|SAMSUNG|Samsung).*");
        if(mobile1 || mobile2) {
            return true;
        }
        return false;
    }
	
	// 입력된 date의 1년전의 날짜를 반환
	public static String getLastYear(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendarDate = Calendar.getInstance();

    	String [] dateArray = date.split("-");
    	calendarDate.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]) -1, Integer.parseInt(dateArray[2]));

    	calendarDate.add(Calendar.YEAR, -1);	// 전년으로 계산하기 위해 -1
        
        String dateLastYear = simpleDateFormat.format(calendarDate.getTime());
        return dateLastYear;
    }
	
	// 날짜 더하기
	public static String addDate(String date, int count) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendarDate = Calendar.getInstance();

    	String [] dateArray = date.split("-");
    	calendarDate.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]) -1, Integer.parseInt(dateArray[2]));

    	calendarDate.add(Calendar.DATE, count);
        
        String newDate = simpleDateFormat.format(calendarDate.getTime());
        return newDate;
    }

	/**
	 * 날짜 유형별 날짜 더하기
	 * @param date		기준 날짜
	 * @param count		더할 값
	 * @param type		Calendar.DATE, Calendar.MONTH, Calendar.YEAR
	 * @return string
	 */
	public static String addDateByType(String date, int count, int type) {
		Calendar calendarDate = Calendar.getInstance();
		String [] dateArray = date.split("-");
		String dateFormat = "yyyy-MM-dd";
		
		switch (type) {
			case Calendar.DATE:
				calendarDate.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]) -1, Integer.parseInt(dateArray[2]));
				calendarDate.add(Calendar.DATE, count);
				dateFormat = "yyyy-MM-dd";
			break;
			case Calendar.MONTH:
				calendarDate.set(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]) -1, 1);	// 일 정보가 없으므로 1로 설정
				calendarDate.add(Calendar.MONTH, count);
				dateFormat = "yyyy-MM";
			break;
			case Calendar.YEAR:
				calendarDate.set(Integer.parseInt(dateArray[0]), 1, 1);	// 월, 일 정보가 없으므로 1로 설정
				calendarDate.add(Calendar.YEAR, count);
				dateFormat = "yyyy";
			break;
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		String newDate = simpleDateFormat.format(calendarDate.getTime());
		return newDate;
	}
	
	// 시간 차이 계산
	public static int getDiffTime(int type, String dateStr1, String dateStr2, String dateFormatStr) {
		try {
	    	
	    	if (Utils.isNullOrEmpty(dateFormatStr)) {
	    		dateFormatStr = Utils.DefaultDateFormat;
	    	}

			if (isNullOrEmpty(dateStr1) || isNullOrEmpty(dateStr2)) {	// 날짜가 없는 경우 add by sjbaek
				return 0;
			}

			SimpleDateFormat dateFormat1 = new SimpleDateFormat(dateFormatStr);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat(dateFormatStr);
            Date date1 = dateFormat1.parse(dateStr1);
            Date date2 = dateFormat2.parse(dateStr2);
            
            long diffSecond = (date1.getTime() - date2.getTime()) / 1000;
            int result = -1;
            
            switch (type) {
			case Calendar.MONTH: result = getDiffMonth(date1, date2); break;
			case Calendar.DATE: result = (int)diffSecond / (60 * 60 * 24); break;
			case Calendar.HOUR: result = (int)diffSecond / (60 * 60); break;
			case Calendar.MINUTE: result = (int)diffSecond / 60; break;
            }
            
            return result;
        } catch (ParseException ex) {
        	return 0;
        }
	}
	
	// 개월수 차이 계산
	// by chat gpt3
	private static int getDiffMonth(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);

		int diffYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);

		return diffMonth;
	}
	
	// 날짜 비교
	// date1이 이르면 음수
	// 같으면 0
	// date1이 늦으면 양수
	public static int compareDate(String dateStr1, String dateStr2) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
            Date date1 = dateFormat.parse(dateStr1);
            Date date2 = dateFormat.parse(dateStr2);
            
            if (date1.before(date2)) {
            	return -1;
            } else if (date1.after(date2)) {
            	return 1;
            } else {
            	return 0;
            }
        } catch (ParseException ex) {
        	return 0;
        }
	}
	
	// 날짜 비교
	// date1이 이르면 음수
	// 같으면 0
	// date1이 늦으면 양수
	public static int compareDate(Date date1, Date date2) {
		try {
            if (date1.before(date2)) {
            	return -1;
            } else if (date1.after(date2)) {
            	return 1;
            } else {
            	return 0;
            }
        } catch (Exception ex) {
        	return 0;
        }
	}
	
	// 랜덤 문자열 생성
	public static String getRandomString(int targetStringLength) {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 57; // letter 'z'
		//int rightLimit = 122; // letter 'z'
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
		  //.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      //.filter(i -> (i <= 57 || i >= 48))	// 숫자만
		  .limit(targetStringLength)
		  .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		  .toString();
		return generatedString;
	}
	
	// 랜덤 숫자 생성
	// by chat gpt3
	public static int getRandomInt(int min, int max) {
		if (min >= max) {
            throw new IllegalArgumentException("Invalid range. Minimum value must be less than maximum value.");
        }

        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
	}
	
	/**
	 * 날짜 유효성 검사
	 * @param startdStr
	 * @param enddStr
	 * @param format
	 * @return boolean
	 */
	public static boolean isValidDate(String startdStr, String enddStr, String format) {
		try {
			if (Utils.isNullOrEmpty(format)) {	// 날짜 형식 없음
				format = Utils.DefaultDateFormat;
			}
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			
			if (Utils.isNullOrEmpty(startdStr)) {	// 시작 날짜 설정 여부 확인
				return false;
			}
			
			if (Utils.isNullOrEmpty(enddStr)) {	// 종료 날짜 설정 여부 확인
				return false;
			}
			
            Date startd = simpleDateFormat.parse(startdStr);
            Date endd = simpleDateFormat.parse(enddStr);
            
            if (startd.after(endd)) {	// 시작 날짜가 종료 날짜보다 큰 경우
            	return false;
            }
				
			return true;

        } catch (ParseException ex) {
        	return false;
        }
	}

	/**
	 * sql 처리에 적절한 문자열로 변경
	 * @param str
	 * @param trim			boolean 공백 제거 여부
	 * @param decode		boolean url decoding 여부
	 * @param quotes		boolean html 특수 문자 변환 여부
	 * @param removeTag		boolean 태그 제거 여부
	 * @return String
	 */
	public static String convertForSqlProcessing(String str, Map<String, Object> option) {
		try {
			boolean trim = option.containsKey("trim") ? (boolean)option.get("trim") : true;
			boolean quotes = option.containsKey("quotes") ? (boolean)option.get("quotes") : true;
			boolean decode = option.containsKey("decode") ? (boolean)option.get("decode") : true;
			boolean removeTag = option.containsKey("removeTag") ? (boolean)option.get("removeTag") : true;

			if (trim) {
				str = str.trim();	// 공백 제거
			}
			if (quotes) {
				str = URLDecoder.decode(str, "UTF-8");	// url decoding
			}
			if (decode) {	
				str = StringEscapeUtils.escapeHtml4(str);	// html 특수 문자 변환 ex) <b> -> &lt;b&gt;
			}
			if (removeTag) {
				str = Jsoup.parse(str).text();	// 태그 제거
			}

			str = str.trim();

			return str;
		} catch (Exception exception) {
			return str;
		}
	}

	/**
	 * 올림, 내림한 값 가져오기
	 * @param digit		자리수
	 * @param type		0 올림, 1 반올림, 2 내림
	 * @param num
	 * @return float
	 */
	public static float getRoundValue(int digit, int type, float num) throws Exception {

		float resultNum = 0.0f;

		switch (type) {
			case 0:	// 자리수 이하 올림
				BigDecimal roundUpNum = new BigDecimal(num).setScale(digit, RoundingMode.CEILING);
				resultNum = roundUpNum.floatValue();
			break;
			case 1:	// 자리수 이하 반올림
				BigDecimal roundNum = new BigDecimal(num).setScale(digit, RoundingMode.HALF_UP);
				resultNum = roundNum.floatValue();
			break;
			case 2:	// 자리수 이하 내림
				BigDecimal roundDownNum = new BigDecimal(num).setScale(digit, RoundingMode.FLOOR);
				resultNum = roundDownNum.floatValue();
			break;
		}

		return resultNum;
	}

	/**
	 * 두 날짜 사이의 날짜 목록 가져오기
	 * by chat gpt3
	 * @param start
	 * @param endd
	 * @return list
	 */
	public static List<String> getDateList(String startd, String endd) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate startDate = LocalDate.parse(startd, formatter);
		LocalDate endDate = LocalDate.parse(endd, formatter);

		List<String> dateStringList = new ArrayList<>();

		LocalDate currentDate = startDate;
		while (!currentDate.isAfter(endDate)) {
			dateStringList.add(currentDate.format(formatter)); // 날짜를 문자열로 변환하여 리스트에 추가
			currentDate = currentDate.plusDays(1);
		}

		return dateStringList;
	}

	/**
	 * 날짜를 요일로 반환
	 * by chat gpt3
	 * @param dateStr
	 * @return list
	 */
	public static int getDayOfWeek(String dateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(dateStr, formatter);
		DayOfWeek dayOfWeek = date.getDayOfWeek();

		// DayOfWeek 열거형을 정수로 변환	0 월, 1 화, 2 수, 3 목, 4 금, 5 토, 6 일
		int dayOfWeekValue = dayOfWeek.getValue() - 1;

		// 만약 일요일(7)을 0으로 표현하려면 다음과 같이 처리할 수 있습니다.
		// int dayOfWeekValue = (dayOfWeek.getValue() + 5) % 7;

		return dayOfWeekValue;
	}

	/**
	 * 날짜가 해당요일이 맞는지 확인
	 * @param dateStr
	 * @param dayOfWeek		0 월, 1 화, 2 수, 3 목, 4 금, 5 토, 6 일
	 * @return boolean
	 */
	public static boolean compareDayOfWeek(String dateStr, String dayOfWeek) {
		if (Utils.isEqual(dayOfWeek, getDayOfWeek(dateStr))) {
			return true;
		}
		
		return false;
	}

	/**
	 * 날짜가 해당요일이 맞는지 확인
	 * @param dateStr
	 * @param dayOfWeek		0 월, 1 화, 2 수, 3 목, 4 금, 5 토, 6 일
	 * @return boolean
	 */
	public static boolean compareDayOfWeek(String dateStr, int dayOfWeek) {
		if (dayOfWeek == getDayOfWeek(dateStr)) {
			return true;
		}
		
		return false;
	}

	/**
	 * 두 지점간의 거리 계산
	 * by chat gpt3
	 * @param lat1Str   위도1 (문자열)
	 * @param lon1Str   경도1 (문자열)
	 * @param lat2Str   위도2 (문자열)
	 * @param lon2Str   경도2 (문자열)
	 * @return int (미터 단위)
	 */
	public static int calcDistance(String lat1Str, String lon1Str, String lat2Str, String lon2Str) {
		final int R = 6371; // 지구의 반지름 (단위: km)

		double lat1 = Double.parseDouble(lat1Str);
		double lon1 = Double.parseDouble(lon1Str);
		double lat2 = Double.parseDouble(lat2Str);
		double lon2 = Double.parseDouble(lon2Str);

		double lat1Rad = Math.toRadians(lat1);
		double lon1Rad = Math.toRadians(lon1);
		double lat2Rad = Math.toRadians(lat2);
		double lon2Rad = Math.toRadians(lon2);

		double dlon = lon2Rad - lon1Rad;
		double dlat = lat2Rad - lat1Rad;

		double a = Math.sin(dlat/2) * Math.sin(dlat/2) +
				Math.cos(lat1Rad) * Math.cos(lat2Rad) *
				Math.sin(dlon/2) * Math.sin(dlon/2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

		int distance = (int) (R * c * 1000); // 킬로미터를 미터로 변환 (1 km = 1000 m)
		return distance;
	}
	
	// by chatgpt3
	// aes 암호화
	public static String encrypt(String secretKey, String str) {
        try {
			Cipher cipher = Cipher.getInstance("AES");
			
			byte[] key = new byte[16];
			int i = 0;
			
			for(byte b : secretKey.getBytes()) {
				key[i++%16] ^= b;
			}
			
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
			
			return new String(Hex.encodeHex(cipher.doFinal(str.getBytes("UTF-8")))).toUpperCase();
		} catch(Exception e) {
			return null;
		}
    }

	// by chatgpt3
	// aes 복호화
	public static String decrypt(String secretKey, String str) {
        try {
			Cipher cipher = Cipher.getInstance("AES");
			
			byte[] key = new byte[16];
			int i = 0;
			
			for(byte b : secretKey.getBytes()) {
				key[i++%16] ^= b;
			}
			
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
			
			return new String(cipher.doFinal(Hex.decodeHex(str.toCharArray())));
		} catch(Exception e) {
			return null;
		}
    }
	
	// by chatgpt3
	// 카멜케이스로 변경
	public static String convertToCamelCase(String underscoreSeparated) {
        StringBuilder camelCase = new StringBuilder();
        boolean capitalizeNext = false;

        for (char ch : underscoreSeparated.toCharArray()) {
            if (ch == '_') {
                capitalizeNext = true;
            } else {
                camelCase.append(capitalizeNext ? Character.toUpperCase(ch) : ch);
                capitalizeNext = false;
            }
        }

        return camelCase.toString();
    }

	// by chatgpt3
    public static void putOrReplaceMap(Map<String, Object> paramMap, String key, String value) {
        if (paramMap.containsKey(key)) {
            paramMap.replace(key, value);
		} else {
            paramMap.put(key, value);
        }
    }
}
