package main.java.threePigs.fishUrl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aliyun.odps.udf.UDF;
import com.aliyun.odps.udf.UDFException;
/**@author LYH
 * 提取URL的特征
 * */
public class UrlFeacture extends UDF {

	public int[] process(Object[] objects) throws Exception {
        //String url = (String) objects[0];
		String html_content = (String) objects[1];
        String url = "http://www.baicaidz.com";
        URL aUrl = new URL(url);                
        String protocol = aUrl.getProtocol();
        String host = aUrl.getHost();
        int port = aUrl.getPort(); 
        
        //统计结果
        int isPro = dealProtocol(protocol); //判断协议是否为http或https
        int hasIp = dealHost(host); //判断是否有ip
        int isPort = dealPort(port); //判断端口号是否为80
        int numPoint = gainIPseries(host); //统计ip级数（即ip中.的个数）
        int numDigit = gainDigitNum(host); //统计数字个数
        int numAbChar = gainAbChar(host); //统计异常字符和汉字的个数
        int[] urlFeacture = {isPro,hasIp,isPort,numPoint,numDigit,numAbChar};
        return urlFeacture;
    }
	/**
	 * @title dealProtocol
	 * @description 协议为http或https 返回1;否则返回0
	 * @return int*/
	public int dealProtocol(String pro){
		int flag = 0;
		if(pro=="http"||pro=="https"){
			flag = 1;
		}else{
			flag = 0;
		}
		return flag;
	}
	/**
	 * @title dealHost
	 * @description 有IP 返回1;否则返回0
	 * @return int*/
	public int dealHost(String host){
		int flag = 0;
		if(host != null){
			flag = 1;
		}else{
			flag = 0;
		}
		return flag;
	}
	/**
	 * @title dealPort
	 * @description 端口号为80或者缺省 返回1;否则返回0
	 * @return int*/
	public int dealPort(int port){
		int flag = 0;
		if(port == 80 || port == -1){
			flag = 1;
		}else{
			flag = 0;
		}
		return flag;
	}
	/**
	 * @title gainIPseries
	 * @description 获取IP级数，即ip中“.”的个数
	 * @return int*/
	public int gainIPseries(String host){
		int num = 0;
		if(host == null){
			num = 0;
		}
		else {
			String[] str = host.split(".");
			num = str.length;
		}
		return num;
	}
	/**
	 * @title gainDigitNum
	 * @description 获取host中数字的个数
	 * @return int*/
	public int gainDigitNum(String host){
		int num = 0;
		char[] ch = host.toCharArray();
		for(int i=0;i<ch.length;i++){
			if(ch[i]<=57 && ch[i]>=48){
				num++;
			}
		}
		return num;
	}
	/**
	 * @title gainDigitNum
	 * @description 获取host中异常字符（",',@,$,%,&,*,^,!,-,_,|,+,(,),{,},[,],汉字等）的个数
	 * @return int*/
	public int gainAbChar(String host){
		int num = 0; 
		char[] ch = host.toCharArray();
		//判断是否有汉字
	    String regEx = "[\\u4e00-\\u9fa5]";      	              	    
	    Pattern p = Pattern.compile(regEx);      
	    Matcher m = p.matcher(host);      
	    while (m.find()) {      
	        for (int i = 0; i <= m.groupCount(); i++) {      
	             num = num + 1;      
	         }      
	    }
	    //判断异常字符
	    for(int i=0;i<ch.length;i++){
	    	if(ch[i]>=33 && ch[i]<=45){
	    		num ++;
	    	}else if(ch[i]>=60 && ch[i]<=64){
	    		num++;
	    	}else if(ch[i]>=91 && ch[i]<=96){
	    		num++;
	    	}else if(ch[i]>=123 && ch[i]<=126){
	    		num++;
	    	}
	    }
		return num;
	}
}
