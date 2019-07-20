package com.nanfeng.poetryanalyse.crawler.pipeline;

import com.nanfeng.poetryanalyse.crawler.common.Page;

import java.util.Map;

/**
 * 控制台管道，只打印输出
 * Author：nanfeng
 * Created:2019/3/17
 */
public class ConsolePipeline implements Pipeline{
    @Override
    public void pipeline(final Page page) {
        Map<String,Object> data = page.getDataSet().getData();
        //存储
        System.out.println(data);
    }
}
