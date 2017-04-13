# UrlBuilder
链式API的URL生成器，简单、直观、易用、安全。

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


# Usage

- Step 1. Add the JitPack repository to your build file

把 JitPack repository 添加到`build.gradle`文件中`repositories`的末尾:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- Step 2. Add the dependency

在你的app `build.gradle`的`dependencies`中添加依赖

```
compile 'com.github.imkarl:urlbuilder:[latestVersion]'
```


# Sample

- 最简单的例子

```
String url = new UrlBuilder().scheme("https").host("www.baidu.com").path("/").build();
```

输出：
```
https://www.baidu.com/
```


- 完整形态

```
String url = new UrlBuilder().scheme("https").host("www.baidu.com").port(80).path("/s")
                    .putQuery("ie", "UTF-8")
                    .putQuery("wd", "测试")
                    .fragment("abc")
                    .build();
```

输出：
```
https://www.baidu.com:80/s?ie=UTF-8&wd=%E6%B5%8B%E8%AF%95#abc
```


- 更复杂的场景

```
String url = new UrlBuilder().scheme("https").host("www.baidu.com").path("/s")
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
```

输出：
```
https://www.baidu.com/s?ie=UTF-8&wd=%E6%B5%8B%E8%AF%95&tfflag=1&abc=one&abc=two#bbb
```

- 更多示例

请查看 [UrlBuilderUnitTest.java](https://github.com/ImKarl/UrlBuilder/blob/master/library/src/test/java/cn/imkarl/urlbuilder/UrlBuilderUnitTest.java)

# Thanks

- [Uniform Resource Identifier - wikipedia.org](https://en.wikipedia.org/wiki/Uniform_Resource_Identifier)
- [URL - wikipedia.org](https://en.wikipedia.org/wiki/URL)
- [Query string - wikipedia.org](https://en.wikipedia.org/wiki/Query_string)
- [duplicate query keys - stackoverflow.com](https://stackoverflow.com/questions/1746507/authoritative-position-of-duplicate-http-get-query-keys)
