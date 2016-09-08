package threePigs.fishUrl;

import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.UDTF;
import com.aliyun.odps.udf.annotation.Resolve;
import parser.HtmlParser;


/**
 * @author Arbor vlinyq@gmail.com
 * @version 2016/9/3
 */
@Resolve({"string,string->string,bigint,bigint,bigint,bigint,bigint,bigint,double,bigint,bigint,bigint,bigint"})
public class MyUDF extends UDTF {

    @Override
    public void process(Object[] objects) throws UDFException {
        String url = (String) objects[0];
        String html_content = (String) objects[1];

        HtmlParser htmlParser = new HtmlParser(url,html_content);
        ContentFilter filter = new ContentFilter(htmlParser);
        UrlFeacture urlFeature = new UrlFeacture();
        int[] urlFeatureValue = null;
        try {
            urlFeatureValue = urlFeature.process(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* 特征     网站URL       协议是否为https       是否包含IP          端口号是否为80        域名级数             数字个数        异常字符数
         *          空链接比例         指向静态html文件链接数                存在潜在有害form         潜在有害action    最频繁域匹配
         */
        try {
            forward(url, (long)urlFeatureValue[0],(long)urlFeatureValue[1], (long)urlFeatureValue[2], (long)urlFeatureValue[3], (long)urlFeatureValue[4],(long)urlFeatureValue[5],
                    filter.getNullLinkRatio(), (long)filter.getStaticLinkCount(), (long)filter.getIfBadFormExist(), (long)filter.getIfBadActionExist(),(long) filter.getFreqUrlMatch());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
