package com.nanfeng.poetryanalyse.analyse.entity;

import lombok.Data;

/**
 * Author：nanfeng
 * Created:2019/7/7
 */
@Data
public class PoetryInfo {
    /**
     * 标题
     */
    private String title;
    /**
     * 朝代
     */
    private String dynasty;
    /**
     * 作者
     */
    private String author;
    /**
     * 正文
     */
    private String content;
}
