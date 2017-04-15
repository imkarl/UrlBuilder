package cn.imkarl.urlbuilder;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

/**
 * URL 生成器
 * @version imkarl 2017-04
 *
 * 格式形如：
 *     [<scheme>:]<scheme-specific-part>[#<fragment>]
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

    private static final String DEFAULT_SCHEME = "http";

    private String scheme;          // null ==> relative URI
    private String host;            // null ==> registry-based
    private int port = -1;          // -1 ==> undefined
    private UrlPath path;            // null ==> opaque
    private UrlQuery query;
    private String fragment;

    public UrlBuilder() {
    }
    private UrlBuilder(String scheme, String host, int port, UrlPath path, UrlQuery query, String fragment) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.path = path;
        this.query = query;
        this.fragment(fragment);
    }

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
    public UrlBuilder path(UrlPath path) {
        this.path = path;
        return this;
    }
    /**
     * 添加到path最后面
     */
    public UrlBuilder appendPath(String segment) {
        if (Util.isEmpty(segment)) {
            return this;
        }

        if (this.path == null) {
            this.path = new UrlPath();
        }
        this.path.append(segment);
        return this;
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
        this.query.append(key, value);
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
        this.query.put(key, value);
        return this;
    }

    public UrlBuilder fragment(String fragment) {
        if (Util.isEmpty(fragment)) {
            this.fragment = null;
        } else if (fragment.charAt(0) == '#') {
            this.fragment = fragment.substring(1);
        } else {
            this.fragment = fragment;
        }
        return this;
    }


    public String build() {
        checkArguments();

        StringBuilder result = new StringBuilder();
        result.append(Util.isEmpty(scheme) ?DEFAULT_SCHEME : scheme);
        result.append(':');
        result.append("//");
        result.append(encode(host));
        if (port > 0) {
            result.append(':').append(port);
        }

        String query = this.query==null ? null : this.query.build(true);
        if (path != null) {
            String strPath = path.build(true);
            result.append(Util.isEmpty(strPath) ? "/" : strPath);
        } else {
            result.append('/');
        }
        if (Util.isNotEmpty(query)) {
            result.append('?').append(query);
        }
        if (Util.isNotEmpty(fragment)) {
            result.append('#').append(encode(fragment));
        }

        return result.toString();
    }

    public URL toURL() {
        checkArguments();

        String query = (this.query == null || this.query.isEmpty()) ? null : this.query.build(false);

        StringBuilder fileBuilder = new StringBuilder();
        if (path != null) {
            fileBuilder.append(path.build(false));
        } else if (Util.isNotEmpty(query) || Util.isNotEmpty(fragment)) {
            fileBuilder.append('/');
        }
        if (Util.isNotEmpty(query)) {
            fileBuilder.append('?').append(query);
        }
        if (Util.isNotEmpty(fragment)) {
            fileBuilder.append('#').append(fragment);
        }

        try {
            return new URL(Util.isEmpty(scheme) ?DEFAULT_SCHEME : scheme, host, port, fileBuilder.toString());
        } catch (MalformedURLException e) {
            return null;
        }
    }
    public URI toURI() {
        checkArguments();

        String authority = null;
        if (Util.isNotEmpty(host)) {
            if (port > 0) {
                authority = host + ":" + port;
            } else {
                authority = host;
            }
        }

        String query = (this.query == null || this.query.isEmpty()) ? null : this.query.build(false);

        try {
            return new URI(Util.isEmpty(scheme) ?DEFAULT_SCHEME : scheme, authority, (path==null?null:path.build(false)), query, fragment);
        } catch (URISyntaxException e) {
            return null;
        }
    }
    public Uri toUri() {
        checkArguments();

        Uri.Builder builder = new Uri.Builder().scheme(Util.isEmpty(scheme) ?DEFAULT_SCHEME : scheme).path((path==null?null:path.build(false))).fragment(fragment);
        if (!Util.isEmpty(host)) {
            if (port > 0) {
                builder.authority(host + ":" + port);
            } else {
                builder.authority(host);
            }
        }
        if (query != null && !Util.isEmpty(query.getParams())) {
            for (Part<String, String> item : query.getParams()) {
                builder.appendQueryParameter(item.getKey(), item.getValue());
            }
        }
        return builder.build();
    }

    /**
     * 检查参数是否合法
     */
    private void checkArguments() {
        if (Util.isEmpty(host)) {
            throw new IllegalArgumentException("'host' must be non empty.");
        }
    }


    public static UrlBuilder from(String scheme, String host, int port, String path, String query, String fragment) {
        return new UrlBuilder(scheme, host, port, UrlPath.parse(path), UrlQuery.parse(query), fragment);
    }
    public static UrlBuilder from(String scheme, String host, int port, UrlPath path, UrlQuery query, String fragment) {
        return new UrlBuilder(scheme, host, port, path, query, fragment);
    }
    public static UrlBuilder from(Uri uri) {
        UrlQuery query = new UrlQuery();
        Set<String> queryKeys = uri.getQueryParameterNames();
        for (String queryKey : queryKeys) {
            List<String> queryValues = uri.getQueryParameters(queryKey);
            for (String queryValue : queryValues) {
                query.append(queryKey, queryValue);
            }
        }
        return from(uri.getScheme(), uri.getHost(), uri.getPort(), UrlPath.parse(uri.getPath()), query, uri.getFragment());
    }
    public static UrlBuilder from(URI uri) {
        UrlQuery query = UrlQuery.parse(uri.getRawQuery());
        return from(uri.getScheme(), uri.getHost(), uri.getPort(), UrlPath.parse(uri.getPath()), query, uri.getFragment());
    }
    public static UrlBuilder from(URL url) {
        return from(url.getProtocol(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
    }

    public static UrlBuilder parse(String url) {
        return from(Uri.parse(url));
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


    @Override
    public String toString() {
        return build();
    }

    public String getScheme() {
        return scheme;
    }
    public String getHost() {
        return host;
    }
    public int getPort() {
        return port;
    }
    public UrlPath getPath() {
        return path;
    }
    public UrlQuery getQuery() {
        return query;
    }
    public String getFragment() {
        return fragment;
    }

}
