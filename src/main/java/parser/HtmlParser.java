package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author Arbor vlinyq@gmail.com
 * @version 2016/9/3
 */
public class HtmlParser {
    String url;
    String html;
    Document doc;

    public HtmlParser(String url, String html) {
        this.url = url;
        this.html = html;
        this.doc = Jsoup.parse(html);
    }

    public Elements getLinks() {
        return doc.select("a[href]");
    }

    public Elements getImgs() {
        return doc.getElementsByTag("img");
    }
}
