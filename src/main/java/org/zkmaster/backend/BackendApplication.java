package org.zkmaster.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO Feature - Сравнение данных в зк - ПОКА идея в том что бы это делал фронт, но можно и на бэке предусмотреть, к примеру что бы реализовать функцию Merge ZK
 * TODO Feature - Копирование из одного в другой
 */
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        System.err.println("ZKM Back-end INIT :: FINISH");
        System.err.println("ZKM Back-end RUNTIME :: START");
    }


}
