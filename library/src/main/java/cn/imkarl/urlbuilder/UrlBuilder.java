package cn.imkarl.urlbuilder;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL 生成器
 * @version imkarl 2017-04
 *
 * 格式形如：
 *     [scheme:][//authority][path][?query][#fragment]
 *     [scheme:][//host:port][path][?query][#fragment]
 *
 * 参考图：
 * <pre>
 *                     hierarchical part
 *               ┌─────────────────────────────────┴───────────────────────────────────┐
 *                     authority                      path
 *               ┌───────────────────────────┴───────────────────────┐┌───────┴────────┐
 *        abc://username:password@example.com:123/path/data?key=value&key2=value2#fragid1
 *        └┬┘   └─────────────┬─────────────┘   └────┬────┘ └─────────┬─────────┘ └───────┬──────┘ └──┬──┘
 *    scheme  user-information     host     port                  query             fragment
 *
 *        urn:example:mammal:monotreme:echidna
 *        └┬┘ └────────────────────┬─────────────────────┘
 *      scheme                path
 * </pre>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Uniform_Resource_Identifier">Uniform Resource Identifier</a>
 */
public final class UrlBuilder {

    private String scheme;          // null ==> relative URI
    private String host;            // null ==> registry-based
    private int port = -1;          // -1 ==> undefined
    private String path;            // null ==> opaque
    private UrlQuery query;
    private String fragment;

    public UrlBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }
    public UrlBuilder host(String host) {
        this.host = host;
        return this;
    }
    public UrlBuilder port(int port) {
        this.port = port;
        return this;
    }

    /**
     * 设置path，将覆盖之前所有的path相关设置
     */
    public UrlBuilder path(String path) {
        if (Util.isEmpty(path)) {
            this.path = null;
        } else {
            this.path = "/" + formatPath(path);
        }
        return this;
    }
    /**
     * 添加到path最后面
     */
    public UrlBuilder appendPath(String queryPart) {
        String oldPath = this.path;
        String newPath;
        if (Util.isEmpty(oldPath)) {
            newPath = "/" + formatPath(queryPart);
        } else if (oldPath.charAt(oldPath.length() - 1) == '/') {
            newPath = oldPath + formatPath(queryPart);
        } else {
            newPath = oldPath + "/" + formatPath(queryPart);
        }
        this.path = newPath;
        return this;
    }
    private static String formatPath(String path) {
        if (Util.isEmpty(path)) {
            return "";
        } else if (path.charAt(0) == '/') {
            return path.substring(1);
        } else {
            return path;
        }
    }

    /**
     * 设置query，将覆盖之前所有的query相关设置
     */
    public UrlBuilder query(UrlQuery query) {
        this.query = query;
        return this;
    }
    /**
     * 添加query，不论是否已存在相同KEY
     */
    public UrlBuilder appendQuery(String key, String value) {
        if (Util.isEmpty(key)) {
            return this;
        }

        if (this.query == null) {
            this.query = new UrlQuery();
        }
        this.query.appendQuery(key, value);
        return this;
    }
    /**
     * 如果已存在相同KEY，则替换之前所有的
     */
    public UrlBuilder putQuery(String key, String value) {
        if (Util.isEmpty(key)) {
            return this;
        }

        if (this.query == null) {
            this.query = new UrlQuery();
        }
        this.query.putQuery(key, value);
        return this;
    }

    public UrlBuilder fragment(String fragment) {
        this.fragment = fragment;
        return this;
    }


    public String build() {
        URL url = buildURL();
        return url==null ? null : url.toString();
    }
    public URL buildURL() {
        String query = this.query==null ? null : this.query.build(true);

        StringBuilder fileBuilder = new StringBuilder();
        if (Util.isNotEmpty(path)) {
            fileBuilder.append(path);
        } else if (Util.isNotEmpty(query) || Util.isNotEmpty(fragment)) {
            fileBuilder.append('/');
        }
        if (Util.isNotEmpty(query)) {
            fileBuilder.append('?').append(query);
        }
        if (Util.isNotEmpty(fragment)) {
            if (fragment.charAt(0) == '#') {
                fileBuilder.append('#').append(encode(fragment.substring(1)));
            } else {
                fileBuilder.append('#').append(encode(fragment));
            }
        }

        try {
            return new URL(scheme, host, port, fileBuilder.toString());
        } catch (MalformedURLException e) {
            return null;
        }
    }
    public URI buildURI() {
        String authority = null;
        if (Util.isNotEmpty(host)) {
            if (port > 0) {
                authority = host + ":" + port;
            } else {
                authority = host;
            }
        }

        String query = this.query==null ? null : this.query.build(false);

        try {
            return new URI(scheme, authority, path, query, fragment);
        } catch (URISyntaxException e) {
            return null;
        }
    }
    public Uri buildUri() {
        Uri.Builder builder = new Uri.Builder().scheme(scheme).path(path).fragment(fragment);
        if (!Util.isEmpty(host)) {
            if (port > 0) {
                builder.authority(host + ":" + port);
            } else {
                builder.authority(host);
            }
        }
        if (query != null) {
            for (Part<String, String> item : query.getParams()) {
                builder.appendQueryParameter(item.getKey(), item.getValue());
            }
        }
        return builder.build();
    }

    @Override
    public String toString() {
        return build();
    }


    public static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            return str;
        }
    }
    public static String decode(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (Exception e) {
            return str;
        }
    }

}
