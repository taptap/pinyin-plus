## pinyin-plus

汉字转拼音的库，有如下特点

- 拼音数据基于 [`cc-cedict`](https://cc-cedict.org/) 、[`kaifangcidian`](https://kaifangcidian.com/) 开源词库 
- 基于拼音词库的数据初始化分词引擎进行`分词`，准确度高，解决`多音字`的问题
- 支持繁体字
- 支持自定义词库，词库格式同 `cc-cedict` 字典格式
- api 简单，分为普通模式、索引模式

### 使用场景
汉字转拼音，常用于索引引擎场景创建拼音的索引，这个场景的问题一般由两种实现路径，一种是直接使用带拼音的的分词
插件，会自动帮你创建出拼音的索引，还有一种就是自己将汉字转换为拼音字符串，采用空格分隔分词来达到定制化索引的目的。
不论哪种实现路径，都离不开分词和拼音转换。`pinyin-plus` 的特点是，索引分词的词库和拼音的词库是基于同一套词库，
所以多音词的准确度特别高，而且词库的格式保留了开源词典的格式，词库可以轻松的定时更新。同时也预留了自定义词库的扩展
接口，保留定制化需求的高优先级

### 性能
- 测试服务参见：src/test/java/com/taptap/pinyin/PinYinWebApiTest.java
- 压测工具 wrk： https://github.com/wg/wrk 
```shell
#pinyin-plus 的压测数据，测试词语：率土之滨
kl@kldeMacBook-Pro-6 arthas % wrk -t16 -c100 -d15s --latency http://localhost:8080/%E7%8E%87%E5%9C%9F%E4%B9%8B%E6%BB%A8
Running 15s test @ http://localhost:8080/%E7%8E%87%E5%9C%9F%E4%B9%8B%E6%BB%A8
  16 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   733.97us  138.45us  16.40ms   96.12%
    Req/Sec     8.19k   293.50     8.90k    87.83%
  Latency Distribution
     50%  718.00us
     75%  739.00us
     90%  785.00us
     99%    1.02ms
  1970023 requests in 15.10s, 266.78MB read
Requests/sec: 130469.56
Transfer/sec:     17.67MB
```

### 添加依赖
gradle
```groovy
compile "com.github.taptap:pinyin-plus:1.0"
```
maven
```xml
        <dependency>
            <groupId>com.github.taptap</groupId>
            <artifactId>pinyin-plus</artifactId>
            <version>1.0</version>
        </dependency>
```
### 使用
```java
    //普通模式示例，汉字转换拼音后，单子采用空格隔开输出
    @Test
    void testToPinYin() {
        String pinyin = PinyinPlus.to("率土之滨");
        System.err.println(pinyin);
        Assertions.assertEquals("shuai tu zhi bin", pinyin);
    }
    //索引模式示例，汉字转换拼音后，词组采用空格隔开输出
    @Test
    void testToPinYin2() {
            String pinyin = PinyinPlus.toIndex("写的射雕英雄传");
            System.err.println(pinyin);
            Assertions.assertEquals("xie de shediaoyingxiongzhuan", pinyin);
    }
    
```

### 自定义词库
在项目 `resources` 目录下，新增 `custom_cedict_ts.u8` 文本文件，输入如下格式数据，`#` 开头的为注释，如：
```
#自定义词库
血花 血花 [xue4 hua1] //
```
格式保留和开源词库 [cc-cedict](https://cc-cedict.org/) 一样的风格，遇到相同的词组，自定义的优先级最高，会覆盖系统默认的词组
### 鸣谢
- 分词引擎采用了 [segment](https://github.com/houbb/segment) , 感谢作者开源
-  [cc-cedict](https://cc-cedict.org/) 、[kaifangcidian](https://kaifangcidian.com/) 开源词库 


