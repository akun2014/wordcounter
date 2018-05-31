package com.example.service;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by akun on 2018/5/30.
 */
public class WorldCounterTest {

    @Test
    public void test() {
        String indexPage = "https://www.amazon.com/Kovea-LPG-Adaptor-Small-Silver/product-reviews/B00CFPISZW/ref=cm_cr_arp_d_viewopt_sr?ie=UTF8&reviewerType=all_reviews";
        String indexPage1 = "https://www.amazon.com/Lixada-Camping-Adapter-Outdoor-Propane/product-reviews/B072QXKVJP/ref=cm_cr_dp_d_show_all_top?ie=UTF8&reviewerType=all_reviews";
        List<String> webUrl = new ArrayList<>(1);
        webUrl.add(indexPage);
        webUrl.add(indexPage1);

        WordsCounterService wordsCounterService = new WordsCounterService();
        wordsCounterService.doWorker(webUrl, excludeWord());
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
}
