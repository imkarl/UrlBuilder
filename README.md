# UrlBuilder
链式风格的URL生成器，简单、直观、易用、安全。

[![](https://jitpack.io/v/imkarl/urlbuilder.svg)](https://jitpack.io/#imkarl/urlbuilder)


# Introduce

下图显示了两个示例URI及其组成部分：
```
                   hierarchical part
             ┌─────────────────────────────────┴───────────────────────────────────┐
                   authority                      path
             ┌───────────────────────────┴───────────────────────┐┌───────┴────────┐
      abc://username:password@example.com:123/path/data?key=value&key2=value2#fragid1
      └┬┘   └─────────────┬─────────────┘   └────┬────┘ └─────────┬─────────┘ └───────┬──────┘ └──┬──┘
  scheme  user-information     host      port                  query             fragment

      urn:example:mammal:monotreme:echidna
      └┬┘ └────────────────────────┬─────────────────────────┘
    scheme                path
```


# Features

- 可以方便地转化为`Uri`、`URI`、`URL`、`String`
- [`UrlQuery`](https://github.com/ImKarl/UrlBuilder/blob/master/library/src/main/java/cn/imkarl/urlbuilder/UrlQuery.java)
支持更多健全的操作API（`appendQuery` \ `putQuery`）
- 自动完成URL转码（`URLEncoder.encode(str, "UTF-8")`）
- 自动补全`URL Scheme`（缺省值："http"）


# Usage

**Step 1.** Add the JitPack repository to your build file
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency
```
compile 'com.github.ImKarl:UrlBuilder:[latestVersion]'
```


# Sample

- 生成URL

```
UrlQuery query = new UrlQuery()
                    .appendQuery("ie", "UTF-8")
                    .putQuery("wd", "test")
                    .putQuery("wd", "测试")
                    .appendQuery("tfflag", "0")
                    .putQuery("tfflag", "1")
                    .appendQuery("abc", "one")
                    .appendQuery("abc", "two");

// host 为必要参数
String url = new UrlBuilder().scheme("https").host("www.baidu.com").path("/s")
                    .query(query)
                    .fragment("aaa")
                    .fragment("bbb")
                    .build();

输出：`url`
> "https://www.baidu.com/s?ie=UTF-8&wd=%E6%B5%8B%E8%AF%95&tfflag=1&abc=one&abc=two#bbb"
```


- 解析URL

```
String url = "https://%E6%B5%8B%E8%AF%95.baidu.com/s/%E6%B5%8B%E8%AF%95/?ie=UTF-8&wd=%E6%B5%8B%E8%AF%95&tfflag=1&abc=one&abc=two#%E6%B5%8B%E8%AF%95";
UrlBuilder urlBuilder = UrlBuilder.parse(url);
boolean compare = url.equals(urlBuilder.build());
String host = urlBuilder.getHost();

String queryString = "?ie=UTF-8&wd=%E6%B5%8B%E8%AF%95&tfflag=1&abc=one&abc=two#%E6%B5%8B%E8%AF%95";
String query = UrlQuery.parse(queryString).build(false);

输出：`compare`
> true

输出：`host`
> "测试.baidu.com"

输出：`strQuery`
> "ie=UTF-8&wd=测试&tfflag=1&abc=one&abc=two"
```


- `Uri`、`URI`、`URL`之间相互转换

```
Uri uri = Uri.parse("http://www.baidu.com/");
UrlBuilder urlBuilder = UrlBuilder.from(uri);
Uri newUri = urlBuilder.toUri();

boolean compare = uri.equals(newUri);
String url = urlBuilder.build();

输出：`compare`
> true

输出：`url`
> "http://www.baidu.com/"
```

```
URI uri = URI.create("http://www.baidu.com/");
UrlBuilder urlBuilder = UrlBuilder.from(uri);
URI newUri = urlBuilder.toURI();

boolean compare = uri.equals(newUri);
String url = urlBuilder.build();

输出：`compare`
> true

输出：`url`
> "http://www.baidu.com/"
```

```
URL uri = new URL("http://www.baidu.com/");
UrlBuilder urlBuilder = UrlBuilder.from(uri);
URL newUri = urlBuilder.toURL();

boolean compare = uri.equals(newUri);
String url = urlBuilder.build();

输出：`compare`
> true

输出：`url`
> "http://www.baidu.com/"
```


- 更多示例

请查看 [UrlBuilderUnitTest.java](https://github.com/ImKarl/UrlBuilder/blob/master/library/src/test/java/cn/imkarl/urlbuilder/UrlBuilderUnitTest.java)


# Thanks

- [Uniform Resource Identifier - wikipedia.org](https://en.wikipedia.org/wiki/Uniform_Resource_Identifier)
- [URL - wikipedia.org](https://en.wikipedia.org/wiki/URL)
- [Query string - wikipedia.org](https://en.wikipedia.org/wiki/Query_string)
- [duplicate query keys - stackoverflow.com](https://stackoverflow.com/questions/1746507/authoritative-position-of-duplicate-http-get-query-keys)
