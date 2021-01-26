package com.gw.print.model;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.printing.Scaling;

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
    private String scaling;
    private int orientation;
    private int copies;
    private boolean duplex;
    private int paperSize;
    private Map<String,byte[]> multipartFiles;
    public List<String> getUrls() {
        return urls;
    }



    private Scaling scalingParam;
    private long costTime;
    private int byteLength;



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

    public String getScaling() {
        return scaling;
    }

    public void setScaling(String scaling) {
        if (StringUtils.isEmpty(scaling)) {
            this.scaling = "ACTUAL";
        }else {
            this.scaling = scaling;
        }
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(String copies) {
        if (StringUtils.isEmpty(copies)) {
            this.copies = 1;
        }else {
            this.copies = Integer.parseInt(copies);
        }
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        if (StringUtils.isEmpty(orientation)) {
            this.orientation = 3;
        }else {
            this.orientation = Integer.parseInt(orientation);
        }
    }

    public boolean isDuplex() {
        return duplex;
    }

//    public void setDuplex(boolean duplex) {
//        this.duplex = duplex;
//    }

    public void setDuplex(String duplex) {
        if (StringUtils.isEmpty(duplex)) {
            this.duplex = false;
        }else {
            this.duplex = Boolean.parseBoolean(duplex);
        }
    }

    public int getByteLength() {
        return byteLength;
    }

    public void setByteLength(int byteLength) {
        this.byteLength = byteLength;
    }

    public int getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(String paperSize) {
        if (StringUtils.isEmpty(paperSize)) {
            this.paperSize = 4;
        }else {
            this.paperSize = Integer.parseInt(paperSize);
        }
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public Scaling getScalingParam() {
        return scalingParam;
    }

    public void setScalingParam(Scaling scalingParam) {
        this.scalingParam = scalingParam;
    }

    public Map<String, byte[]> getMultipartFiles() {
        return multipartFiles;
    }

    public void setMultipartFiles(Map<String, byte[]> multipartFiles) {
        this.multipartFiles = multipartFiles;
    }
}
