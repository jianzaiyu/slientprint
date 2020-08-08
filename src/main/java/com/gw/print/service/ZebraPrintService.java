package com.gw.print.service;

import com.gw.print.model.ZebraConfigs;
import fr.w3blog.zpl.utils.ZebraUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Created by ggs.
 */
public class ZebraPrintService {

    public String printZebra(ZebraConfigs zebraConfigs) throws Exception {
        if(StringUtils.isEmpty(zebraConfigs.getZebraCode())){
            throw new Exception("zpl code could't be empty");
        }
        if(!StringUtils.isEmpty(zebraConfigs.getZebraPrinterName())){
            ZebraUtils.printZpl(zebraConfigs.getZebraCode(),zebraConfigs.getZebraPrinterName());
        }else {
            ZebraUtils.printZpl(zebraConfigs.getZebraCode(),zebraConfigs.getZebraPrinterIp(),Integer.parseInt(zebraConfigs.getZebraPrinterPort()));
        }
        return "{\"result\":\"success\"}";
    }
}
