package com.example.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by akun on 2018/6/1.
 */
@Slf4j
@RestController
@RequestMapping(value = "/rest/file")
public class FileDownloadRest {

    @RequestMapping(value = "/download")
    public void download(HttpServletResponse response, @RequestParam("filename") String filename) throws Exception {
        try {
            String fileName = "/tmp/" + filename;
            File file = new File(fileName);
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            log.error("", e);
        }
    }


    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object fileList() {

        Collection<File> files = FileUtils.listFiles(new File("/tmp"), new String[]{"xls", "txt"}, false);
        List<String> fileNames = new ArrayList<>(files.size());
        files.forEach(file -> {
            fileNames.add(file.getName());
        });

        Map<String, List<String>> data = new HashMap<>();
        data.put("files", fileNames);
        return data;
    }
}
