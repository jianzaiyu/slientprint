package com.gw.print;

import com.gw.print.component.MySystemTray;
import com.gw.print.netty.HttpServer;
import com.gw.print.support.ConsolePrinter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    public static void main(String[] args) {
        //交互界面启动
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("浏览器打印插件");
        primaryStage.setScene(new Scene(root, 600, 400));
//        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            MySystemTray.getInstance().hide(primaryStage);
//            new DialogBuilder(primaryStage).setNegativeBtn("取消", new DialogBuilder.OnClickListener() {
//                @Override
//                public void onClick() {
//                    //点击取消按钮之后执行的动作
//                    event.consume();
//                }
//            }).setPositiveBtn("确定", new DialogBuilder.OnClickListener() {
//                @Override
//                public void onClick() {
//                    //点击确定按钮之后执行的动作
//                    System.exit(0);
//                }
//            }).setTitle("提示").setMessage("确定要退出吗?").create();
        });
        MySystemTray.getInstance().listen(primaryStage);
        ConsolePrinter.init(root);

        //后端服务启动
        Thread backend = new Thread(HttpServer::start);
        backend.setDaemon(true);
        backend.start();
    }
}
