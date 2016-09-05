package threePigs.fishUrl;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.HtmlParser;

import java.io.*;

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

    public int getIfBadFormExist() {
        int exist = 0;
        Elements forms = htmlParser.getForms();

        for (Element form: forms) {
            Elements inputField = form.select("input");

            if (inputField.size() == 0) { // form表单中不含input域
                continue;
            }



        }

        return exist;
    }

    public static void main(String args[]){
        File file = new File("test.html");
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
        }
    }
}
