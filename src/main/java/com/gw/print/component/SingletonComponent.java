package com.gw.print.component;

import com.gw.print.controller.PrintController;
import com.gw.print.service.BasePrintService;
import com.gw.print.service.CommonPrintService;
import com.gw.print.service.FileDownloadService;
import com.gw.print.service.ZebraPrintService;

/**
 * Created by ggs.
 */
public class SingletonComponent {
    private static volatile PrintController printController;
    private static volatile BasePrintService basePrintService;
    private static volatile CommonPrintService commonPrintService;
    private static volatile FileDownloadService fileDownloadService;
    private static volatile ZebraPrintService zebraPrintService;

    private SingletonComponent() {

    }

    public static PrintController printController() {
        if (printController == null) {
            synchronized (SingletonComponent.class) {
                if (printController == null) {
                    printController = new PrintController();
                }
            }
        }
        return printController;
    }

    public static BasePrintService basePrintService() {
        if (basePrintService == null) {
            synchronized (SingletonComponent.class) {
                if (basePrintService == null) {
                    basePrintService = new BasePrintService();
                }
            }
        }
        return basePrintService;
    }

    public static CommonPrintService commonPrintService() {
        if (commonPrintService == null) {
            synchronized (SingletonComponent.class) {
                if (commonPrintService == null) {
                    commonPrintService = new CommonPrintService();
                }
            }
        }
        return commonPrintService;
    }

    public static FileDownloadService fileDownloadService() {
        if (fileDownloadService == null) {
            synchronized (SingletonComponent.class) {
                if (fileDownloadService == null) {
                    fileDownloadService = new FileDownloadService();
                }
            }
        }
        return fileDownloadService;
    }

    public static ZebraPrintService zebraPrintService() {
        if (zebraPrintService == null) {
            synchronized (SingletonComponent.class) {
                if (zebraPrintService == null) {
                    zebraPrintService = new ZebraPrintService();
                }
            }
        }
        return zebraPrintService;
    }
}
