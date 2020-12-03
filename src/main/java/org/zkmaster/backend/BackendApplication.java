package org.zkmaster.backend;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.zkmaster.backend.entity.ZKNode;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 1. Мару для подключений к зк И фабрика для подключений
 * TODO 2. Сравнение данных в зк
 * TODO 3. Копирование из одного в другой
 * TODO 4. Удаление
 * TODO 5. Добавление
 */
@SpringBootApplication
public class BackendApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BackendApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BackendApplication.class, args);
        ZooKeeper zoo = context.getBean("zoo", ZooKeeper.class);
        try {

            System.out.println();
            LOG.info("APP START");
            mainCode(zoo);
            LOG.info("APP FINISH");

        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static ZKNode getNode(ZooKeeper zoo, String path) throws KeeperException, InterruptedException {
//        print("path(current)", path);
        String nodeValue = new String(zoo.getData(path, false, null), StandardCharsets.UTF_8);

        List<ZKNode> children = null;
        var getChildren = zoo.getChildren(path, false);
        if (getChildren != null && !getChildren.isEmpty()) {
            children = new ArrayList<>();
            for (var childName : getChildren) {

                String childPath = ("/".equals(path))
                        ? path + childName
                        : path + "/" + childName;
                children.add(getNode(zoo, childPath));
            }
        }
        return new ZKNode(path, nodeValue, children);
    }

    private static void mainCode(ZooKeeper zoo) throws KeeperException, InterruptedException {
//        final String firstPath = "/firstNode";
        final String rootPath = "/";

        var rsl = getNode(zoo, rootPath);

        print("rsl", "\r\n" + rsl);


//        // dynamic nude
//        String parent;
//        String path;
//        String value;
//        List<String> children;
//
//
//        parent = "/";
//        path = firstPath;
//        byte[] temp = zoo.getData(path, false, null);
//        value = new String(temp, StandardCharsets.UTF_8);
//        children = zoo.getChildren("/", false);


//        print("parent", parent);
//        print("path", path);
//        print("value", value);
//        print("children", children);
//        var root = new ZKNode(fullPath, parent, path, value, null, null);


//        byte[] rootValue = zoo.getData("/firstNode", false, null);

//                print("children: ");
//                children.forEach(BackendApplication::print);

//                var scanner = new Scanner(System.in);
//                if (scanner.nextLine().equals("close")) {
//                    run = false;
//                }

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
