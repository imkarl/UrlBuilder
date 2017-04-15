package cn.imkarl.urlbuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * URL 路径段
 * @version imkarl 2017-04
 *
 * @see <a href="https://en.wikipedia.org/wiki/URL#Internationalized_URL">Internationalized URL</a>
 */
public class UrlPath {

    private List<String> segments;
    private boolean endTag;

    public UrlPath() {
    }

    /**
     * 添加到path最后面
     */
    public UrlPath append(String segment) {
        add(segment, false);
        return this;
    }

    /**
     * 添加到path最前面
     */
    public UrlPath before(String segment) {
        add(segment, true);
        return this;
    }

    private void add(String segment, boolean before) {
        if (Util.isEmpty(segment) || "/".equals(segment)) {
            return;
        }

        segment = segment.trim();
        if (segment.charAt(0) == '/') {
            segment = segment.substring(1);
        }
        if (segment.charAt(segment.length()-1) == '/') {
            segment = segment.substring(0, segment.length()-1);
        }

        if (this.segments == null) {
            this.segments = new ArrayList<>();
        }
        if (before) {
            this.segments.add(0, segment.trim());
        } else {
            this.segments.add(segment.trim());
        }
    }

    public UrlPath endTag(boolean endTag) {
        this.endTag = endTag;
        return this;
    }


    /**
     * 构建path，前面带'/'（对segment进行URL编码处理）
     * @return 如果没有任何内容，则返回null
     */
    public String build() {
        return build(true);
    }
    /**
     * 构建path，前面带'/'
     * @param encode 是否需要对segment进行URL编码处理
     * @return 如果没有任何内容，则返回空字符串""
     */
    public String build(boolean encode) {
        String path = "";
        if (!Util.isEmpty(segments)) {
            StringBuilder pathBuiler = new StringBuilder();
            for (String segment : segments) {
                if (encode) {
                    pathBuiler.append('/').append(UrlBuilder.encode(segment));
                } else {
                    pathBuiler.append('/').append(segment);
                }
            }
            path = pathBuiler.toString();
        }
        if (endTag) {
            return path + "/";
        }
        return path;
    }


    public static UrlPath from(Collection<String> segments) {
        UrlPath urlPath = new UrlPath();
        urlPath.segments = new ArrayList<>(segments);
        return urlPath;
    }

    public static UrlPath parse(String path) {
        UrlPath urlPath = new UrlPath();

        if (Util.isNotEmpty(path)) {
            path = path.trim();

            urlPath.endTag(path.endsWith("/"));

            String[] segments = path.split("/");
            if (!Util.isEmpty(segments)) {
                for (String segment : segments) {
                    if (Util.isEmpty(segment)) {
                        continue;
                    }

                    urlPath.append(UrlBuilder.decode(segment));
                }
            }
        }

        return urlPath;
    }


    @Override
    public String toString() {
        return build(false);
    }

    public List<String> getSegments() {
        return segments;
    }
    public boolean isEndTag() {
        return endTag;
    }

}
