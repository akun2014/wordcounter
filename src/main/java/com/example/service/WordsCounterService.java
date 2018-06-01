package com.example.service;

import com.example.bean.WordBean;
import com.example.utils.ExcelUtil;
import com.example.utils.WordSortUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.utils.WordSortUtil.*;

/**
 * Created by akun on 2018/5/30.
 */
@Service
@Slf4j
public class WordsCounterService {

    public void doWorker(List<String> webUrls, Set<String> excludeWorlds) {
        List<List<StringBuilder>> collect = webUrls.parallelStream().map(url -> {
            try {
                log.info("doworker url:{}", url);
                Document document = getConnection(url).get();
                int pageNumber = getPageNumber(document);
                List<StringBuilder> totalReview = new ArrayList<>(pageNumber);
                for (int i = 1; i <= pageNumber; i++) {
                    String pageUrl = getPageUrl(url, i);
                    try {
                        TimeUnit.MILLISECONDS.sleep(500L);
                        Document doc = getConnection(pageUrl).get();
                        StringBuilder page = parsePage(doc);
                        totalReview.add(page);
                    } catch (Exception e) {
                        createExceptionFile(e.getMessage());
                        log.error("", e);
                    }
                }
                log.info("doworker url:{} done", url);
                return totalReview;
            } catch (IOException e) {
                createExceptionFile(e.getMessage());

                log.error("", e);
            }
            return new ArrayList<StringBuilder>();
        }).collect(Collectors.toList());
        log.info("数据爬取结束....");
        StringBuilder worlds = new StringBuilder();
        for (List<StringBuilder> stringBuilders : collect) {
            for (StringBuilder stringBuilder : stringBuilders) {
                worlds.append(stringBuilder);
            }
        }
        log.info("开始单词统计");
        exportWords(worlds.toString(), excludeWorlds);
    }


    private void exportWords(String txt, Set<String> excludeWorlds) {
        //处理文本切分
        String[] content = WordSortUtil.splitText(txt, " ");

        //清洗单词数据
        String[] words = cleanWord(content, excludeWorlds);
        log.info("words size:{}", words.length);

        //统计单词数量
        Map<String, Integer> count = countWord(words);

        //根据单词频率排序
        Map<String, Integer> wordRate = sortRate(count, true);

        //输出结果
        List<WordBean> list = new ArrayList<>(wordRate.size());

        wordRate.forEach((key, value) -> {
            WordBean wordBean = new WordBean();
            wordBean.setWorld(key);
            wordBean.setCount(value);
            list.add(wordBean);
        });
        log.info("开始导出到excel");
        ExcelUtil.createExcel(list);

    }

    private String getPageUrl(String url, int pageNum) {
        String pageUrl = url + "&pageNumber=" + pageNum;
        log.info("webUrl:{}", pageUrl);
        return pageUrl;
    }


    private int getPageNumber(Document document) {
        Element element = document.getElementById("cm_cr-pagination_bar");
        Element element1 = element.children().get(0);
        Elements children = element1.children();
        Element element2 = children.get(6);
        String pageNum = element2.text();
        log.info("pageNum:{}", pageNum);

        return Integer.parseInt(pageNum);
    }

    private StringBuilder parsePage(Document document) {
        StringBuilder worlds = new StringBuilder(200);
        Element element = document.getElementById("cm_cr-review_list");
        Elements children = element.children();
        for (Element child : children) {
            for (Element element1 : child.children()) {
                if (!element1.hasAttr("id")) {
                    continue;
                }
                Elements children1 = element1.children();
                for (Element element2 : children1.get(3).children()) {
                    worlds.append(element2.text());
                    log.info("review content:{}", element2.text());
                }
            }
        }
        return worlds;
    }

    private Connection getConnection(String url) {
        Connection conn = Jsoup.connect(url);
        conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        conn.header("Accept-Encoding", "gzip, deflate, sdch");
        conn.header("Accept-Language", "zh-CN,zh;q=0.8");
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

        return conn;
    }

    private void createExceptionFile(String message) {
        String filename = new DateTime().toString("yy-MM-dd_HH-mm-ss") + ".txt";
        try {
            FileUtils.writeStringToFile(new File("/tmp/" + filename), message, Charset.forName("UTF-8"));
        } catch (IOException e1) {
            log.error("", e1);
        }
    }

}
