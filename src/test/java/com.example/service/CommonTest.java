package com.example.service;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by akun on 2018/6/1.
 */
public class CommonTest {


    @Test
    public void test() {
        Collection<File> files = FileUtils.listFiles(new File("/tmp"), new String[]{"xls"}, false);
        List<String> fileNames = new ArrayList<>(files.size());
        files.forEach(file -> {
            fileNames.add(file.getName());
        });

        System.out.println(fileNames);
    }
}
