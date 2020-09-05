package com.gw.print.support;

import com.jfoenix.controls.JFXTextArea;
import javafx.scene.Parent;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * console打印
 */
public class ConsolePrinter {
    private static JFXTextArea console;

    public static void init(Parent root) {
        ConsolePrinter.console = (JFXTextArea) root.lookup("#console");
    }

    public static void info(String log) {
        console.appendText(log);
        console.appendText("\n");
    }

    public static void err(String message, Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        info(message + sw.toString());
    }

    public static void clear() {
        console.clear();
    }
}
