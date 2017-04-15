package cn.imkarl.urlbuilder;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * UrlBuilder单元测试
 */
public class UrlBuilderUnitTest {
    private static final String TAG = "UrlBuilder";

    private static void log(Object msg) {
        System.out.println(TAG+": "+(msg==null ? "[NULL]" : String.valueOf(msg)));
    }


    @Test
    public void testSimple() throws Exception {
        final String url = "http://www.baidu.com/";
        String buildUrl = new UrlBuilder().host("www.baidu.com").build();

        log("------------ testSimple ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }

    @Test
    public void testHost() throws Exception {
        final String url = "https://www.baidu.com/";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").build();

        log("------------ testHost ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }
    @Test
    public void testHost2() throws Exception {
        final String url = "https://www.baidu.com/";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").path(new UrlPath().endTag(false)).build();

        log("------------ testHost2 ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }
    @Test
    public void testHostByPort() throws Exception {
        final String url = "https://www.baidu.com:8090/";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").port(8090).build();

        log("------------ testHost2 ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }

    @Test
    public void testQuery() throws Exception {
        final String url = "https://www.baidu.com/s?ie=UTF-8&wd=test";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").appendPath("/s").appendQuery("ie", "UTF-8").appendQuery("wd", "test").build();

        log("------------ testQuery ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }
    @Test
    public void testQueryByChinese() throws Exception {
        final String url = "https://www.baidu.com/s?ie=UTF-8&wd=%E6%B5%8B%E8%AF%95";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").appendPath("/s").appendQuery("ie", "UTF-8").appendQuery("wd", "测试").build();

        log("------------ testQueryByChinese ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }
    @Test
    public void testQueryByMulti() throws Exception {
        final String url = "https://www.baidu.com/s?ie=UTF-8&ie=GBK&wd=%E6%B5%8B%E8%AF%95";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").appendPath("/s").appendQuery("ie", "UTF-8").appendQuery("ie", "GBK").appendQuery("wd", "测试").build();

        log("------------ testQueryByMulti ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }
    @Test
    public void testQueryByOverride() throws Exception {
        final String url = "https://www.baidu.com/s?ie=GBK&wd=%E6%B5%8B%E8%AF%95";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").appendPath("/s").putQuery("ie", "UTF-8").putQuery("ie", "GBK").putQuery("wd", "测试").build();

        log("------------ testQueryByOverride ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }

    @Test
    public void testFragment() throws Exception {
        final String url = "https://www.baidu.com/#abc";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").fragment("abc").build();

        log("------------ testFragment ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }
    @Test
    public void testFragmentByChinese() throws Exception {
        final String url = "https://www.baidu.com/#%E6%B5%8B%E8%AF%95";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").fragment("测试").build();

        log("------------ testFragmentByPath ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }
    @Test
    public void testFragmentByPath() throws Exception {
        final String url = "https://www.baidu.com/#abc";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").fragment("abc").build();

        log("------------ testFragmentByPath ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }
    @Test
    public void testFragmentByPath2() throws Exception {
        final String url = "https://www.baidu.com/s#abc";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").appendPath("/s").fragment("abc").build();

        log("------------ testFragmentByPath ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }
    @Test
    public void testFragmentByPathAndQuery() throws Exception {
        final String url = "https://www.baidu.com/s?wd=test#abc";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").appendPath("/s").appendQuery("wd", "test").fragment("abc").build();

        log("------------ testFragmentByPathAndQuery ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }
    @Test
    public void testFragmentByQuery() throws Exception {
        final String url = "https://www.baidu.com/?wd=test#abc";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").appendQuery("wd", "test").fragment("abc").build();

        log("------------ testFragmentByPathAndQuery ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }

    @Test
    public void testComplex() throws Exception {
        final String url = "https://www.baidu.com/s?ie=UTF-8&wd=%E6%B5%8B%E8%AF%95&tfflag=1&abc=one&abc=two#bbb";
        String buildUrl = new UrlBuilder().scheme("https").host("www.baidu.com").appendPath("/s")
                .appendQuery("ie", "UTF-8")
                .putQuery("wd", "test")
                .putQuery("wd", "测试")
                .appendQuery("tfflag", "0")
                .putQuery("tfflag", "1")
                .appendQuery("abc", "one")
                .appendQuery("abc", "two")
                .fragment("aaa")
                .fragment("bbb")
                .build();

        log("------------ testComplex ------------");
        log("original=\t"+url);
        log("buildUrl=\t"+buildUrl);
        assertTrue(url.equals(buildUrl));
    }

}