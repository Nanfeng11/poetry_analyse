package com.nanfeng.poetryanalyse.crawler.prase;

import com.nanfeng.poetryanalyse.crawler.common.Page;

/**
 * Author：nanfeng
 * Created:2019/3/17
 */
public interface Prase {
    /**
     * 解析页面
     * @param page
     */
    void prase(final Page page);
}
