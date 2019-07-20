package com.nanfeng.poetryanalyse.web;

import com.google.gson.Gson;
import com.nanfeng.poetryanalyse.analyse.model.AuthorCount;
import com.nanfeng.poetryanalyse.analyse.model.WordCount;
import com.nanfeng.poetryanalyse.analyse.service.AnalyseService;
import com.nanfeng.poetryanalyse.config.ObjectFactory;
import com.nanfeng.poetryanalyse.crawler.Crawler;
import spark.*;

import java.util.List;

/**
 * 提供Web接口---后端服务，通过浏览器等可以访问，可以看到数据
 * Web API
 * 1、Sparkjava 框架完成Web API开发
 * 2、Servlet技术实现Web API开发
 * 3、Java-Httpd实现Web API（纯Java语言实现的Web服务器）
 *      （对Socket Http协议非常清楚）
 * Author：nanfeng
 * Created:2019/7/7
 */
public class WebController {
    //Controller层需要访问的是业务层

    private final AnalyseService analyseService;

    public WebController(AnalyseService analyseService) {
        this.analyseService = analyseService;
    }

    //->    http://127.0.0.1:4567/
    //->    /analyse/author_count
    private List<AuthorCount> analyseAuthorCount(){
        return analyseService.analyseAuthorCount();
    }

    //->    http://127.0.0.1:4567
    //->    /analyse/word_cloud
    private List<WordCount> analyseWordCloud(){
        return analyseService.analyseWordCloud();
    }

    //运行web
    public void launch(){
        //转换器
        ResponseTransformer transformer = new JSONResponseTransformer();

        //src/main/resources/static
        //前端静态文件的目录
        Spark.staticFileLocation("/static");

        //服务端接口
        //第一个接口
        Spark.get("/analyse/author_count",
                (request, response) -> analyseAuthorCount(),
                transformer);
        //第二个接口
        Spark.get("/analyse/word_cloud",
               (request, response) -> analyseWordCloud(),
                transformer
                );

        Spark.get("/crawler/stop",(request, response) -> {
            Crawler crawler = ObjectFactory.getInstance().getObject(Crawler.class);
            crawler.stop();
            return "爬虫停止";
        });
    }

    public static class JSONResponseTransformer implements ResponseTransformer{

        //Object -> String
        private Gson gson = new Gson();

        @Override
        public String render(Object o) throws Exception {
            return gson.toJson(o);
        }
    }

    //Gson对象转成字符串，或者字符串转成对象
//    public static void main(String[] args) {
//        Gson gson = new Gson();
//
//        WordCount wordCount = new WordCount();
//        wordCount.setWord("Java");
//        wordCount.setCount(10);
//
//        System.out.println(gson.toJson(wordCount));
//    }
}
