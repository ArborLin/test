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
    public int getNullLinkCount() {
        int count = 0;
        Elements links = htmlParser.getLinks();

        for (Element link: links){
            String linkHref = link.attr("href");
            if (linkHref.equals("")){
                count++;
            }
        }
        return count;
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
        }
    }
}
