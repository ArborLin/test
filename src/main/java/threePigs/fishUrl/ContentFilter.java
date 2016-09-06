package threePigs.fishUrl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.HtmlParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arbor vlinyq@gmail.com
 * @version 2016/9/3
 */
public class ContentFilter {
    HtmlParser htmlParser;

    public ContentFilter(HtmlParser htmlParser) {
        this.htmlParser = htmlParser;
    }

    /**
     * 获取页面中空链接数目（为""或"?"）
     * @return count 空链接数
     */
    public int getNullLinkCount() {
        int count = 0;
        Elements links = htmlParser.getLinks();

        for (Element link: links){
            String linkHref = link.attr("href");
            if (linkHref.equals("") || linkHref.equals("?")){
                count++;
            }
        }
        return count;
    }

    public Double getNullLinkRatio() {
        int nullLinkCount = getNullLinkCount();
        Elements links = htmlParser.getLinks();
        if (links.size()!=0)
            return (double) nullLinkCount /(links.size());
        else
            return Double.valueOf(0);
    }

    /**
     * 获取指向静态链接的数目
     * @return count 静态链接计数
     */
    public int getStaticLinkCount() {
        int count = 0;
        Elements links = htmlParser.getLinks();

        for (Element link: links) {
            String linkHref = link.attr("href");
            if (linkHref.contains(".html")) {
                System.out.println(linkHref);
                count++;
            }
        }
        return count;
    }

    /**
     * 判断是否存在潜在有害表单
     * @return exist 0-不存在，1-存在
     * @throws MalformedURLException
     */
    public int getIfBadFormExist() throws MalformedURLException {
        int exist = 0;
        Elements forms = htmlParser.getForms();

        for (Element form: forms) {
            Elements inputField = form.select("input");

            if (inputField.size() == 0) { // form表单中不含input域
                Element formAncestor = form.parent().parent(); // 向上两级，继续寻找input
                Elements input = formAncestor.select("input");

                if (input.size()==0)
                    continue;
            }

            String formHtml = form.html();

            if (!ifContainsKeywords(formHtml)){ // 不含login关键词
                if (form.getElementsByAttributeValueContaining("type","search").size()==0 ||
                        form.getElementsByAttributeValueContaining("class","search").size()==0){    // 不为搜索框，向上两级搜索login关键词
                    Element formAncestor = form.parent().parent(); // 向上两级，寻找是否含有login关键词

                    if(!ifContainsKeywords(formAncestor.html())) // 向上两级仍不含
                        continue;
                } else
                    continue;
            }

            // 确认有form、input、login关键词|过多图片， 进而检查action域
            String actionAttr = form.attr("action");
            String url = htmlParser.getUrl();
            URL aUrl = new URL(url);

            if ((actionAttr==null || actionAttr.equals("") || !actionAttr.contains("http")) && aUrl.getProtocol().equals("http")) { //action域为空或为相对地址，且本页URL不为https
                exist = 1;
                return exist;
            } else if (actionAttr!=null && !actionAttr.contains("https")) {
                exist = 1;
                return exist;
            }

        }

        return exist;
    }

    /**
     * 是否存在潜在有害action域
     * @return
     * @throws MalformedURLException
     */
    public int getIfBadActionExist() throws MalformedURLException {
        int exist = 0;
        Elements forms = htmlParser.getForms();

        for (Element form: forms) {
            String actionAttr = form.attr("action");
            String[] actionSplit = actionAttr.split("/");
            if (actionSplit.length <= 1 || !ifSameDomain(htmlParser.getUrl(), actionAttr)){   //为空或单个文件名，或指向与页面不同域
                exist = 1;
                return exist;
            }
        }

        return exist;
    }

    /**
     * 获取最频繁Host
     * @return
     * @throws Exception
     */
    public String getFrequentHost() throws Exception{
        Elements links = htmlParser.getLinks();
        String hostFreq = "";
        Map<String, Integer> hostMap = new HashMap<String, Integer>();

        //统计每个host的次数
        for (Element link: links){

            String linkHref = link.attr("href");
            if (!linkHref.contains(":/"))
                linkHref = htmlParser.getUrl();
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


    public int getFreqUrlMatch() throws Exception {
        int match = 0;
        URL pageUrl = new URL(htmlParser.getUrl());
        String freqHost = getFrequentHost();
        if (pageUrl.getHost().equals(freqHost))
            match = 1;

        return match;
    }

    /**
     * 判断是否同域
     * @param url
     * @param actionUrl
     * @return
     */
    private boolean ifSameDomain(String url, String actionUrl) throws MalformedURLException {
        boolean same = false;
        URL pageUrl = new URL(url);
        if (actionUrl.contains(":/")) {//绝对路径
            URL actionURL = new URL(actionUrl);
            if (actionURL.getHost().equals(pageUrl.getHost()))
                same = true;
        } else {
            same = true;
        }

        return same;
    }

    private boolean ifContainsKeywords(String s) {
        boolean flag = false;
        String keywords = "username|password|passcode|login|telephone|phone|number|address|card|mail" +
                "登录|姓名|电话|手机|地址|住址|卡号|信用|身份证|邮箱|申请|denglu|xingming|dianhua|shouji|dizhi|xinyong|shenfenzheng|youxiang";
        Matcher m = Pattern.compile(keywords).matcher(s);
        if(m.find()){
            System.out.println(m.group());
            flag = true;
        }
        return flag;
    }
}
