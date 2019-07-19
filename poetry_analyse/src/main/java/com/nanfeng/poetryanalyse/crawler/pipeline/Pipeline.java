package com.nanfeng.poetryanalyse.crawler.pipeline;

import com.nanfeng.poetryanalyse.crawler.common.Page;

/**
 * Author：nanfeng
 * Created:2019/3/17
 */
public interface Pipeline {

    /**
     * 管道处理page中的数据
     * @param page
     */

    void pipeline(final Page page);
}
