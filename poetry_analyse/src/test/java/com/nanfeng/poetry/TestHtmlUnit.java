package com.nanfeng.poetry;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;


import java.io.IOException;


/**
 * Author：nanfeng
 * Created:2019/3/17
 */
public class TestHtmlUnit {

    public static void main(String[] args) {

        //自动关闭流
        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {

            //过滤JavaScript文件
            webClient.getOptions().setJavaScriptEnabled(false);

            HtmlPage htmlPage = webClient.getPage("https://so.gushiwen.org/shiwenv_45c396367f59.aspx");
//            //getBody    获取正文
////            HtmlElement bodyElement = htmlPage.getBody();
////            String text = bodyElement.asText();
////            System.out.println(text);
//
            //采集
//            HtmlDivision domElement = (HtmlDivision) htmlPage.getElementById("contsona51c59087fc8");
//
            //解析
//            String divContent = domElement.asText();
//            System.out.println(divContent);


            HtmlElement body = htmlPage.getBody();

            //标题
            String titlePath = "//div[@class='cont']/h1/text()";
            DomText titleDom = (DomText) body.getByXPath(titlePath).get(0);
            String title = titleDom.asText();

            //朝代
            String dynastyPath = "//div[@class='cont']/p/a[1]";
            HtmlAnchor dynastyDom = (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
            String dynasty = dynastyDom.asText();

            //作者
            String authorPath = "//div[@class='cont']/p/a[2]";
            HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
            String author = authorDom.asText();

            //正文
            String contentPath = "//div[@class='cont']/div[@class='contson']";
            HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
            String content = contentDom.asText();

//

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
