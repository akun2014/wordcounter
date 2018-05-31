package com.example.rest;

import com.example.service.WordsCounterService;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by akun on 2018/5/30.
 */
@Slf4j
@RestController
@RequestMapping(value = "/rest/word",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class WordCounterRest {

    @Resource
    WordsCounterService wordsCounterService;


    @PostMapping("/counter")
    public void doWorker(HttpServletResponse response, @RequestBody RequestBean requestBean) {
//        String indexPage = "https://www.amazon.com/Kovea-LPG-Adaptor-Small-Silver/product-reviews/B00CFPISZW/ref=cm_cr_arp_d_viewopt_sr?ie=UTF8&reviewerType=all_reviews";
//        String indexPage1 = "https://www.amazon.com/Lixada-Camping-Adapter-Outdoor-Propane/product-reviews/B072QXKVJP/ref=cm_cr_dp_d_show_all_top?ie=UTF8&reviewerType=all_reviews";
//        List<String> webUrl = new ArrayList<>(1);
//        webUrl.add(indexPage);
//        webUrl.add(indexPage1);
        wordsCounterService.doWorker(requestBean.getUrls(), requestBean.getExclude());

        try {
            String dateTime = new DateTime().toString("yyyy-MM-dd");
            String fileName = "/tmp/words_" + dateTime + ".xls";
            File file = new File(fileName);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private Set<String> excludeWord() {
        Set<String> set = new HashSet<>();
        set.add("the");
        set.add("to");
        set.add("a");
        set.add("and");
        set.add("it");
        set.add("i");
        set.add("with");
        set.add("is");
        return set;
    }

    @Getter
    @Setter
    static class RequestBean {
        List<String> urls;
        Set<String> exclude;
    }

    public static void main(String[] args) {
                String indexPage = "https://www.amazon.com/Kovea-LPG-Adaptor-Small-Silver/product-reviews/B00CFPISZW/ref=cm_cr_arp_d_viewopt_sr?ie=UTF8&reviewerType=all_reviews";
        String indexPage1 = "https://www.amazon.com/Lixada-Camping-Adapter-Outdoor-Propane/product-reviews/B072QXKVJP/ref=cm_cr_dp_d_show_all_top?ie=UTF8&reviewerType=all_reviews";
        List<String> webUrl = new ArrayList<>(1);
        webUrl.add(indexPage);
        webUrl.add(indexPage1);

        RequestBean bean = new RequestBean();
        bean.setUrls(webUrl);

        System.out.println(new Gson().toJson(bean));
    }

}
