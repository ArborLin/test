package threePigs.fishUrl;


/**
 * Created by zsc on 2016/9/14.
 */
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

import java.io.IOException;

public class HtmlFilter {
    private String url;
    private String html;
    private String[] urlFeature = {"55fqwiofj.55ioajdoias", "13335926308", "seaskytools",
            "idong88", "v7027545.4202.30la", "chenxiaomao", "wap.baicaidz",
            "xn---1-8w2c3v.clskckt", "haihekr", "jshelu", "z386189.ip5w5.tumm.top",
            "xn--1-lp6axr.lnhzlaw", "xn--jd-9j4d569afv8b.lovenancheng", "liuyanguestc",
            "june19921228"};

    //    private String[] feature = {"if(window.ie6) remoteURL=(remoteURL.substring(0,4)=='http')?remoteURL:remoteURL;",
//            "statics/icons/thridparty0.gif",
//            "www.banggood.com",
//            "action=\"send_passed.asp\"","action=\"Mobile_phone.asp\""};
    public HtmlFilter(String url, String html) {
        this.url = url;
        this.html = html;
    }

    public int getLabel() {
        int label = 0;

        for (int i = 0; i < urlFeature.length; i++) {
            if (url.contains(urlFeature[i])) {
                label = 1;
                continue;
            }
        }

//        if (label == 1) {
//            return label;
//        } else {
//            try {
//                HttpClient client = new HttpClient();
//                HttpMethod method = new GetMethod(url);
//                client.executeMethod(method);
//                int statusCode = method.getStatusCode();
//                if ((statusCode== HttpStatus.SC_MOVED_TEMPORARILY) || (statusCode == HttpStatus.SC_MOVED_PERMANENTLY) ||
//                        (statusCode == HttpStatus.SC_SEE_OTHER) || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
//                    label = 1;
//                }
//            } catch (IOException e) {
//                label = 1;
//            }
//
//        }
        return label;
    }

//    public static void main(String args[]) throws MalformedURLException {
//        String url = "132.html";
//        File file = new File(url);
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
//            HtmlFilter filter = new HtmlFilter(url, html);
//            System.out.println("结果 " + filter.getLabel());
//        }
//    }

}
