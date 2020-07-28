package com.gw.print.model;

import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 打印配置参数
 */
public class PrintConfigs {
    private List<String> urls;
    private String printer;
    private int copies;
    private boolean duplex;
    private String paperSize;
    private Map<String,byte[]> multipartFiles;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(String origin, String urlStr) {
        String[] urlArr = urlStr.split(";");
        urls = new ArrayList<>();
        for (String url : urlArr) {
            url = StringUtils.trim(url);
            if (StringUtils.isNotEmpty(url)) {
                if (StringUtils.isNotEmpty(origin)) {
                    url = origin + url;
                }
                urls.add(url);
            }
        }
    }

    public void setUrls(String urlStr) {
        if(!StringUtils.isEmpty(urlStr)){
            urls = Arrays.asList(urlStr.split(";"));
        }

    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public boolean isDuplex() {
        return duplex;
    }

    public void setDuplex(boolean duplex) {
        this.duplex = duplex;
    }

    public String getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(String paperSize) {
        this.paperSize = paperSize;
    }

    public Map<String, byte[]> getMultipartFiles() {
        return multipartFiles;
    }

    public void setMultipartFiles(Map<String, byte[]> multipartFiles) {
        this.multipartFiles = multipartFiles;
    }
}
