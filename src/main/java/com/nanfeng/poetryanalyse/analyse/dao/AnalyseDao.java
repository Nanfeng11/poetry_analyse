package com.nanfeng.poetryanalyse.analyse.dao;

import com.nanfeng.poetryanalyse.analyse.entity.PoetryInfo;
import com.nanfeng.poetryanalyse.analyse.model.AuthorCount;

import java.util.List;

/**
 * 把所有数据查出来
 * Author：nanfeng
 * Created:2019/7/7
 */
public interface AnalyseDao {

    /**
     * 分析唐诗中作者的创作数量
     * @return
     */
    List<AuthorCount> analyseAuthorCount();

    /**
     * 查询所有诗文，提供给业务层进行分析（词云）
     * @return
     */
    List<PoetryInfo> queryAllPoetryInfo();
}
