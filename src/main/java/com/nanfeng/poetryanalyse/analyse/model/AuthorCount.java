package com.nanfeng.poetryanalyse.analyse.model;

import lombok.Data;

/**
 * 作者和他所创作的数目
 * Author：nanfeng
 * Created:2019/7/7
 */
@Data
public class AuthorCount {
    private String author;
    private Integer count;
}
