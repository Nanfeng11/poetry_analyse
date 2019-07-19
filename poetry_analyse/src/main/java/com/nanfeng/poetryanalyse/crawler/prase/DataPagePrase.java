package com.nanfeng.poetryanalyse.crawler.prase;

import com.gargoylesoftware.htmlunit.html.*;
import com.nanfeng.poetryanalyse.crawler.common.Page;

/**
 * 详情页面解析（解析数据）
 * Author：nanfeng
 * Created:2019/3/17
 */
public class DataPagePrase implements Prase {
    @Override
    public void prase(Page page) {
        if(!page.isDetail()){
            return;
        }

//        //获取到网页
//        HtmlDivision area = (HtmlDivision) page.getHtmlPage().getElementById("contson45c396367f59");
//        String content = area.asText();
//        page.getDataSet().putData("正文",content);

        ///html/body/div[3]/div[1]/div[2]/div[1]/h1
        //body > div.main3 > div.left > div:nth-child(2) > div.cont > h1（拷贝选择器）

        //查文档HtmlUnit，格式:div[@class='cont']

        HtmlPage htmlPage = page.getHtmlPage();

        HtmlElement body = htmlPage.getBody();

        //标题
        String titlePath = "//div[@class='cont']/h1/text()";
        DomText titleDom = (DomText) body.getByXPath(titlePath).get(0); //返回list，只有一个，取第0个
        String title = titleDom.asText();

        //朝代
        //String dynastyPath = "//div[@class='cont']/p/a[1]";
        String dynastyPath = "//div[@class='cont']/p[@class='source']/a[1]";
        HtmlAnchor dynastyDom = (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
        String dynasty = dynastyDom.asText();

        //作者
        // String authorPath = "//div[@class='cont']/p/a[2]";
        String authorPath = "//div[@class='cont']/p[@class='source']/a[2]";
        HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
        String author = authorDom.asText();

        //正文
        String contentPath = "//div[@class='cont']//div[@class='contson']";
        HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
        String content = contentDom.asText();

        //解析数据，方便后面加入更多的数据，清洗的时候要不要自己决定
        page.getDataSet().putData("title",title);
        page.getDataSet().putData("dynasty",dynasty);
        page.getDataSet().putData("author",author);
        page.getDataSet().putData("content",content);
        //更多的数据

        //page.getDataSet().putData("url",page.getUrl());

    }
}
