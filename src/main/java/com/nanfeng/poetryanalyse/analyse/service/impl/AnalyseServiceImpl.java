package com.nanfeng.poetryanalyse.analyse.service.impl;

import com.nanfeng.poetryanalyse.analyse.dao.AnalyseDao;
import com.nanfeng.poetryanalyse.analyse.entity.PoetryInfo;
import com.nanfeng.poetryanalyse.analyse.model.AuthorCount;
import com.nanfeng.poetryanalyse.analyse.model.WordCount;
import com.nanfeng.poetryanalyse.analyse.service.AnalyseService;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.*;

/**
 * Author：nanfeng
 * Created:2019/7/7
 */
public class AnalyseServiceImpl implements AnalyseService {

    //业务层依赖数据库查询层（面向接口编程）
    //分析对象（不关心具体从哪查，只关心接口的实例化对象）
    private final AnalyseDao analyseDao;

    public AnalyseServiceImpl(AnalyseDao analyseDao) {
        this.analyseDao = analyseDao;
    }

    @Override
    public List<AuthorCount> analyseAuthorCount() {
        //此处结果并未排序
        //排序方式：
        //1、DAO层用SQL排序
        //2、Service层进行数据排序
        //对List集合进行排序
        List<AuthorCount> authorCounts = analyseDao.analyseAuthorCount();
        //可替换成Lambda表达式->方法引用->数组排序
        Collections.sort(authorCounts, new Comparator<AuthorCount>() {
            @Override
            public int compare(AuthorCount o1, AuthorCount o2) {
                //按照count升序
                // return o1.getCount()-o2.getCount();
                return o1.getCount().compareTo(o2.getCount());
                //降序*(-1)
            }
        });
        return authorCounts;
    }

    @Override
    public List<WordCount> analyseWordCloud() {

        //1、查询出所有数据
        //2、取出title content
        //3、分词  过滤 /w null len<2 ...
        //4、统计 k-v k是词，v是词频

        Map<String,Integer> map = new HashMap<>();

        List<PoetryInfo> poetryInfos = analyseDao.queryAllPoetryInfo();

        for (PoetryInfo poetryInfo : poetryInfos) {
            List<Term> terms = new ArrayList<>();
            String title = poetryInfo.getTitle();
            String content = poetryInfo.getContent();
            terms.addAll(NlpAnalysis.parse(title).getTerms());
            terms.addAll(NlpAnalysis.parse(content).getTerms());

            //过滤，运用stream流的filter方法或使用迭代器
            //ArrayList是一个并发修改的，不能用for循环来过滤删除
            Iterator<Term> iterator = terms.iterator();
            while (iterator.hasNext()) {
                Term term = iterator.next();
                //词性过滤
                if (term.getNatureStr() == null || term.getNatureStr().equals("w")) {
                    iterator.remove();
                    continue;
                }
                //词的过滤
                if (term.getRealName().length() < 2) {
                    iterator.remove();
                    continue;
                }
                //统计
                String realName = term.getRealName();
                Integer count = 0;
                if (map.containsKey(realName)){
                    count = map.get(realName)+1;
                }else {
                    count=1;
                }
                map.put(realName,count);
            }
        }

        //要返回的是一个List，所以要把Map转成List
        List<WordCount> wordCounts = new ArrayList<>();
        for (Map.Entry<String,Integer> entry : map.entrySet()){
            WordCount wordCount = new WordCount();
            wordCount.setCount(entry.getValue());
            wordCount.setWord(entry.getKey());
            wordCounts.add(wordCount);
        }
        return wordCounts;
    }

    //Ansj分词
//    public static void main(String[] args){
//        Result result = NlpAnalysis.parse("我是新青年，计算机技术666");
//        List<Term> terms = result.getTerms();
//        for (Term term:terms){
//            System.out.println(term.toString());
//        }
//    }
}
