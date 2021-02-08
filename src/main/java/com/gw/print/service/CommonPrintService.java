package com.gw.print.service;

import com.gw.print.component.SingletonComponent;
import com.gw.print.model.Bean;
import com.gw.print.model.PrintConfigs;
import com.gw.print.support.ConsolePrinter;
import com.gw.print.support.MediaSizeNameSupport;
import com.gw.print.support.PaperSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import java.awt.print.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by ggs.
 */
@Bean("commonPrintService")
public class CommonPrintService {

    private BasePrintService basePrintService = SingletonComponent.basePrintService();
   // @Autowired("basePrintService")
    //private BasePrintService basePrintService;
    public String printPdf(PrintConfigs userConfigs) throws Exception {
        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        //份数
        attr.add(new Copies(userConfigs.getCopies()));
        attr.add(Finishings.STAPLE);
        //纸张类型
        attr.add(new MediaSizeNameSupport(userConfigs.getPaperSize()));
        //双面打印
        if (userConfigs.isDuplex()) {
            attr.add(Sides.DUPLEX);
        }
        //打印方向
        switch (userConfigs.getOrientation()) {
            case 3:
                attr.add(OrientationRequested.PORTRAIT);
                break;
            case 4:
                attr.add(OrientationRequested.LANDSCAPE);
                break;
            case 5:
                attr.add(OrientationRequested.REVERSE_PORTRAIT);
                break;
            case 6:
                attr.add(OrientationRequested.REVERSE_LANDSCAPE);
                break;
        }
        //缩放类型
        Scaling scaling;
        switch (userConfigs.getScaling().toUpperCase()) {
            case "SHRINK":
                scaling = Scaling.SHRINK_TO_FIT;
                break;
            case "STRETCH":
                scaling = Scaling.STRETCH_TO_FIT;
                break;
            case "SCALE":
                scaling = Scaling.SCALE_TO_FIT;
                break;
            default:
                scaling = Scaling.ACTUAL_SIZE;
                break;
        }
        userConfigs.setScalingParam(scaling);
        //打印机名称
        String printerName = StringUtils.isEmpty(userConfigs.getPrinter()) ? basePrintService.getDefaultPrinter() : userConfigs.getPrinter();
        userConfigs.setPrinter(printerName);


        //文件下载链接
        List<String> urlList = userConfigs.getUrls();
        if (CollectionUtils.isNotEmpty(urlList)) {
            for (String urlPath : urlList) {
                URL url = new URL(new URI(urlPath).toASCIIString());
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5*1000);
                InputStream inputStream = conn.getInputStream();
                userConfigs.setByteLength(inputStream.available());
                print(userConfigs, attr, buildBook(inputStream, userConfigs));
            }
        }
        //所有直传文件
        Map<String, byte[]> files = userConfigs.getMultipartFiles();
        if (MapUtils.isNotEmpty(files)) {
            for (Map.Entry<String, byte[]> entry : files.entrySet()) {
                byte[] bytes = entry.getValue();

                print(userConfigs, attr, buildBook(new ByteArrayInputStream(bytes), userConfigs));

            }
        }
        return "{\"result\":\"success\"}";
    }


    private Book buildBook(InputStream inputStream, PrintConfigs configs) throws IOException {
        long start = System.currentTimeMillis();
        PDDocument document = PDDocument.load(inputStream);
        configs.setCostTime(System.currentTimeMillis() - start);
        PDFPrintable pdfPrintable = new PDFPrintable(document, configs.getScalingParam());
        Paper paper = new Paper();
        double width = PaperSupport.getPaperWidth(configs.getPaperSize());
        double height = PaperSupport.getPaperHeight(configs.getPaperSize());
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(configs.getOrientation() == 3 ? PageFormat.PORTRAIT : PageFormat.LANDSCAPE);
        Book book = new Book();
        book.append(pdfPrintable, pageFormat);
        return book;
    }


    private synchronized void print(PrintConfigs configs, PrintRequestAttributeSet attr, Book book) throws Exception {
        ConsolePrinter.info("选择使用打印机: " + configs.getPrinter());
        ConsolePrinter.info("打印分数: " + configs.getCopies());
        ConsolePrinter.info("纸张大小: " + configs.getPaperSize());
        ConsolePrinter.info("缩放类型: " + configs.getScaling());
        ConsolePrinter.info("打印方向: " + configs.getOrientation());
        ConsolePrinter.info("双面打印: " + configs.isDuplex());
        ConsolePrinter.info("文件长度: " + configs.getByteLength());
        ConsolePrinter.info("下载时长: " + configs.getCostTime() + "ms");
        PrintService printer = basePrintService.getPrinterByName(configs.getPrinter());
        if (printer == null) {
            throw new IOException("没有对应的打印服务");
        }
        if (printer.getAttribute(PrinterIsAcceptingJobs.class) == PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS) {
            throw new PrinterException("打印机不可用");
        }
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintService(printer);
        printerJob.setPageable(book);
        printerJob.print(attr);
        ConsolePrinter.info("打印成功", 3);
    }
}
