package threePigs.fishUrl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.HtmlParser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
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

    public int getIfBadActionExist() throws MalformedURLException {
        int exist = 0;
        Elements forms = htmlParser.getForms();

        for (Element form: forms) {
            String actionAttr = form.attr("action");
            String[] actionSplit = actionAttr.split("/");
            URL url = new URL(htmlParser.getUrl());
            if (actionSplit.length <= 1 || !ifSameDomain(htmlParser.getUrl(), actionAttr)){   //为空或单个文件名，或指向与页面不同域
                exist = 1;
                return exist;
            }
        }

        return exist;
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

    public static void main(String args[]) throws MalformedURLException {
        File file = new File("test2.html");
        InputStreamReader reader = null;
        StringWriter writer = new StringWriter();
        try {
                reader = new InputStreamReader(new FileInputStream(file));
            //将输入流写入输出流
            char[] buffer = new char[1024];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                writer.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        //返回转换结果
        if (writer != null) {
            String html = writer.toString();
            HtmlParser htmlParser = new HtmlParser("http://www.13335926308.com",html);
            ContentFilter filter = new ContentFilter(htmlParser);
            System.out.println(filter.getNullLinkCount());
            System.out.println(filter.getStaticLinkCount());
            System.out.println(filter.getIfBadFormExist());
            System.out.println(filter.getIfBadActionExist());
        }
    }
}
