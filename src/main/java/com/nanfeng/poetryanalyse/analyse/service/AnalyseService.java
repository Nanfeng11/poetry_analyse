package com.nanfeng.poetryanalyse.analyse.service;

import com.nanfeng.poetryanalyse.analyse.model.AuthorCount;
import com.nanfeng.poetryanalyse.analyse.model.WordCount;

import java.util.List;

/**
 * Author：nanfeng
 * Created:2019/7/7
 */
public interface AnalyseService {
    /**
     * 分析唐诗中作者的创作数量
     * @return
     */
    List<AuthorCount> analyseAuthorCount();

    /**
     * 词云分析
     * @return
     */
    List<WordCount> analyseWordCloud();
}
