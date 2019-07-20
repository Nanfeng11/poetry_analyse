package com.nanfeng.poetryanalyse.crawler.pipeline;

import com.nanfeng.poetryanalyse.analyse.entity.PoetryInfo;
import com.nanfeng.poetryanalyse.crawler.common.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Author：nanfeng
 * Created:2019/3/17
 */
public class DatabasePipeline implements Pipeline {

    private final Logger logger = LoggerFactory.getLogger(DatabasePipeline.class);

    //创建数据源
    private final DataSource dataSource;

    public DatabasePipeline(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void pipeline(final Page page) {

        //配置里面有更多的数据，清洗需要几个取几个即可，不需要在解析里面一次性处理完
        String title = (String) page.getDataSet().getData("title");
        String dynasty = (String) page.getDataSet().getData("dynasty");
        String author = (String) page.getDataSet().getData("author");
        String content = (String) page.getDataSet().getData("content");

        //数据库(修改对象）
        String sql = "insert into poetry_info (title,dynasty,author,content) values (?,?,?,?)";
        //获取连接   //预编译命令
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            //为占位符设置参数
           statement.setString(1,title);
           statement.setString(2,dynasty);
           statement.setString(3,author);
           statement.setString(4,content);

           //执行更新语句
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Database insert occur exception {}.",e.getMessage());
        }
    }
}
