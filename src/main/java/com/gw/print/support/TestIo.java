package com.gw.print.support;
 
import jdk.nashorn.internal.runtime.URIUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TestIo {
 
    /**
     * 从网络Url中下载文件
     * @param urlStr        下载文件路径
     * @param fileName      文件名称包含类型
     * @param savePath      保存本地的路径
     * @throws IOException
     */
    public void testIO(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为5秒
        conn.setConnectTimeout(5*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //创建保存路径
        File saveDir = new File(savePath);
        if(!saveDir.exists()) {
            saveDir.mkdir();
        }
        //创建文件
        File file = new File(saveDir+File.separator+fileName);
        //文件输出流
        FileOutputStream fout = new FileOutputStream(file);
        //输出文件
        byte[] buff = new byte[4096];
        int len = -1;
        while ((len = inputStream.read(buff)) != -1) {
            fout.write(buff,0,len);
        }
        //关闭输出输入流
        fout.close();
        inputStream.close();
        System.out.println("info:"+url+" download success");
    }

//    public static void main(String[] args) throws IOException {
//        TestIo testIo = new TestIo();
//        for (int i = 0; i < 10; i++) {
//            testIo.testIO("http://2d13c9a9a79bdc47.test.paas.gwm.cn/api/v1/public/user/rzit/bucket/instructtemp/file/焊装车间_机舱线_LGWEF6A57KH911150_44_A4.pdf?key=15fae4d11f7a1a1c"
//                    ,System.currentTimeMillis()+".pdf","C:\\Users\\GGs\\Desktop\\pdftest");
//            testIo.testIO("http://2d13c9a9a79bdc47.test.paas.gwm.cn/api/v1/public/user/rzit/bucket/instructtemp/file/焊装车间_机舱线_LGWEF6A57KH911150_43_A4.pdf?key=a182920d2aceeacb"
//                    ,System.currentTimeMillis()+".pdf","C:\\Users\\GGs\\Desktop\\pdftest");
//        }
//    }


}