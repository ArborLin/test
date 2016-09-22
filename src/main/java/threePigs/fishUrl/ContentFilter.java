package threePigs.fishUrl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.HtmlParser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
    String url;
    String host;

    public ContentFilter(HtmlParser htmlParser, String url) throws MalformedURLException{
        this.htmlParser = htmlParser;
        this.url = url;
        this.host = new URL(url).getHost();
    }


//    public void getAllLinks() {
//        Elements links = htmlParser.getDoc().select("a[href]"); //"a[href]" //带有href属性的a元素
//        Elements media = htmlParser.getDoc().select("[src]");   //利用属性查找元素，比如：[href]
//        Elements imports = htmlParser.getDoc().select("link[href]");
//
//        print("\nMedia: (%d)", media.size());
//        for (Element src : media) {
//            if (src.tagName().equals("img"))
//                print(" * %s: <%s> %sx%s (%s)",
//                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
//                        trim(src.attr("alt"), 20));//src.attr("src")结果:<y18.gif> 18x18 ()
//                //src.attr("abs:src")结果:<https://news.ycombinator.com/y18.gif> 18x18 ()
//            else
//                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
//        }
//
//        print("\nImports: (%d)", imports.size());
//        for (Element link : imports) {
//            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
//        }
//
//        print("\nLinks: (%d)", links.size());
//        for (Element link : links) {
//            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
//        }
//    }
//
//    private static void print(String msg, Object... args) {
//        System.out.println(String.format(msg, args));
//    }
//
//    private static String trim(String s, int width) {
//        if (s.length() > width)
//            return s.substring(0, width-1) + ".";
//        else
//            return s;
//    }


    /**
     * 获取页面身份
     *
     * @return
     */
    public double getFrequentHost() {
        int count = 0;
        Elements links = htmlParser.getLinks();
        for (Element link : links) {
            String absHref = link.attr("abs:href");
            if (absHref.equals("")) {
                count++;
            }
        }
        count = count - getNullLinkCount();
        for (Element link : links) {
            String linkHref = link.attr("abs:href");
            System.out.println("afreq: " + linkHref);
            if (linkHref.contains(host)) {
                count++;
            }
        }
        if (links.size() != 0) {
            return (double) count / links.size();
        } else {
            return 0;
        }
    }


    /**
     * 获取页面中空链接数目（为""或"?"）
     *
     * @return count 空链接数
     */
    public int getNullLinkCount() {
        int count = 0;
        Elements links = htmlParser.getLinks();

        System.out.println("size: " + links.size());
        for (Element link : links) {
            String linkHref = link.attr("href").replace(" ","");
            System.out.println("linkHref: " + linkHref);
            if (linkHref.equals("") || linkHref.equals("?") || linkHref.equals("#")) {
                count++;
            }
        }
        return count;
    }

    public double getNullLinkRatio() {
        int nullLinkCount = getNullLinkCount();
        Elements links = htmlParser.getLinks();
        if (links.size() != 0)
            return (double) nullLinkCount / (links.size());
        else
            return 0;
    }


    //判断是否存在表单
    public int getIfFormExist() throws MalformedURLException {
        int exist = 0;
        Elements forms = htmlParser.getForms();
        if (forms.size() == 0) {
            exist = 0;
        } else {
            exist = 1;
        }
        return exist;
    }

    /**
     * 判断input个数
     * @return
     */
    public int getInputNum() {
        int count = 0;
        Elements inputs = htmlParser.getInput();
        for (Element input : inputs) {
            String type = input.attr("type").toLowerCase();
            if (type.equals("text") || type.equals("submit") || type.equals("password")) {
                count++;
            }
        }
        return count;
    }

//    //判断input个数
//    public int getInputNum() throws MalformedURLException {
//        int count = 0;
//        Elements forms = htmlParser.getForms();
//
//        for (Element form : forms) {
//            Elements inputField = form.select("input");
//            if (inputField.size() == 0) { // form表单中不含input域
//                Element formAncestor = form.parent().parent(); // 向上两级，继续寻找input
//                Elements input = formAncestor.select("input");
//                if (input.size() == 0) {
//                    continue;
//                } else {
//                    count = count + input.size();
//                }
//            } else {
//                Element formAncestor = form.parent().parent(); // 向上两级，继续寻找input
//                Elements input = formAncestor.select("input");
//                if (input.size() == 0) {
//                    continue;
//                } else {
//                    count = count + input.size();
//                }
//            }
//        }
//
//        return count;
//    }
//
//    public int getInputKeyNum() throws MalformedURLException {
//        int exist = 0;
//        Elements inputs = htmlParser.getDoc().select("input");
//        System.out.println(inputs.size());
//        if (getInputNum() != 0) {
//            for (Element input : inputs) {
////                Elements inputs = form.select("input");
//                System.out.println("input: " + inputs.size());
//
//                String formHtml = input.toString();
//                System.out.println("fff " + formHtml);
//                System.out.println("进入for");
//                if (ifContainsKeywords(formHtml) == 0) { // 不含login关键词
//                    System.out.println("外面的if");
////                        Element formAncestor = form.parent().parent(); // 向上两级，寻找是否含有login关键词
////                        if (ifContainsKeywords(formAncestor.html()) == 0) {
////                            System.out.println("if里面的if");
////                            continue;
////                        } else {
////                            System.out.println("if里面的else");
////                            exist = exist + ifContainsKeywords(formAncestor.select("input").html());
////                        }
//                    continue;
//                } else {
////                        Element formAncestor = form.parent().parent(); // 向上两级，寻找是否含有login关键词
////                        if (ifContainsKeywords(formAncestor.select("input").html()) == 0) {
////                            System.out.println("else里面的if");
////                            continue;
////                        } else {
////                            System.out.println("else里面的else");
////                            exist = exist + ifContainsKeywords(formAncestor.select("input").html());
////                        }
//                    exist = exist + ifContainsKeywords(formHtml);
//
//
//                }
//
//
//            }
//        }
//        return exist;
//    }


//    /**
//     * 判断是否存在潜在有害表单
//     *
//     * @return exist 0-不存在，1-存在
//     * @throws MalformedURLException
//     */
//    public int getIfBadFormExist() throws MalformedURLException {
//        int exist = 0;
//        Elements forms = htmlParser.getForms();
//
//        for (Element form: forms) {
//            Elements inputField = form.select("input");
//
//            if (inputField.size() == 0) { // form表单中不含input域
//                Element formAncestor = form.parent().parent(); // 向上两级，继续寻找input
//                Elements input = formAncestor.select("input");
//
//                if (input.size()==0)
//                    continue;
//            }
//
//            String formHtml = form.html();
//
//            if (!ifContainsKeywords(formHtml)){ // 不含login关键词
//                if (form.getElementsByAttributeValueContaining("type","search").size()==0 ||
//                        form.getElementsByAttributeValueContaining("class","search").size()==0){    // 不为搜索框，向上两级搜索login关键词
//                    Element formAncestor = form.parent().parent(); // 向上两级，寻找是否含有login关键词
//
//                    if(!ifContainsKeywords(formAncestor.html())) // 向上两级仍不含
//                        continue;
//                } else
//                    continue;
//            }
//
//            // 确认有form、input、login关键词|过多图片， 进而检查action域
//            String actionAttr = form.attr("action");
//            String url = htmlParser.getUrl();
//            URL aUrl = new URL(url);
//
//            if ((actionAttr==null || actionAttr.equals("") || !actionAttr.contains("http")) && aUrl.getProtocol().equals("http")) { //action域为空或为相对地址，且本页URL不为https
//                exist = 1;
//                return exist;
//            } else if (actionAttr!=null && !actionAttr.contains("https")) {
//                exist = 1;
//                return exist;
//            }
//
//        }
//
//        return exist;
//    }

    /**
     * 是否存在潜在有害action域
     *
     * @return
     * @throws MalformedURLException
     */
    public int getIfBadActionExist() throws MalformedURLException {
        int exist = 0;
        Elements forms = htmlParser.getForms();
        if (forms.size() > 0) {
            for (Element form : forms) {
                String actionAttr = form.attr("action").replace(" ","").toLowerCase();
                System.out.println("action  :  " + form.attr("action").equals(""));//action判断
                if (actionAttr.equals("#") || actionAttr.equals("?") || actionAttr.equals("")) { //action域为空或为相对地址，且本页URL不为https
                    exist = 1;
                } else if (actionAttr.contains("login") || actionAttr.contains("admin") || actionAttr.contains("search")) {
                    exist = 0;
                } else {
                    exist = 1;
                }
            }
        }
        return exist;
    }

//    /**
//     * 获取最频繁Host
//     *
//     * @return
//     * @throws Exception
//     */
//    public String getFrequentHost() throws Exception {
//        Elements links = htmlParser.getLinks();
//        String hostFreq = "";
//        Map<String, Integer> hostMap = new HashMap<String, Integer>();
//
//        //统计每个host的次数
//        for (Element link : links) {
//
//            String linkHref = link.attr("href");
//            linkHref = linkHref.replaceAll("\\s", "");
//            URI uri = null;
//            try {
//                uri = new URI(linkHref);
//            } catch (Exception e) {
//                //System.out.println("URI异常");
//                e.printStackTrace();
//                continue;
//            }
//
//            if (uri.getScheme() != null && !uri.getScheme().equals("http") && !uri.getScheme().equals("https")) { // 忽略非http或https的其他协议，如tecent://
//                continue;
//            }
//            if (uri.getScheme() == null) {
//                linkHref = htmlParser.getUrl();
//            }
//            URL aUrl = null;
//            try {
//                aUrl = new URL(linkHref);
//            } catch (Exception e) {
//                System.out.println("错误LinkHref " + linkHref);
//                e.printStackTrace();
//                continue;
//            }
//
//            String host = aUrl.getHost();
//            Integer count = hostMap.get(host);
//            if (count == null) {
//                hostMap.put(host, 1);
//            } else {
//                hostMap.put(host, ++count);
//            }
//        }
//
//        //得到最频繁出现的host
//        int max = 0;
//        for (Map.Entry<String, Integer> entry : hostMap.entrySet()) {
//            if (entry.getValue() > max) {
//                max = entry.getValue();
//                hostFreq = entry.getKey();
//            }
//        }
//
//        return hostFreq;
//
//    }
//
//
//    public int getFreqUrlMatch() throws Exception {
//        int match = 0;
//        URL pageUrl = new URL(htmlParser.getUrl());
//        String freqHost = getFrequentHost();
//        if (pageUrl.getHost().equals(freqHost))
//            match = 1;
//
//        return match;
//    }
//
//    /**
//     * 判断是否同域
//     *
//     * @param url
//     * @param actionUrl
//     * @return
//     */
//
//    private boolean ifSameDomain(String url, String actionUrl) throws MalformedURLException {
//        boolean same = false;
//        URL pageUrl = new URL(url);
//        if (actionUrl.contains(":/")) {//绝对路径
//            URL actionURL = null;
//            try {
//                actionURL = new URL(actionUrl);
//            } catch (MalformedURLException e) {
//                //action域格式异常，默认为不同域
//                return false;
//            }
//
//            if (actionURL.getHost().equals(pageUrl.getHost()))
//                same = true;
//        } else {
//            same = true;
//        }
//
//        return same;
//    }

//    private int ifContainsKeywords(String s) {
//        int count = 0;
//        String[] keywords = {"card", "mail",
//                "account", "bank", "mobile", "password", "id", "qq",
//                "类型", "姓名", "手机", "卡号", "银行",
//                "信用", "身份证", "军官证", "护照", "邮箱",
//                "xingming", "shouji", "kahao",
//                "yinhang", "xinyong", "shenfenzheng", "junguanzheng", "huzhao", "youxiang", "leixing"};
//        String[] keywords2 = {"telephone", "phone", "address", "card", "mail",
//                "account", "bank", "mobile", "password", "qq",
//                "类型", "姓名", "电话", "手机", "地址", "住址", "卡号", "银行",
//                "信用", "身份证", "军官证", "护照", "邮箱", "公司", "职务", "职业", "行业", "类型",
//                "xingming", "dianhua", "shouji", "dizhi", "zhuzhi", "kahao",
//                "yinhang", "xinyong", "shenfenzheng", "junguanzheng", "huzhao", "youxiang", "leixing"};
//
////        Matcher m = Pattern.compile(keywords).matcher(s.toLowerCase());
////        while (m.find()) {
////            count += 1;
////        }
//        for (int i = 0; i < keywords2.length; i++) {
//            if (s.contains(keywords2[i])) {
//                System.out.println("信息： " + keywords2[i]);
//                count += 1;
//            }
//        }
//        return count;
//    }
//
//
//    public static void main(String args[]) throws Exception {
//        File file = new File("src/main/java/isFish/normalLogin5.html");
//        InputStreamReader reader = null;
//        StringWriter writer = new StringWriter();
//        try {
//            reader = new InputStreamReader(new FileInputStream(file));
//            //将输入流写入输出流
//            char[] buffer = new char[1024];
//            int n = 0;
//            while (-1 != (n = reader.read(buffer))) {
//                writer.write(buffer, 0, n);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (reader != null)
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//        }
//        //返回转换结果
//        if (writer != null) {
//            String html = writer.toString();
//            String url = "http://www.iCloud.com";
//            HtmlParser htmlParser = new HtmlParser("http://www.meitiancafe.com", html);
//            ContentFilter filter = new ContentFilter(htmlParser, url);
//
////            System.out.println("key: " + filter.getInputKeyNum());
////            System.out.println("num: " + filter.getInputNum());
//            System.out.println("null: " + filter.getNullLinkCount());
//            System.out.println("freq: " + filter.getFrequentHost());
//            System.out.println("input: " + filter.getInputNum());
//            System.out.println("a: " + filter.getIfBadActionExist());
//        }
//    }

//    public int getFreqUrlMatch() throws Exception {
//        int match = 0;
//        URL pageUrl = new URL(htmlParser.getUrl());
//        String freqHost = getFrequentHost();
//        if (pageUrl.getHost().equals(freqHost))
//            match = 1;
//
//        return match;
//    }
//
//    /**
//     * 判断是否同域
//     * @param url
//     * @param actionUrl
//     * @return
//     */
//    private boolean ifSameDomain(String url, String actionUrl) throws MalformedURLException {
//        boolean same = false;
//        URL pageUrl = new URL(url);
//        if (actionUrl.contains(":/")) {//绝对路径
//            URL actionURL = null;
//            try{
//                actionURL = new URL(actionUrl);
//            }catch (MalformedURLException e){
//                //action域格式异常，默认为不同域
//                return false;
//            }
//
//            if (actionURL.getHost().equals(pageUrl.getHost()))
//                same = true;
//        } else {
//            same = true;
//        }
//
//        return same;
//    }
//
//    private boolean ifContainsKeywords(String s) {
//        boolean flag = false;
//        String keywords = "username|password|passcode|login|telephone|phone|number|address|card|mail" +
//                "登录|姓名|电话|手机|地址|住址|卡号|信用|身份证|邮箱|申请|denglu|xingming|dianhua|shouji|dizhi|xinyong|shenfenzheng|youxiang";
//        Matcher m = Pattern.compile(keywords).matcher(s);
//        if(m.find()){
////            System.out.println(m.group());
//            flag = true;
//        }
//        return flag;
//    }
//
//    public static void main(String args[]) {
//        try {
//            String link = "http://baidu.com";
//            link = link.replaceAll("\\s","");
//            URI uri = new URI(link);
//            URL url = new URL(link);
//            System.out.println(uri.getScheme());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//    }
}
