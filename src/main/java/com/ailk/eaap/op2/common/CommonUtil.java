package com.ailk.eaap.op2.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.asiainfo.foundation.log.Logger;
import org.springframework.jdbc.UncategorizedSQLException;

import com.linkage.rainbow.util.StringUtil;

/**
 * 
* @ClassName: CommonUtil 
* @Description: 通用工具类
* @author 李铁扬 
* @date Aug 18, 2011 7:47:09 PM 
*
 */
public class CommonUtil {
	private static final Logger LOG = Logger.getLog(CommonUtil.class);
	
	private final static byte[] hex = "0123456789ABCDEF".getBytes();

	public static final String MINUTE = "1";//分钟
	public static final String HOUR = "2";//小时

	
	/**
	 * 
	* @Title: getNow 
	* @Description: 取当前系统时间,格式:yyyyMMddHHmmss，例如20080808122135
	* @param @return    
	* @return String    
	* @throws
	 */
	public static String getNow() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return sDateFormat.format(new Date());
	}

	public static String getNowMillSeconed() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sDateFormat.format(new Date());
	}
	
	/**
	 * 
	* @Title: getFormatTimeString 
	* @Description: TODO
	* @param @param date
	* @param @return    
	* @return String    
	* @throws
	 */
	public static String getFormatTimeString(Date date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return sDateFormat.format(date);
	}
	
	public static String getFormatMMString() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		return sDateFormat.format(new Date());
	}
	
	
	public static Date getDateFromString(String timeStr) {
		try{
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			return sDateFormat.parse(timeStr);
		}catch(Exception e){
			return new Date();
		}
		
	}
	
	/**
	 * 返回带时区的时间
	 * @return
	 */
	public static String getDefaultTimeZoneDateString() {
		SimpleDateFormat sdf =new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		sdf.setTimeZone(TimeZone.getDefault());
		return sdf.format(new Date());
	}

	public static String cutSoapMessage(String returnmethod, String soapMessage){
		String result = null;
		Pattern p = Pattern.compile("\\<"+ returnmethod + "(.*?)>(.*?)\\<\\/" + returnmethod +"\\>");
        soapMessage = replaceBlank(soapMessage) ;
        Matcher match = p.matcher(soapMessage);
        while (match.find()) {
            result = match.group(2);
        }
        
        return result;
	}
	
    /** 
     * 取消回车换行(\r\n)
     * <功能详细描述>
     * @param msg
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String replaceBlank(String msg)
    {
       Pattern p = Pattern.compile("\r|\n");
       Matcher m = p.matcher(msg);
       return m.replaceAll(""); 
    }

	/**
	 * 
	* @Title: byte2str 
	* @Description: 从字节数组到十六进制字符串转换
	* @param @param b
	* @param @return    
	* @return String    
	* @throws
	 */
	public static String byte2str(byte[] b) {

		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buff);
	}

	/**
	 * 
	* @Title: str2byte 
	* @Description:  从十六进制字符串到字节数组转换
	* @param @param hexstr
	* @param @return    
	* @return byte[]    
	* @throws
	 */
	public static byte[] str2byte(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) ((parseInt(c0) << 4) | parseInt(c1));
		}
		return b;
	}

	private static int parseInt(char c) {
//		if (c >= 'a')
//			return (c - 'a' + 10) & 0x0f;
//		if (c >= 'A')
//			return (c - 'A' + 10) & 0x0f;
//		return (c - '0') & 0x0f;
		return Integer.parseInt(String.valueOf(c), 16);
	}
	/**
	 * 
	* @Title: isPackageDiscarded 
	* @Description: 判断抛出的异常是否是ORACLE的存储过程失效异常
	* @param @param e
	* @param @return    
	* @return boolean    
	* @throws
	 */
	public static boolean isPackageDiscarded(Exception e){
		if(e==null)
			return false;
		//调用JDBC的SQLException异常
		if(e instanceof SQLException ){
			SQLException e1 = (SQLException)e;
			return "72000".equals(e1.getSQLState()) && e1.getErrorCode() == 4068; 
		}
		//可能是由spring抛出的封装后的异常
		if(e instanceof UncategorizedSQLException){
			
			SQLException e1 = ((UncategorizedSQLException)e).getSQLException();
			
			return "72000".equals(e1.getSQLState()) && e1.getErrorCode() == 4068; 
		}
		return false;
	}
	
	/**
	 * 
	* @Title: getErrMsg 
	* @Description: 取得异常信息
	* @param @param e
	* @param @return    
	* @return String    
	* @throws
	 */
	public static String getErrMsg(Throwable e){
		String errMsg = e.getMessage()+"\r\n";
		StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement s : trace) {
        	errMsg += s + "\r\n";
        }
        if(errMsg.length()>1500)
			errMsg=errMsg.substring(0,1500);
		return errMsg;
	}
	/**
	 * 
	* @Title: isEmpty 
	* @Description: 判断字符串是否为空
	* @param @param str
	* @param @return    
	* @return boolean    
	* @throws
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.equals("");
	}

	/**
	 * 
	* @Title: isOutTime 
	* @Description: 判断是否超出当前时间
	* @param @param str
	* @param @return    
	* @return boolean    
	* @throws
	 */
	public static boolean isOutTime(Object str) {
		if(str==null){
			return false;
		}
		try{
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = sDateFormat.parse(str.toString());

			return date.after(new Date());
		}catch(Exception e){
			LOG.error(e.toString());
			return false;
		}
		
	}
	
	
	/**
	 * 字符串匹配
	 * @param srcStr
	 * @param value
	 * @return
	 */
	public static boolean iscontainValue(String srcStr,String value){
			Pattern pattern = Pattern
					.compile(value,
							Pattern.DOTALL);
			Matcher matcher = pattern.matcher(srcStr);
			if (matcher.find()) {
				return true;
			}else{
				return false;
			}
	}
	
	
	public static String getMatchPatternStr(String srcStr,String patternStr){
			Pattern pattern = Pattern.compile(patternStr);
			Matcher matcher = pattern.matcher(srcStr);
			if (matcher.find()) {
				return matcher.group(1).trim();
			}else{
				return null;
			}
	}
	
	/**
	 * 
	* @Title: getTransactionID 
	* @Description: 取流水号信息
	* @param @param originalMessage
	* @param @return    
	* @return String    
	* @throws
	 */
	public static String getNodeValue(String originalMessage,String nodeName) {
			Pattern pattern = Pattern
					.compile(
							"<"+nodeName+">(.*?)</"+nodeName+">",
							Pattern.DOTALL);
			Matcher matcher = pattern.matcher(originalMessage);
			if (matcher.find()) {
				return matcher.group(1).trim();
			}else{
				return null;
			}
	}
	
	
	
	/**
	 * 对认证密码做加密处理
	 * @param xmlStr
	 * @param pwd
	 * @return
	 */
	public static String encrypt(String xmlStr,String pwd){
			Pattern pattern = Pattern
					.compile(
							"<SrcSysSign>"+pwd+"</SrcSysSign>",
							Pattern.DOTALL);
			Matcher matcher = pattern.matcher(xmlStr);
			if (matcher.find()) {
				return matcher.replaceFirst("<SrcSysSign>******</SrcSysSign>");
			}else{
				return xmlStr;
			}
	}
	/**
	 * 计算下一时间周期
	 * @param cycleType
	 * @param cycleValue
	 * @return
	 */
	public static String addEndTime(String cycleType,String cycleValue){
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		if(CommonUtil.MINUTE.equals(cycleType)){
			ca.add(Calendar.MINUTE, new Integer(cycleValue));
		} else if(CommonUtil.HOUR.equals(cycleType)){
			ca.add(Calendar.HOUR, new Integer(cycleValue));
		}
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return sDateFormat.format(ca.getTime());
		
	}
	/**
	 * 
	* @Title: asciiSort 
	* @Description: ascii排序
	* @param @param str
	* @param @return    
	* @return String    
	* @throws
	 */
	public static String asciiSort(String str){
		char[] chars = str.toCharArray();
		int i,j;
		int l=chars.length;
		for(i=l-1;i>0;i--){
			 for(j=0;j<i;j++){
				 if(chars[j]>chars[j+1]){
					 char t=chars[j];
					 chars[j]=chars[j+1];
					 chars[j+1]=t;
				 }
			 }
		}
		
		return new String(chars);
	}
	
	/**
	 * 计算时间差
	 * @param beingTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public static long subTime(String beingTime,String endTime) throws Exception{
		if(beingTime==null || beingTime.equals("") 
				|| endTime==null ||endTime.equals("")){
			return 0;
		}
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date beingDate = sDateFormat.parse(beingTime);
		Date endDate = sDateFormat.parse(endTime);
	    long diff = endDate.getTime() - beingDate.getTime();

		return diff;
	}
	
	/**
	 * 对象转换成字符串
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String ObjtoStr(Object obj) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();  
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(obj);
		byte[] objstr = out.toByteArray();
		return new String(objstr);
	}
	
	/**
	 * 
	 * @param client_id
	 * @param key
	 * @return
	 */
	public static String generalAccessToken(String client_id,String key){

		return StringUtil.Md5(client_id+key+System.currentTimeMillis()+StringUtil.makeAwardNo(9999));
		
	}
	
	/** 
     * 将Clob转成String ,静态方法 
     *  
     * @param clob 
     *            字段 
     * @return 内容字串，如果出现错误，返回 null 
     */  
    public static String clobToString(Clob clob) {  
        if (clob == null)  
            return null;  
        StringBuffer sb = new StringBuffer();  
        Reader clobStream = null;  
        try {  
            clobStream = clob.getCharacterStream();  
            char[] b = new char[60000];// 每次获取60K   
            int i = 0;  
            while ((i = clobStream.read(b)) != -1) {  
                sb.append(b, 0, i);  
            }  
        } catch (Exception ex) {  
            sb = null;  
        } finally {  
            try {  
                if (clobStream != null) {  
                    clobStream.close();  
                }  
            } catch (Exception e) {  
            	LOG.error("occur exception:",e);
            }  
        }  
        if (sb == null)  
            return null;  
        else  
            return sb.toString();  
    }  

	
	public static String getUUID(){
		String s = UUID.randomUUID().toString();
		return s;
        
	}
	
	
	public static String getResponseDesc(String responseDesc){
		if(null == responseDesc){
			return "";
		}
		if(responseDesc.length()>4096){
			return responseDesc.substring(0, 4096);
		}
		return responseDesc;
	}

}
