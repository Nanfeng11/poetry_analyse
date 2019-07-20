package com.nanfeng.poetryanalyse.crawler.prase;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.nanfeng.poetryanalyse.crawler.common.Page;


/**
 * 链接解析（解析文档）
 * Author：nanfeng
 * Created:2019/3/17
 */
public class DocumentPrase implements Prase {
    @Override
    public void prase(Page page) {
        if (page.isDetail()) {
            return;
        }

//        //统计数量
//        final AtomicInteger count = new AtomicInteger(0);

        HtmlPage htmlPage = page.getHtmlPage();

        //先取所有的div标签，再取div里的所有a标签，链式调用
        /**
         * 取到一个网页的详情页面，也就是当前页面的子页面
         */
        htmlPage.getBody()
                .getElementsByAttribute(
                        "div",
                        "class",
                        "typecont"
                )
                .forEach(div -> {
                    DomNodeList<HtmlElement> aNodeList = div.getElementsByTagName("a");
                    aNodeList.forEach(
                            aNode -> {
                                //子页面path
                                String path = aNode.getAttribute("href");
                                Page subPage = new Page(
                                        page.getBase(),
                                        path,
                                        true
                                );
                                page.getSubPage().add(subPage);
                            }
                    );
                });
    }
}
