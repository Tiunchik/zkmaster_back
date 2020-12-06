package org.zkmaster.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * TODO Feature - Сравнение данных в зк - ПОКА идея в том что бы это делал фронт, но можно и на бэке предусмотреть, к примеру что бы реализовать функцию Merge ZK
 * TODO Feature - Копирование из одного в другой

 */
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        System.out.println("APP MAIN - START");
        SpringApplication.run(BackendApplication.class, args);
        System.out.println("APP MAIN - STOP");
    }

    private static void print(Object msg) {
        System.out.println("Main - " + msg);
    }

    private static void print(String info, Object obj) {
        System.out.println("Main - " + info + " : " + obj);
    }

    private static void print(String info, String string) {
        System.out.println("Main - " + info + " : |" + string + '|');
    }

    private static void print(String info, List<String> strings) {
        System.out.println("Main - " + info + " :");
        for (var each : strings) {
            System.out.println("Main - " + info + " elem: |" + each + '|');
        }
    }

}
