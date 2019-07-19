package com.nanfeng.poetryanalyse.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.nanfeng.poetryanalyse.analyse.dao.AnalyseDao;
import com.nanfeng.poetryanalyse.analyse.dao.impl.AnalyseDaoImpl;
import com.nanfeng.poetryanalyse.analyse.service.AnalyseService;
import com.nanfeng.poetryanalyse.analyse.service.impl.AnalyseServiceImpl;
import com.nanfeng.poetryanalyse.crawler.Crawler;
import com.nanfeng.poetryanalyse.crawler.common.Page;
import com.nanfeng.poetryanalyse.crawler.prase.DataPagePrase;
import com.nanfeng.poetryanalyse.crawler.prase.DocumentPrase;
import com.nanfeng.poetryanalyse.crawler.pipeline.ConsolePipeline;
import com.nanfeng.poetryanalyse.crawler.pipeline.DatabasePipeline;
import com.nanfeng.poetryanalyse.web.WebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Author：nanfeng
 * Created:2019/7/7
 */
public final class ObjectFactory {

    //工厂对象---单例（饿汉式）
    private static final ObjectFactory instance = new ObjectFactory();

    private final Logger logger = LoggerFactory.getLogger(ObjectFactory.class);

    /**
     * 存放所有对象
     */
    //每个类都可以用class类表示
    private final Map<Class, Object> objectHashMap = new HashMap<>();

    private ObjectFactory() {

        //1、初始化配置对象
        initConfigProperties();

        //2、数据源对象
        initDataSource();

        //3、爬虫对象
        initCrawler();

        //4、web对象
        initWebController();

        //5、对象清单打印输出
        printObjectList();
    }

    private void initWebController() {
        //需要Service，而Service只需要Dao就可以了
        DataSource dataSource = getObject(DataSource.class);
        AnalyseDao analyseDao = new AnalyseDaoImpl(dataSource);
        AnalyseService analyseService = new AnalyseServiceImpl(analyseDao);
        WebController webController = new WebController(analyseService);
        objectHashMap.put(WebController.class,webController);
    }

    private void initCrawler() {
        ConfigProperties configProperties = getObject(ConfigProperties.class);
        DataSource dataSource = getObject(DataSource.class);
        final Page page = new Page(
                configProperties.getCrawlerBase(),
                configProperties.getCrawlerPath(),
                configProperties.isCrawlerDetail()
        );

        Crawler crawler = new Crawler();
        crawler.addPage(page);
        crawler.addPrase(new DocumentPrase());
        crawler.addPrase(new DataPagePrase());
        if (configProperties.isEnableConsole()){
            crawler.addPipeline(new ConsolePipeline());
        }
        crawler.addPipeline(new DatabasePipeline(dataSource));

        objectHashMap.put(Crawler.class, crawler);
    }

    private void initDataSource() {
        ConfigProperties configProperties = getObject(ConfigProperties.class);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUsername());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());
        objectHashMap.put(DataSource.class, dataSource);
    }

    private void initConfigProperties() {
        ConfigProperties configProperties = new ConfigProperties();
        objectHashMap.put(ConfigProperties.class, configProperties);

        logger.info("ConfigProperties info:\n{}",configProperties.toString());
    }

    public <T> T getObject(Class classz) {
        if (!objectHashMap.containsKey(classz)) {
            throw new IllegalArgumentException("Class" + classz.getName() + "not found Object");
        }
        return (T) objectHashMap.get(classz);
    }

    public static ObjectFactory getInstance() {
        return instance;
    }

    private void printObjectList() {
       logger.info("=========ObjectFactory List=========");
        for (Map.Entry<Class, Object> entry : objectHashMap.entrySet()) {
            logger.info(String.format("[%s]==>[%s]",
                    entry.getKey().getCanonicalName(),
                    entry.getValue().getClass().getCanonicalName()
            ));
        }
        logger.info("=====================");
    }
}
