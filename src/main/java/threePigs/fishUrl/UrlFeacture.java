package threePigs.fishUrl;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LYH
 *         提取URL的特征
 */
public class UrlFeacture {

    public int[] process(String url) throws Exception {
        //String url = (String) objects[0];
        URL aUrl = new URL(url);
        String protocol = aUrl.getProtocol();
        String host = aUrl.getHost();
        int port = aUrl.getPort();
        String path = aUrl.getPath();

        //统计结果
        int isPro = getProtocol(protocol); //判断协议是否为http或https
        int hasIp = getIp(host); //判断是否有ip
//        int isPort = dealPort(port); //判断端口号是否为80
        int pointNum = getIPseries(host); //统计域名级数（即域名中.的个数是否大于等于4）
//        int numDigit = gainDigitNum(host); //统计数字个数
        int pathNum = getPath(path);//统计路径长度
        int pathKey = getPathKey(path);//判断路径中是否有关键词
        int hasAt = getAt(host); //判断@
//        int hasLine = getLine(url);//判断-
        int maxLength = getMaxLength(url);//最大长度
//        int minLength = getMinLength(url);//最小长度


        int[] urlFeacture = {hasIp, pointNum, hasAt, maxLength};
        return urlFeacture;
    }


    /**
     *
     * @param path
     * @return 判断路径级数
     */
    public int getPath(String path) {
        int flag = 0;
        String[] pathNum = path.split("/");
        if ((pathNum.length - 1) >= 3) {
            flag = 1;
        }
        return flag;
    }

    public int getPathKey(String path) {
        int flag = 0;
        String[] keys = {"admin", "login", "banking", "signin"};
        for (int i = 0; i < keys.length; i++) {
            if (path.contains(keys[i])) {
                flag = 1;
            }
        }
        return flag;
    }


    /**
     * @return int
     * @title getProtocol
     * @description 协议为https 返回1;否则返回0
     */
    public int getProtocol(String pro) {
        int flag = 0;
        if (pro.equals("https")) {
            flag = 1;
        } else {
            flag = 0;
        }
        return flag;
    }

    /**
     * @return int
     * @title getIp
     * @description 有IP 返回1;否则返回0
     */
    public int getIp(String host) {
        String ip = "\\b.*([0-9]{1,3}\\.){3}[0-9]{1,3}.*\\b";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(host);
        if (matcher.matches()) {
            return 1;
        } else {
            return 0;
        }
    }

//    /**
//     * @return int
//     * @title dealPort
//     * @description 端口号为80或者缺省 返回1;否则返回0
//     */
//    public int dealPort(int port) {
//        int flag = 0;
//        if (port == 80 || port == -1) {
//            flag = 1;
//        } else {
//            flag = 0;
//        }
//        return flag;
//    }

    /**
     * @return int
     * @title getIPseries
     * @description 获取IP级数，即ip中“.”的个数
     */
    public int getIPseries(String host) {
        int num = 0;
        if (host == null) {
            num = 0;
        } else {
            String[] str = host.split("\\.");
            num = str.length;
        }
        if (num >= 5) {
            return 1;
        } else {
            return 0;
        }
    }

//    /**
//     * @return int
//     * @title gainDigitNum
//     * @description 获取host中数字的个数
//     */
//    public int gainDigitNum(String host) {
//        int num = 0;
//        char[] ch = host.toCharArray();
//        for (int i = 0; i < ch.length; i++) {
//            if (ch[i] <= 57 && ch[i] >= 48) {
//                num++;
//            }
//        }
//        return num;
//    }

//	/**
//	 * @title gainDigitNum
//	 * @description 获取host中是否包含异常字符（@,-）
//	 * @return int*/
//	public int gainAbChar(String host){
//		int num = 0;
//		char[] ch = host.toCharArray();
////		//判断是否有汉字
////	    String regEx = "[\\u4e00-\\u9fa5]";
////	    Pattern p = Pattern.compile(regEx);
////	    Matcher m = p.matcher(host);
////	    while (m.find()) {
////	        for (int i = 0; i <= m.groupCount(); i++) {
////	             num = num + 1;
////	         }
////	    }
//	    //判断异常字符
//	    for(int i=0;i<ch.length;i++){
//	    	if(ch[i]==64){
//	    		num ++;
//	    	}else if(ch[i]==45){
//	    		num++;
//	    	}
//	    }
//		if (num > 0) {
//			return 1;
//		} else {
//			return 0;
//		}
//	}

    //判断有无@
    public int getAt(String host) {
        int num = 0;
        char[] ch = host.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == 64) {
                num++;
            }
        }
        if (num > 0) {
            return 1;
        } else {
            return 0;
        }

    }

//    //判断有无-
//    public int getLine(String url) {
//        int num = 0;
//        char[] ch = url.toCharArray();
//        for (int i = 0; i < ch.length; i++) {
//            if (ch[i] == 45) {
//                num++;
//            }
//        }
//        if (num > 0) {
//            return 1;
//        } else {
//            return 0;
//        }
//
//    }

    //判断最大长度是否超过50
    public int getMaxLength(String url) {
        int num = url.length();
        if (num > 50) {
            return 1;
        } else {
            return 0;
        }
    }

//    //判断最小长度是否小于7
//    public int getMinLength(String url) {
//        int num = url.length();
//        if (num <= 7) {
//            return 1;
//        } else {
//            return 0;
//        }
//    }

    public static void main(String[] args) throws Exception {
        String url = "http://login.taobao.com/member/xclient.htm";
        URL aUrl = new URL(url);
        String protocol = aUrl.getProtocol();
        String host = aUrl.getHost();
        UrlFeacture urlFeacture = new UrlFeacture();
        int ip = urlFeacture.getIp(host);

        System.out.println("p: " + protocol);
        System.out.println("host " + host);
        System.out.println("ip: " + ip);
        System.out.println("级数 " + urlFeacture.getIPseries(host));
        System.out.println("at: " + urlFeacture.getAt(url));
    }

}
