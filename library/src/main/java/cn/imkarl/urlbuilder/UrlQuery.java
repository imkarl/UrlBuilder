package cn.imkarl.urlbuilder;

import java.util.ArrayList;
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
    public UrlQuery(Map<String, String> params) {
        this.params = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (Util.isNotEmpty(entry.getKey())) {
                    this.params.add(new Part<>(entry.getKey(), entry.getValue()));
                }
            }
        }
    }
    public UrlQuery(Collection<Part<String, String>> params) {
        if (params != null && !params.isEmpty()) {
            this.params = new ArrayList<>(params);
        } else {
            this.params = new ArrayList<>();
        }
    }

    /**
     * 添加query，不论是否已存在相同KEY
     */
    public UrlQuery appendQuery(String key, String value) {
        if (Util.isEmpty(key)) {
            return this;
        }

        if (this.params == null) {
            this.params = new ArrayList<>();
        }
        this.params.add(new Part<>(key, value));
        return this;
    }

    /**
     * 如果已存在相同KEY，则替换之前所有的
     */
    public UrlQuery putQuery(String key, String value) {
        if (Util.isEmpty(key)) {
            return this;
        }

        if (this.params != null && !this.params.isEmpty()) {
            Iterator<Part<String, String>> it = this.params.iterator();
            while(it.hasNext()) {
                Part<String, String> part = it.next();
                if (part.getKey().equals(key)) {
                    it.remove();
                }
            }
        }
        appendQuery(key, value);
        return this;
    }

    public List<Part<String, String>> getParams() {
        return params;
    }


    /**
     * 构建Query string，前面不带'?'
     * @param encode 是否需要对key-value进行URL编码处理
     * @return 如果没有任何内容，则返回null
     */
    public String build(boolean encode) {
        String query = null;
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

    @Override
    public String toString() {
        return build(false);
    }

}
