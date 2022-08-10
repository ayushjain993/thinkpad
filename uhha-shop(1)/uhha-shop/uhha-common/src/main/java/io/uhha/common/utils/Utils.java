package io.uhha.common.utils;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import io.uhha.common.constant.Constant;
import io.uhha.common.exception.BCException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Utils {
	
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);


	/**
	 * 获得随机字符串
	 * @param count 字符串长度
	 * @return
	 */
	public static String randomString(int count) {
		String str = "abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";
		int size = str.length();
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		while (count > 0) {
			sb.append(String.valueOf(str.charAt(random.nextInt(size))));
			count--;
		}
		return sb.toString();
	}

	/**
	 * 获取随机数字
	 * @param length 随机数长度
	 * @return
	 */
	public static String randomInteger(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(new Random().nextInt(10));
		}
		return sb.toString();
	}

	/**
	 * 获取随机图像名称
	 * @return
	 */
	public static String getRandomImageName() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmsss");
		String path = simpleDateFormat.format(new Date());
		path += "_" + randomString(5);
		return path;
	}

	/**
	 * 保存文件
	 * @param dir 保存文件的目录
	 * @param fileName 文件名称
	 * @param inputStream 保存的数据流
	 * @return 保存状态（是否成功）
	 */
	public static boolean saveFile(String dir, String fileName, InputStream inputStream) {
		boolean flag = false;
		File directory = new File(dir);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		if (!directory.isDirectory()) {
			logger.debug("Not a directory!");
			return flag;
		}
		if (!directory.exists()) {
			directory.mkdirs();
		}

		if (inputStream == null) {
			logger.debug("InputStream null.");
			return flag;
		}

		File realFile = new File(directory, fileName);
		FileOutputStream fileOutputStream = null;
		int tmp = 0;
		try {
			fileOutputStream = new FileOutputStream(realFile);
			while ((tmp = inputStream.read()) != -1) {
				fileOutputStream.write(tmp);
			}

			if (fileOutputStream != null) {
				fileOutputStream.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}

			flag = true;

		} catch (Exception e) {
			logger.debug("Read InputStream fail.");
		} finally {
			fileOutputStream = null;
			inputStream = null;
		}

		return flag;
	}

	/**
	 * md5码生成（先使用MD5加密，再使用Base64加密）
	 * @param content 需要加密的字符串
	 * @return
	 * @throws Exception
	 */
	public static String MD5(String content) throws BCException {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BCException();
		}
		Base64.Encoder baseEncoder = Base64.getEncoder();
		String retString = baseEncoder.encodeToString(md5.digest(content.getBytes()));
		return retString;
	}

	/**
	 * 获取时间撮
	 * 
	 * @return
	 */
	public static Timestamp getTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 后台页数
	 * @return
	 */
	public static int getNumPerPage() {
		return 40;
	}

	/**
	 * 通用唯一识别码
	 * 
	 * @return
	 */
	public static synchronized String UUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * 计算两个时间差
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	public static long timeMinus(Timestamp t1, Timestamp t2) {
		return (t1.getTime() - t2.getTime()) / 1000;
	}

	/**
	 * 获得今天0点
	 * @return
	 */
	public static long getTimesNowZero() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	/**
	 * 以当前时间为起点,增加天数
	 * @return
	 */
	public static long getTimesAdd(int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTimeInMillis();
	}

	/**
	 * 获得指定日期的0点
	 * @return
	 */
	public static long getTimesByDay(int day) {
		Calendar cal = Calendar.getInstance();
		long nowTime = new Date().getTime();
		long dayTime = day * 24 * 60 * 60 * 1000;
		cal.setTime(new Date(nowTime + dayTime));
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	/**
	 * 获得指定日期的0点
	 * @return
	 */
	public static long getDayByMonth(int day) {
		Calendar cal = Calendar.getInstance();
		long nowTime = new Date().getTime();
		cal.setTime(new Date(nowTime));
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}
	
	/**
	 * 获取指定时间
	 * @return
	 */
	public static Timestamp getYYYYMMDDHHMM() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Timestamp(calendar.getTime().getTime());
	}

	/**
	 * 获取当前时间字符串
	 * @return
	 */
	public static String getCurTimeString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	/**
	 * 将数字转换成字符串
	 * @param f
	 * @return
	 */
	public static String number4String(BigDecimal f) {
		DecimalFormat df = new DecimalFormat();
		String style = "0.0000";// 定义要显示的数字的格式
		df.applyPattern(style);
		return df.format(f);
	}
	
	/**
	 * 填补位数
	 * @param f
	 * @return
	 */
	public static String number2String(BigDecimal f) {
		DecimalFormat df = new DecimalFormat();
		String style = "0.00";// 定义要显示的数字的格式
		df.applyPattern(style);
		return df.format(f);
	}
	
	/**
	 * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
	 * @param request
	 * @return
	 */
	public final static String getIpAddr(HttpServletRequest request) {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

		String ip = request.getHeader("X-Forwarded-For");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}



	/** 
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址, 
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值 
     *  
     * @return ip
     */
    public static String getRemoteRealAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For"); 
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {  
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("X-Real-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        } 
        return ip;  
    }
	
	/**
	 * 格式化时间
	 * @param timestamp
	 * @return
	 */
	public static String dateFormatYYYYMMDD(Timestamp timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(timestamp);
	}

	/**
	 * 格式化时间
	 * @param timestamp
	 * @return
	 */
	public static String dateFormat(Timestamp timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(timestamp);
	}

	/**
	 * 格式化时间
	 * @param timestamp
	 * @return
	 */
	public static String dateFormat(Timestamp timestamp,String formatsString) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatsString);
		return sdf.format(timestamp);
	}

	/**
	 * 格式化时间
	 * @param timestamp
	 * @return
	 */
	public static String dateFormatYYYYMMDDHHMM(Timestamp timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(timestamp);
	}

	/**
	 * 格式化时间
	 * @param date
	 * @return
	 */
	public static String dateFormatHHMMSS(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}
	
	/**
	 * 检测参数是否为数值
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str == null || str.trim().length() == 0)
			return false;
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 获取随机数字
	 * @param length 生成字符串的长度
	 * @return
	 */
	public static String getRandomString(int length) {
		String base = "01234567890123456789012345678901234567890123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	/**
	 * 获取时间String,yyyy-MM-dd,天数+-
	 * @param day
	 * @return
	 */
	public static String getCurTimeString(int day) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	
	/**
	 * 获取时间对象,yyyy-MM-dd,天数+-
	 * @param day
	 * @return
	 */
	public static Timestamp getCurTimestamp(int day) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);// 把日期往后增加一天.整数往后推,负数往前移动
		return new Timestamp(calendar.getTime().getTime()); // 这个时间就是日期往后推一天的结果
	}
	
	/**
	 * 获取指定时间的   天数 + -
	 * @param day 加减天数
	 * @param curday 当前时间
	 * @return 需要时间
	 */
	public static Timestamp getCurTimestampCS(int day,Timestamp curday) {		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(curday);
		calendar.add(Calendar.DATE, day);
		return new Timestamp(calendar.getTime().getTime());
	}
	
	/**
	 * 获取时间Date，yyyy-MM-dd,天数+-
	 * @param date
	 * @return
	 */
	public static Date getCurTimeString(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
			
		}
	}
	
	/**
	 * 获取指定日期的Timestamp
	 * @param datesString
	 * @return
	 */
	public static Timestamp getSpecifyDateTimestamp(String datesString,int day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try {
			date = sdf.parse(datesString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);// 把日期往后增加一天.整数往后推,负数往前移动
		return new Timestamp(calendar.getTime().getTime()); // 这个时间就是日期往后推一天的结果
	}
	
	/**
	 * 获取指定日期的Timestamp
	 * @param datesString
	 * @return
	 */
	public static Timestamp getDateForHHMMSS(String datesString) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Date date = new Date();
		try {
			date = sdf.parse(datesString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return new Timestamp(calendar.getTime().getTime()); 
	}
	
	/**
	 * 隐藏登录名
	 * @param loginName
	 * @return
	 */
	public static String formatloginName(String loginName) {
		if(StringUtils.isEmpty(loginName)){
			return "";
		}
		if (PhoneValiUtil.isMobile(loginName)) {
			loginName = loginName.substring(0, 3) + "****" + loginName.substring(loginName.length() - 4, loginName.length());
		} else if (loginName.matches(Constant.EmailReg)) {
			loginName = loginName.substring(0, 3) + "****" + loginName.substring(loginName.length() - 4, loginName.length());
		} else {
			int endSub = loginName.length() < 6 ? loginName.length() : 6;
			loginName = loginName.substring(0, endSub) + "****";
		}
		return loginName;
	}
	
	public static boolean isIp(String ip){
		if(ip.length() < 7 || ip.length() > 15 || "".equals(ip))
		{
			return false;
		}
		/**
		 * 判断IP格式和范围
		 */
		String rexp = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		Pattern pat = Pattern.compile(rexp);  
		Matcher mat = pat.matcher(ip);  
		boolean ipAddress = mat.find();
		return ipAddress;
	}
	
	/**
	 * 过滤特殊字符
	 */
	public static String filtChart(String str){
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\"]"; 
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	
	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 字符串转化Timestamp型
	 * @param datesString
	 * @author ZGY
	 * @return
	 */
	public static Timestamp getStringConversiontamp(String datesString){
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = Timestamp.valueOf(datesString);
			return ts;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 去掉小数点后面多余的无效0
	 * @param s
	 * @return
	 * @author csj
	 */
	public static String rvZeroAndDot(String s){
		if (s.isEmpty()) {
			return null;
		}
		if(s.indexOf(".") > 0){
			s = s.replaceAll("0+?$", "");//去掉多余的0
			s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
		}
		return s;
	}

	/**
	 * String时间戳转日期类型*1000
	 *
	 */
	public static Date getDateBytimes(String times){
		try{
			BigDecimal bigDecimal = new BigDecimal(times);
			BigDecimal multiply = bigDecimal.multiply(new BigDecimal("1000"));
			Date date = new Date(Long.valueOf(multiply.toString()));
			return date;
		}catch (Exception e){
			logger.error("common|Utils|getDateBytimes|error"+e.getMessage());
		}
		return null;
	}

	/**
	 * 数量*精度 = 交易数据中的数量
	 * @param amount
	 * @param precision
	 * @return
	 */
	public static String getAmountMultiplyPrecision(BigDecimal amount , Integer precision){
		if(amount != null && precision != null){
			return amount.multiply(new BigDecimal("1E+"+precision)).toPlainString();
		}
		return null;
	}

	/**
	 * URL get请求
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getRequestURL(String url) throws IOException {
		URL getURL = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) getURL.openConnection();

		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		connection.connect();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder sbStr = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			sbStr.append(line);
		}
		bufferedReader.close();
		connection.disconnect();
		return new String(sbStr.toString().getBytes(), "utf-8");
	}

	/**
	 * URL put请求
	 * @param obj
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String putRequestURL(JSONObject obj, String url) throws IOException {
		URL postURL = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) postURL.openConnection();
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod("PUT");
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setInstanceFollowRedirects(true);
		httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
		StringBuilder sbStr = new StringBuilder();
		PrintWriter out = new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "utf-8"));
		out.println(obj);
		out.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			sbStr.append(inputLine);
		}
		in.close();
		httpURLConnection.disconnect();
		return new String(sbStr.toString().getBytes(), "utf-8");
	}

}
