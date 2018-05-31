package com.example.service;

import com.example.bean.WordBean;
import com.example.utils.ExcelUtil;
import com.example.utils.WordSortUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.utils.WordSortUtil.*;
import static org.jsoup.Jsoup.connect;

/**
 * Created by akun on 2017/5/7.
 */
@Slf4j
public class MyTest {

    StringBuilder worldFile = new StringBuilder(9999);

    @Test
    public void test() throws IOException {
        String indexPage = "https://www.amazon.com/Kovea-LPG-Adaptor-Small-Silver/product-reviews/B00CFPISZW/ref=cm_cr_arp_d_viewopt_sr?ie=UTF8&reviewerType=all_reviews";
        Document document = connect(indexPage).get();
        int pageNumber = getPageNumber(document);

        for (int i = 1; i <= pageNumber; i++) {

            Document doc = writeOrLoad(i);
            parsePage(doc);
        }

        FileUtils.writeStringToFile(new File("./world.txt"), worldFile.toString(), "UTF-8");

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

    private void parsePage(Document document) {
        Element element = document.getElementById("cm_cr-review_list");
        Elements children = element.children();
        for (Element child : children) {
            for (Element element1 : child.children()) {
                if (!element1.hasAttr("id")) {
                    continue;
                }
                Elements children1 = element1.children();
                for (Element element2 : children1.get(3).children()) {
                    worldFile.append(element2.text());
                    log.info("text:{}", element2.text());
                }
            }
        }
    }


    private String getWebUrl(int pageNum) {
        String url = "https://www.amazon.com/Kovea-LPG-Adaptor-Small-Silver/product-reviews/B00CFPISZW/ref=cm_cr_arp_d_viewopt_sr?ie=UTF8&reviewerType=all_reviews&pageNumber=" + pageNum;
        return url;
    }

    private Document writeOrLoad(int pageNum) throws IOException {
        String fileName = "./html/test" + pageNum + ".html";
        File file = new File(fileName);

        if (file.exists()) {
            log.info("load from file");
            String string = FileUtils.readFileToString(file, "UTF-8");
            return Jsoup.parse(string);
        }
        Document document = connect(getWebUrl(pageNum)).get();
        FileUtils.write(new File(fileName), document.html(), "utf-8");
        return document;
    }

    @Test
    public void counterTest() throws Exception {
        String string = FileUtils.readFileToString(new File("./world.txt"), "UTF-8");
        //处理文本切分
        String[] content = WordSortUtil.splitText(string, " ");

        //清洗单词数据
        String[] words = cleanWord(content, null);

        //统计单词数量
        Map<String, Integer> count = countWord(words);

        //根据单词频率排序
        Map<String, Integer> wordRate = sortRate(count, true);

        //输出结果
        List<WordBean> list = new ArrayList<>(wordRate.size());
        wordRate.forEach((word, rate) -> {
            WordBean wordBean = new WordBean();
            wordBean.setWorld(word);
            wordBean.setCount(rate);
            list.add(wordBean);
        });
        ExcelUtil.createExcel(list);

    }

    //https://www.amazon.com/Kovea-LPG-Adaptor-Small-Silver/product-reviews/B00CFPISZW/ref=cm_cr_dp_d_show_all_btm?ie=UTF8&reviewerType=all_reviews&pageNumber=1
    //https://www.amazon.com/Kovea-LPG-Adaptor-Small-Silver/product-reviews/B00CFPISZW/ref=cm_cr_arp_d_paging_btm_2?ie=UTF8&reviewerType=all_reviews&pageNumber=2

    //https://www.amazon.com/Lixada-Camping-Adapter-Outdoor-Propane/product-reviews/B072QXKVJP/ref=cm_cr_arp_d_paging_btm_2?ie=UTF8&reviewerType=all_reviews&pageNumber=2
    //https://www.amazon.com/Lixada-Camping-Adapter-Outdoor-Propane/product-reviews/B072QXKVJP/ref=cm_cr_dp_d_show_all_btm?ie=UTF8&reviewerType=all_reviews
}
