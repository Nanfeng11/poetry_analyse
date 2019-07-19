package com.nanfeng.poetryanalyse.analyse.dao.impl;

import com.nanfeng.poetryanalyse.analyse.dao.AnalyseDao;
import com.nanfeng.poetryanalyse.analyse.entity.PoetryInfo;
import com.nanfeng.poetryanalyse.analyse.model.AuthorCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现Dao，JDBC编程
 * Author：nanfeng
 * Created:2019/7/7
 */
public class AnalyseDaoImpl implements AnalyseDao {

    private static final Logger logger = LoggerFactory.getLogger(AnalyseDaoImpl.class);

    private final DataSource dataSource;

    public AnalyseDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<AuthorCount> analyseAuthorCount() {
        //放查询结果
        List<AuthorCount> datas = new ArrayList<>();
        //try()自动关闭
        String sql = "select count(*) as count,author from poetry_info group by author";
        try (Connection connection = dataSource.getConnection();
             //查询之后创建命令，预编译命令
             PreparedStatement statement = connection.prepareStatement(sql);
             //执行查询
             ResultSet rs = statement.executeQuery()
        ) {
            //获取结果，用while循环
            while (rs.next()) {
                AuthorCount authorCount = new AuthorCount();
                authorCount.setAuthor(rs.getString("author"));
                authorCount.setCount(rs.getInt("count"));
                datas.add(authorCount);
            }
        } catch (SQLException e) {
            logger.error("Database query occur exception {}.",e.getMessage());
        }

        return datas;
    }

    @Override
    public List<PoetryInfo> queryAllPoetryInfo() {
        List<PoetryInfo> datas = new ArrayList<>();
        String sql = "select title,dynasty,author,content from poetry_info";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery()
        ){
            while (rs.next()){
                PoetryInfo poetryInfo = new PoetryInfo();
                poetryInfo.setTitle(rs.getString("title"));
                poetryInfo.setDynasty(rs.getString("dynasty"));
                poetryInfo.setAuthor(rs.getString("author"));
                poetryInfo.setContent(rs.getString("content"));
                datas.add(poetryInfo);
            }
        }catch (SQLException e){
            logger.error("Database query occur exception {}.",e.getMessage());
        }
        return datas;
    }
}
