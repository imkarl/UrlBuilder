package cn.imkarl.urlbuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * URL 查询字符串
 * @version imkarl 2017-04
 *
 * @see <a href="https://en.wikipedia.org/wiki/Query_string">Query string</a>
 */
public class UrlQuery {
    private List<Part<String, String>> params;

    public UrlQuery() {
    }

    /**
     * 添加query，不论是否已存在相同KEY
     */
    public UrlQuery append(String key, String value) {
        if (Util.isEmpty(key)) {
            return this;
        }

        key = key.trim();
        value = Util.isEmpty(value) ? "" : value.trim();

        if (this.params == null) {
            this.params = new ArrayList<>();
        }
        this.params.add(new Part<>(key, value));
        return this;
    }

    /**
     * 如果已存在相同KEY，则替换之前所有的
     */
    public UrlQuery put(String key, String value) {
        if (Util.isEmpty(key)) {
            return this;
        }

        key = key.trim();

        if (this.params != null && !this.params.isEmpty()) {
            Iterator<Part<String, String>> it = this.params.iterator();
            while(it.hasNext()) {
                Part<String, String> part = it.next();
                if (part.getKey().equals(key)) {
                    it.remove();
                }
            }
        }
        append(key, value);
        return this;
    }


    /**
     * 构建Query string，前面不带'?'（对key-value进行URL编码处理）
     * @return 如果没有任何内容，则返回空字符串""
     */
    public String build() {
        return build(true);
    }
    /**
     * 构建Query string，前面不带'?'
     * @param encode 是否需要对key-value进行URL编码处理
     * @return 如果没有任何内容，则返回空字符串""
     */
    public String build(boolean encode) {
        String query = "";
        if (params != null && !params.isEmpty()) {
            StringBuilder queryBuiler = new StringBuilder();
            for (Part<String, String> item : params) {
                if (encode) {
                    queryBuiler.append(UrlBuilder.encode(item.getKey())).append('=').append(UrlBuilder.encode(item.getValue())).append('&');
                } else {
                    queryBuiler.append(item.getKey()).append('=').append(item.getValue()).append('&');
                }
            }
            queryBuiler.deleteCharAt(queryBuiler.length() - 1);
            query = queryBuiler.toString();
        }
        return query;
    }


    public static UrlQuery from(Map<String, String> params) {
        UrlQuery urlQuery = new UrlQuery();
        urlQuery.params = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (Util.isNotEmpty(entry.getKey())) {
                    urlQuery.params.add(new Part<>(entry.getKey(), entry.getValue()));
                }
            }
        }
        return urlQuery;
    }
    public static UrlQuery from(Collection<Part<String, String>> params) {
        UrlQuery urlQuery = new UrlQuery();
        if (params != null && !params.isEmpty()) {
            urlQuery.params = new ArrayList<>(params);
        } else {
            urlQuery.params = new ArrayList<>();
        }
        return urlQuery;
    }
    public static UrlQuery from(Part<String, String>... params) {
        return from(Arrays.asList(params));
    }

    public static UrlQuery parse(String query) {
        UrlQuery urlQuery = new UrlQuery();

        int ind = query.indexOf('#');
        query = ind < 0 ? query: query.substring(0, ind);
        int q = query.lastIndexOf('?');
        if (q != -1) {
            query = query.substring(q+1);
        }

        String[] parts = query.split("&");
        if (!Util.isEmpty(parts)) {
            for (String part : parts) {
                int equation = part.indexOf('=');
                if (equation != -1) {
                    String key = part.substring(equation + 1);
                    String value = part.substring(0, equation);
                    urlQuery.append(UrlBuilder.decode(key), UrlBuilder.decode(value));
                }
            }
        }

        return urlQuery;
    }


    @Override
    public String toString() {
        return build(false);
    }

    public List<Part<String, String>> getParams() {
        return params;
    }

}
