package com.gw.print.support;

import com.jfoenix.controls.JFXTextArea;
import javafx.scene.Parent;

/**
 * console打印
 */
public class ConsolePrinter {
    private static JFXTextArea console;

    public static void init(Parent root){
        ConsolePrinter.console = (JFXTextArea)root.lookup("#console");
    }

    public static void info(String log){
        console.appendText(log);
        console.appendText("\n");
    }

    public static void clear(){
        console.clear();
    }
}
