package com.gw.print.service;

import com.gw.print.model.Bean;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.standard.*;
import java.awt.print.PrinterJob;
import java.util.*;

/**
 * 打印服务service
 */
@Bean("basePrintService")
public class BasePrintService {

    public String getPrinters() {
        Map<String, PrintService> printers = new HashMap<>();
        // 查找所有的可用的打印服务
        PrintService[] printServices = PrinterJob.lookupPrintServices();
        for (PrintService printService : printServices) {
            // 能提供服务的打印机
            if (printService.getAttribute(PrinterIsAcceptingJobs.class) != PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS) {
                printers.put(printService.getName(), printService);
            }
        }
        return JSONArray.fromObject(printers.keySet()).toString();
    }

    public PrintService getPrinterByName(String printerName) {
        PrintService result = null;
        // 查找所有的可用的打印服务
        PrintService[] printServices = PrinterJob.lookupPrintServices();
        for (PrintService printService : printServices) {
            // 能提供服务的打印机
            if (printService.getAttribute(PrinterIsAcceptingJobs.class) != PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS) {
                if (!StringUtils.isEmpty(printerName) && printerName.equals(printService.getName())) {
                    result = printService;
                }
            }
        }
        return result;
    }

    public String getDefaultPrinter() {
        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        return defaultService == null ? null : defaultService.getName();
    }


}
