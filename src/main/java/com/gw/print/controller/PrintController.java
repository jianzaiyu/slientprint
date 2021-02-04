package com.gw.print.controller;


import com.gw.print.component.SingletonComponent;
import com.gw.print.model.HttpRequest;
import com.gw.print.model.PrintConfigs;
import com.gw.print.model.ZebraConfigs;
import com.gw.print.service.BasePrintService;
import com.gw.print.service.CommonPrintService;
import com.gw.print.service.ZebraPrintService;
import com.gw.print.support.ConsolePrinter;

/**
 * 控制Controller
 */
public class PrintController {
    private BasePrintService basePrintService = SingletonComponent.basePrintService();
    private CommonPrintService commonPrintService = SingletonComponent.commonPrintService();
    private ZebraPrintService zebraPrintService = SingletonComponent.zebraPrintService();

    public String getPrinters(HttpRequest request) {
        return basePrintService.getPrinters();
    }

    public String getDefaultPrinter(HttpRequest request) {
        return basePrintService.getDefaultPrinter();
    }

    public String printPdf(HttpRequest request) {
        try {
            PrintConfigs config = new PrintConfigs();
            config.setCopies(request.getParameter().get("copies"));
            config.setDuplex(request.getParameter().get("duplex"));
            config.setPrinter(request.getParameter().get("printer"));
            config.setPaperSize(request.getParameter().get("paperSize"));
            config.setOrientation(request.getParameter().get("orientation"));
            config.setScaling(request.getParameter().get("scaling"));
            config.setUrls(request.getParameter().get("url"));
            config.setMultipartFiles(request.getMultiPartFiles());
            return commonPrintService.printPdf(config);
        } catch (Exception e) {
            e.printStackTrace();
            ConsolePrinter.info(e.getMessage());
            ConsolePrinter.info(e.toString());
        }
        return "{\"result\":\"failure\"}";
    }

    public String printZebra(HttpRequest request) {
        try {
            ZebraConfigs zebraConfigs = new ZebraConfigs();
            zebraConfigs.setZebraCode(request.getParameter().get("zebraCode"));
            zebraConfigs.setZebraPrinterIp(request.getParameter().get("zebraPrinterIp"));
            zebraConfigs.setZebraPrinterPort(request.getParameter().get("zebraPrinterPort"));
            zebraConfigs.setZebraPrinterName(request.getParameter().get("zebraPrinterName"));
            return zebraPrintService.printZebra(zebraConfigs);
        } catch (Exception e) {
            e.printStackTrace();
            ConsolePrinter.info(e.getMessage());
        }
        return "{\"result\":\"failure\"}";
    }
}
