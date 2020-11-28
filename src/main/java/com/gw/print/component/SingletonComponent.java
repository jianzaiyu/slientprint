package com.gw.print.component;

import com.gw.print.controller.PrintController;
import com.gw.print.service.BasePrintService;
import com.gw.print.service.CommonPrintService;
import com.gw.print.service.FileDownloadService;
import com.gw.print.service.ZebraPrintService;

/**
 * Created by ggs.
 */
public class SingletonComponent{
    public final static PrintController printController = new PrintController();
    public final static BasePrintService basePrintService = new BasePrintService();
    public final static CommonPrintService commonPrintService = new CommonPrintService();
    public final static FileDownloadService fileDownloadService = new FileDownloadService();
    public final static ZebraPrintService zebraPrintService = new ZebraPrintService();
}
