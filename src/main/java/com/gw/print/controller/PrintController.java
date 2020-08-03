package com.gw.print.controller;


import com.gw.print.model.HttpRequest;
import com.gw.print.model.PrintConfigs;
import com.gw.print.service.ThePrintService;
import com.gw.print.support.ConsolePrinter;

import java.util.List;

/**
 * 控制Controller
 */
public class PrintController {
    private static PrintController selfInstance = new PrintController();
    private static ThePrintService thePrintService = new ThePrintService();

    private PrintController() {
    }

    public static PrintController getInstance() {
        return selfInstance;
    }

    public String getPrinters(HttpRequest request) {
        return thePrintService.getPrinters();
    }

    public String getDefaultPrinter(HttpRequest request) {
        return thePrintService.getDefaultPrinter();
    }

    public String printPdf(HttpRequest request) {
        try {
            PrintConfigs config = new PrintConfigs();
            config.setCopies(Integer.parseInt(request.getParameter().get("copies")));
            config.setDuplex(Boolean.parseBoolean(request.getParameter().get("duplex")));
            config.setPrinter(request.getParameter().get("printer"));
            config.setPaperSize(request.getParameter().get("paperSize"));
            config.setUrls(request.getParameter().get("url"));
            config.setMultipartFiles(request.getMultiPartFiles());
//            config.setCopies(1);
//            config.setDuplex(false);
//            config.setPrinter(null);
//            config.setPaperSize("A5");
//            config.setMultipartFiles(request.getMultiPartFiles());
            return thePrintService.printPdf(config);
        } catch (Exception e) {
            e.printStackTrace();
            ConsolePrinter.info(e.getMessage());
            return "{\"result\":\"failure\"}";
        }
    }
}
