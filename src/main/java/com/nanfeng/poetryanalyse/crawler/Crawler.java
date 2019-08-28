package com.nanfeng.poetryanalyse.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import com.nanfeng.poetryanalyse.crawler.common.Page;

import com.nanfeng.poetryanalyse.crawler.prase.Prase;

import com.nanfeng.poetryanalyse.crawler.pipeline.Pipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 爬虫调度框架
 * Author：nanfeng
 * Created:2019/3/17
 */
public class Crawler {

    //打印日志信息
    private static final Logger logger = LoggerFactory.getLogger(Crawler.class);

    /**
     * 放置文档页面（超链接）
     * 放置详情页面（数据）
     *
     *未被采集和解析的页面
     * Page  htmlPage  dataSet
     */
    private final Queue<Page> docQueue = new LinkedBlockingQueue<>();

    /**
     * 放置详情页面(处理完成，数据在dataSet)
     */
    private final Queue<Page> detailQueue = new LinkedBlockingQueue<>();

    /**
     * 采集器
     */
    private final WebClient webClient;

    /**
     * 所有的解析器
     */
    private final List<Prase> praseList = new LinkedList<>();

    /**
     * 所有的清洗器
     */
    private final List<Pipeline> pipelineList = new LinkedList<>();

    /**
     * 线程调度器
     */
    private final ExecutorService executorService;

    public Crawler() {
        this.webClient = new WebClient(BrowserVersion.CHROME);
        this.webClient.getOptions().setJavaScriptEnabled(false);
        this.executorService = Executors.newFixedThreadPool(8, new ThreadFactory() {

            private final AtomicInteger id = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Crawler-Thread-" + id.getAndIncrement());
                return thread;
            }
        });
    }

    /**
     * 启动爬虫
     */
    public void start() {
        //爬取
        //解析
        //清洗

        //方法引用
        this.executorService.submit(this::prase);

//        this.executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//                pipeline();
//            }
//        });

        this.executorService.submit(this::pipeline);

    }

    //解析
    private void prase() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Prase occur exception {} .",e.getMessage());
            }

            //base path detail --->  采集后有htmlpage
            //出队列，可以获取到一个page

            final Page page = this.docQueue.poll();
            if (page == null) {
                continue;
            }
            //多线程
            this.executorService.submit(() -> {
                try {
                    //解析，采集，抓取到url对应的文档模型
                    //匿名内部类，通过类名访问
                    HtmlPage htmlPage = Crawler.this.webClient.getPage(page.getUrl());
                    page.setHtmlPage(htmlPage);

                    for (Prase prase : Crawler.this.praseList) {
                        prase.prase(page);
                    }

                    if (page.isDetail()) {
                        Crawler.this.detailQueue.add(page);
                    } else {
                        //迭代器  Page：泛型的指针类型
                        Iterator<Page> iterator = page.getSubPage().iterator();
                        while (iterator.hasNext()) {
                            Page subPage = iterator.next();
                            Crawler.this.docQueue.add(subPage);
                            iterator.remove();
                        }
                    }
                } catch (IOException e) {
                    logger.error("Parse task occur exception {} .",e.getMessage());
                }
            });
        }
    }

    //清洗
    private void pipeline() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Parse occur exception {} .",e.getMessage());
            }
            final Page page = this.detailQueue.poll();
            if (page == null) {
                continue;
            }
            //多线程
            this.executorService.submit(() -> {
                for (Pipeline pipeline : Crawler.this.pipelineList) {
                    pipeline.pipeline(page);
                }
            });

        }
    }

    public void addPage(Page page) {
        this.docQueue.add(page);
    }

    public void addPrase(Prase prase) {
        this.praseList.add(prase);
    }

    public void addPipeline(Pipeline pipeline) {
        this.pipelineList.add(pipeline);
    }

    /**
     * 停止爬虫
     */
    public void stop() {
        if (this.executorService != null && !this.executorService.isShutdown()) {
            this.executorService.shutdown();
        }
        logger.info("Crawler sotpped ...");
    }

}
