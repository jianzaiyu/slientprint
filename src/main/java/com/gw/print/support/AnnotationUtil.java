package com.gw.print.support;


import com.gw.print.Main;
import com.gw.print.model.Autowired;
import com.gw.print.model.Bean;

import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;


public class AnnotationUtil {

    /**
     * bean容器
     */
    private static ConcurrentHashMap<String, Object> Map;

    static {
        try {
            getAllBeansToMap();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Autowired注解注入
     *
     * @param object
     */
   public static void annotationIOC(Object object) throws IllegalAccessException, InstantiationException {
        Class clazz = object.getClass();
        //获取类的所有包括私有的属性
        Field[] Fields = clazz.getDeclaredFields();
        for (Field field : Fields) {
            if (field.getAnnotation(Autowired.class) != null) {
                //允许改变私有属性
                field.setAccessible(true);
                Autowired annotation = field.getAnnotation(Autowired.class);
                String value = annotation.value();
                try {
                    field.set(object, Map.get(value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取此工程所有的bean存储到容器中
     *
     * @throws Exception
     *
     */

    public static void getAllBeansToMap() throws Exception {
        Map = new ConcurrentHashMap<>();
        getClassName();
    }
    /**
     * 获取此项目中所有的java全路径名，
     *
     * @throws Exception
     */
    private static void getClassName() throws Exception {
        // 获取到此项目的classes路径
        String path1 = Main.class.getResource("/").getPath();
        File files = new File(path1);
        getClassFileName(files, "");
    }

    /**
     * 获取所有类的全路径名，并把类初始化入容器
     *
     * @param
     * @param
     * @throws Exception
     */
    private static void getClassFileName(File files, String directoryName) throws Exception {
        if (files != null) {
            if (directoryName == null) {
                directoryName = "";
            }
            String name = null;
            File[] listFiles = files.listFiles();
            if (listFiles != null) {
                for (int i = 0; i < listFiles.length; i++) {
                    if (listFiles[i].isDirectory()) {
                        // 为目录
                        name = listFiles[i].getName();
                        File files2 = new File(files.getPath() + "\\" + name);
                        if (directoryName.equals("")) {
                            getClassFileName(files2, directoryName + name);
                        } else {
                            getClassFileName(files2, directoryName + "." + name);

                        }
                    } else {
                        // 不为目录
                        name = listFiles[i].getName();
                        if (name.endsWith(".class")){
                        name = name.substring(0, name.lastIndexOf("."));
                        if (directoryName.equals("")) {
                            setMap(directoryName + name);
                        } else {
                            setMap(directoryName + "." + name);

                        }
                    }}
                }
            }

        }
    }


    /**
     * 将带有@Bean初始化到容器
     *
     * @param name
     * @throws Exception
     */
    private static void setMap(String name) throws Exception {
        Class clazz = Class.forName(name);
        //查找此类上是否有此注解
        Bean bean = (Bean) clazz.getAnnotation(Bean.class);
        if (bean != null) {
            Map.put(bean.value(), clazz.newInstance());

        }



    }
}
