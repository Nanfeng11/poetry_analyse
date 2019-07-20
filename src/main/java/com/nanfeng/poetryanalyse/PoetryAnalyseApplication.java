package com.nanfeng.poetryanalyse;

import com.nanfeng.poetryanalyse.config.ObjectFactory;
import com.nanfeng.poetryanalyse.crawler.Crawler;

import com.nanfeng.poetryanalyse.web.WebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 唐诗分析程序的主类
 * Author：nanfeng
 * Created:2019/3/14
 */
public class PoetryAnalyseApplication {

    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(PoetryAnalyseApplication.class);

    public static void main(String[] args) {

        WebController webController = ObjectFactory.getInstance().getObject(WebController.class);
        //运行了web服务，提供接口
        LOGGER.info("Web Service launch ...");
        webController.launch();

        //启动爬虫(命令行)
        if (args.length==1 && args[0].equals("run-crawler")){
            Crawler crawler = ObjectFactory.getInstance().getObject(Crawler.class);
            LOGGER.info("Crawler started ...");
            crawler.start();
        }





//        ConfigProperties configProperties = new ConfigProperties();
//
//        final Page page = new Page(
//                configProperties.getCrawerBase(),
//                configProperties.getCrawerPath(),
//                configProperties.isCrawerDetail()
//        );
//
//        Crawler crawler = new Crawler();
//        crawler.addParse(new DocumentParse());
//        crawler.addParse(new DataPageParse());
//
//        //打印管道，打印page里的数据
//        crawler.addPipeline(new ConsolePipeline());
//
//        //准备数据源
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setUsername(configProperties.getDbUsername());
//        dataSource.setPassword(configProperties.getDbPassword());
//        dataSource.setDriverClassName(configProperties.getDbDriverClass());
//        dataSource.setUrl(configProperties.getDbUrl());
//
//        crawler.addPipeline(new DatabasePipeline(dataSource));
//
//        crawler.addPage(page);
//
//        crawler.start();
//
//        AnalyseDao analyseDao = new AnalyseDaoImpl(dataSource);
//
//        System.out.println("测试一");
//        analyseDao.analyseAuthorCount().forEach(authorCount -> {
//            System.out.println(authorCount);
//        });
//
//        System.out.println("测试二");
//        analyseDao.queryAllPoetryInfo().forEach(System.out::println);
//
//        AnalyseService analyseService = new AnalyseServiceImpl(analyseDao);
////        analyseService.analyseAuthorCount().forEach(System.out::println);
//        analyseService.analyseWordCloud().forEach(System.out::println);

//        Spark.get("/hello",(req, resp)->{
//            return "Hello world Crawler";
//        });
    }

}
