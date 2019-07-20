package com.nanfeng.poetryanalyse.config;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置属性
 * Author：nanfeng
 * Created:2019/3/19
 */
@Data
public class ConfigProperties {

    private String crawlerBase;
    private String crawlerPath;
    private boolean crawlerDetail;

    private String dbUsername;
    private String dbPassword;
    private String dbUrl;
    private String dbDriverClass;

    //默认不开启控制台
    private boolean enableConsole;

    public ConfigProperties() {
        //从外部文件加载

        InputStream inputStream = ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties");
        Properties p = new Properties();
        try {
            p.load(inputStream);
            System.out.println(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.crawlerBase = String.valueOf(p.get("crawler.base"));
        this.crawlerPath = String.valueOf(p.get("crawler.path"));
        this.crawlerDetail = Boolean.parseBoolean(String.valueOf(p.get("crawler.detail")));

        this.dbUsername = String.valueOf(p.get("db.username"));
        this.dbPassword = String.valueOf(p.get("db.password"));
        this.dbDriverClass = String.valueOf(p.get("db.driver_class"));
        this.dbUrl = String.valueOf(p.get("db.url"));

        this.enableConsole = Boolean.valueOf(String.valueOf
                (p.getProperty("config.enable_console","false")));

    }

    public static void main(String[] args) {
        new ConfigProperties();
    }

}
