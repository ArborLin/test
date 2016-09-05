package threePigs.fishUrl;

import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.UDTF;
import parser.HtmlParser;

/**
 * @author Arbor vlinyq@gmail.com
 * @version 2016/9/3
 */
public class MyUDF extends UDTF {

    @Override
    public void process(Object[] objects) throws UDFException {
        String url = (String) objects[0];
        String html_content = (String) objects[1];
    }
}
