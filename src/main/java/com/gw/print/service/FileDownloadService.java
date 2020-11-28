package com.gw.print.service;

import com.gw.print.support.ConsolePrinter;
import com.gw.print.support.URLEncoderHZ;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by ggs.
 */
public class FileDownloadService {

    /**
     * 下载多个待打印的文件
     *
     * @param urlList 文件地址
     * @return 下载的文件
     */
    public LinkedBlockingQueue<byte[]> downloadAllDocument(List<String> urlList) throws InterruptedException, IOException {
        LinkedBlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newCachedThreadPool(r -> new Thread(r, "t_iop_download_" + r.hashCode()));
        CountDownLatch count = new CountDownLatch(urlList.size());
        AtomicBoolean downFlag = new AtomicBoolean(true);
        // 多线程下载文件，都下载成功之后再统一打印。
        for (String url : urlList) {
            if (StringUtils.isEmpty(url)) {
                count.countDown();
                continue;
            }
            executorService.submit(() -> {
                try {
                    queue.add(download(url));
                } catch (Exception e) {
                    // 下载失败
                    ConsolePrinter.err("下载错误信息3:",e);
                    downFlag.set(false);
                } finally {
                    count.countDown();
                }
            });
        }
        // 等待计数器计数
        count.await();
        long endTime = System.currentTimeMillis();
        System.out.println("下载结束，耗时：" + (endTime - startTime));
        ConsolePrinter.info("下载结束，耗时：" + (endTime - startTime));
        // 如果下载失败，则清空队列
        if (!downFlag.get()) {
            throw new IOException("获取文件失败");
        }
        return queue;
    }

    private byte[] download(String url) {
        long startTime = System.currentTimeMillis();
        ByteArrayOutputStream baos = null;
        InputStream i = null;
        try {
            url = StringUtils.trim(url);
            //修复汉字路径无法正常下载
            URL u = new URL(URLEncoderHZ.encode(url,"utf-8"));
            i = u.openStream();
            byte[] b = new byte[1024 * 1024];
            int len;
            baos = new ByteArrayOutputStream();
            while ((len = i.read(b)) != -1) {
                baos.write(b, 0, len);
            }
            baos.flush();
        } catch (IOException e) {
            ConsolePrinter.err("下载错误信息1:",e);
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (i != null) {
                    i.close();
                }
            } catch (IOException e) {
                ConsolePrinter.err("下载错误信息2:",e);
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("url:" + url + "\n下载单个文件耗时：" + (endTime - startTime));
        ConsolePrinter.info("url:" + url + "\n下载单个文件耗时：" + (endTime - startTime));
        if (baos == null) {
            return null;
        }
        return baos.toByteArray();
    }
}
