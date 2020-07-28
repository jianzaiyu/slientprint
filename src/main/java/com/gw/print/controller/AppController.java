package com.gw.print.controller;

import com.gw.print.constants.ServerConstants;
import com.gw.print.netty.HttpServer;
import com.gw.print.support.ConsolePrinter;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

/**
 * 桌面Controller
 */
public class AppController {

    @FXML
    private JFXToggleButton serverSwitch;

    @FXML
    private JFXCheckBox remoteMode;

    @FXML
    public void onSwitch(MouseEvent event) {
        if (serverSwitch.isSelected()) {
            ServerConstants.PRINT_SWITCH = true;
            ConsolePrinter.info("打印服务恢复");
        } else {
            ServerConstants.PRINT_SWITCH = false;
            ConsolePrinter.info("打印服务暂停");
        }
    }

    @FXML
    public void onClearConsole(MouseEvent event) {
        ConsolePrinter.clear();
    }

    @FXML
    public void onRemoteChecked(MouseEvent event) {
        if(remoteMode.isSelected()){
            ServerConstants.REMOTE_MODE = true;
            ConsolePrinter.info("远程打印模式");
        }else {
            ServerConstants.REMOTE_MODE = false;
            ConsolePrinter.info("本地打印模式");
        }
    }


}
