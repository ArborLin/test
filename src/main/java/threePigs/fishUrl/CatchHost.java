package main.java.threePigs.fishUrl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import main.java.parser.HtmlParser;
/**
 * @author LYH
 * @description  获取网页链接中域名出现最频繁的项
 * */
public class CatchHost {
	HtmlParser htmlParser;
	
	/**
	 * 获取网页链接中域名出现最频繁的项
	 * @return string
	 * @throws Exception 
	 * */
	
	public String getFrequentHost() throws Exception{
		Elements links = htmlParser.getLinks();
		String hostFreq = "";		
		Map<String, Integer> hostMap = new HashMap<String, Integer>();
		
		//统计每个host的次数
        for (Element link: links){
        	
            String linkHref = link.attr("href");
            URL aUrl = new URL(linkHref); 
            String host = aUrl.getHost();  
            Integer count = hostMap.get(host);
            if(count == null){
            	hostMap.put(host, 1);
            }else {
				hostMap.put(host, ++count);
			}
        }
        
        //得到最频繁出现的host
        for(Map.Entry<String, Integer> entry : hostMap.entrySet()){
        	int max = 0;
        	if(entry.getValue()>max){
        		max = entry.getValue();
        		hostFreq = entry.getKey();
        	}
        }
        
        return hostFreq;
        
	}
}
