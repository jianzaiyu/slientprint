package com.gw.print.service;

import com.gw.print.model.PrintConfigs;
import com.gw.print.support.ConsolePrinter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by ggs.
 */
public class CommonPrintService {

    private BasePrintService basePrintService = new BasePrintService();

    private FileDownloadService fileDownloadService = new FileDownloadService();

    public String printPdf(PrintConfigs userConfigs) throws IOException, PrinterException, InterruptedException {
        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        //份数
        attr.add(new Copies(userConfigs.getCopies()));
        //装订
        attr.add(Finishings.STAPLE);
        //纸张
        switch (userConfigs.getPaperSize()) {
            case "A3":
                attr.add(MediaSizeName.ISO_A3);
                break;
            case "A5":
                attr.add(MediaSizeName.ISO_A5);
                break;
            default:
                attr.add(MediaSizeName.ISO_A4);
        }
        //双面打印
        if (userConfigs.isDuplex()) {
            attr.add(Sides.DUPLEX);
        }
        //打印机名称
        String printerName = StringUtils.isEmpty(userConfigs.getPrinter())?basePrintService.getDefaultPrinter():userConfigs.getPrinter();
        PrintService printer = basePrintService.getPrinterByName(printerName);
        //文件下载链接
        List<String> urlList = userConfigs.getUrls();
        //所有直传文件
        Map<String, byte[]> files = userConfigs.getMultipartFiles();

        ConsolePrinter.info("选择使用打印机: " + printer.getName());
        ConsolePrinter.info("纸张大小: " + userConfigs.getPaperSize());

        if (printer == null) {
            throw new IOException("没有对应的打印服务");
        }
        if (printer.getAttribute(PrinterIsAcceptingJobs.class) == PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS) {
            throw new PrinterException("打印机不可用");
        }

        LinkedBlockingQueue<byte[]> documentsByte;
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintService(printer);

        if (files != null && !files.isEmpty()) {
            for (Map.Entry<String, byte[]> entry : files.entrySet()) {
                print(printerJob, entry.getValue(), attr);
            }

        } else if(!CollectionUtils.isEmpty(urlList)){
            byte[] fileByte;
            documentsByte = fileDownloadService.downloadAllDocument(urlList);
            // 都下载结束，没问题，开始打印
            while ((fileByte = documentsByte.poll()) != null) {
                print(printerJob, fileByte, attr);
            }
        }else {
            ConsolePrinter.info("没有可打印的内容");
            throw new IOException("没有可打印的内容");
        }
        ConsolePrinter.info("打印成功");
        return "{\"result\":\"success\"}";
    }


    private void print(PrinterJob printerJob, byte[] fileByte, PrintRequestAttributeSet attr) {
        ConsolePrinter.info("文件长度: "+fileByte.length);
        try (PDDocument document = PDDocument.load(fileByte)) {
            printerJob.setPageable(new PDFPageable(document));
            printerJob.print(attr);
        } catch (Exception e) {
            ConsolePrinter.info("打印错误: " + e.getMessage());
        }
    }
}
