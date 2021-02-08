package org.zkmaster.backend.devutil;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class LogTemplateRun {
    
    /**
     * TODO - Think about it!!!
     */
    public static <T> void forEach(T[] array, Consumer<T> action) {
        for (var temp : array) {
            action.accept(temp);
        }
    }
    
    /* Printing templates */
    
    private static final String DEV_LOG_INF = "DEV : [*place*] -- *info*";
    private static final String DEV_LOG_VAR = "DEV : [*place*] -- *info* :: *obj*";
    
    private static final String ELEM_STORE_PRINT_START = "DEV : [*place*] -- Print *store* :: *name*";
    private static final String ITERABLE_ELEM =          "DEV : [*place*] -- *index* :: *elem*";
    private static final String MAP_ELEM =               "DEV : [*place*] -- *index* :: key=*key* ### val=*val*" ;
    
    
    
    
    
    // Define color constants
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_WHITE = "\u001B[37m";

    public static void main(String[] args) {


        // Implementation
        System.out.println(TEXT_CYAN + "This text is red!" + TEXT_RESET + " --- Reset Text" + TEXT_BLUE + " New Text");
        
    
//        print("test", "myList", List.of("one", "two"));


//        print("test", "myMap", Map.of("Sajren", 143, "Kirin", 100));
//        print("test", "SB", new KafkaProperties.Security[12]);
    }
    
    public static void print(String place, String info) {
        System.out.println(makeInfoMsg(place, info));
    }
    public static void printErr(String place, String info) {
        System.err.println(makeInfoMsg(place, info));
    }
    
    public static void print(String place, String info, Object obj) {
        System.out.println(printStrategy(place, info, obj));
    }
    public static void printErr(String place, String info, Object obj) {
        System.err.println(printStrategy(place, info, obj));
    }
    
    public static String printStrategy(String place, String info, Object obj) {
        if (obj instanceof List) {
           return makeListMsg(place, info, (List<Object>) obj);
        } else if (obj instanceof Map) {
            return makeMapMsg(place, info, (Map<Object, Object>) obj);
        } else if (obj instanceof Object[]) {
            return makeArrayMsg(place, info, (Object[]) obj);
        }  else {
            return makeObjectMsg(place, info, obj);
        }
    }
    
    
    public static String makeInfoMsg(String place, String info) {
        return DEV_LOG_INF
                .replace("*place*", upperCasePlace(place))
                .replace("*info*", info);
        
    }
    public static String makeObjectMsg(String place, String info, Object obj) {
        return DEV_LOG_VAR
                .replace("*place*", upperCasePlace(place))
                .replace("*info*", info)
                .replace("*obj*", prettyWrapObject(obj));
    }
    public static String makeArrayMsg(String place, String arrayName, Object[] arr) {
        var rsl = new StringJoiner(System.lineSeparator());
        place = upperCasePlace(place);
    
        rsl.add(ELEM_STORE_PRINT_START
                .replace("*place*", place)
                .replace("*store*", "Array")
                .replace("*name*", arrayName)
        );
        if (arr.length != 0) {
            String localTemplate = ITERABLE_ELEM.replace("*place*", place);
            int elemIndex = 0;
            for (Object obj : arr) {
                rsl.add(localTemplate
                        .replace("*index*", String.valueOf(elemIndex++))
                        .replace("*elem*", prettyWrapObject(obj))
                );
            }
        }
        return rsl.toString();
    }
   
    public static String makeListMsg(String place, String listName, List<Object> list) {
        var rsl = new StringJoiner(System.lineSeparator());
        place = upperCasePlace(place);
    
        rsl.add(ELEM_STORE_PRINT_START
                .replace("*place*", place)
                .replace("*store*", "List")
                .replace("*name*", listName)
        );
        if (!list.isEmpty()) {
            String localTemplate = ITERABLE_ELEM.replace("*place*", place);
            int elemIndex = 0;
            for (Object obj : list) {
                rsl.add(localTemplate
                        .replace("*index*", String.valueOf(elemIndex++))
                        .replace("*elem*", prettyWrapObject(obj))
                );
            }
        }
        return rsl.toString();
    }
    public static String makeMapMsg(String place, String mapName, Map<Object, Object> map) {
        var rsl = new StringJoiner(System.lineSeparator());
    
        rsl.add(ELEM_STORE_PRINT_START
                .replace("*place*", upperCasePlace(place))
                .replace("*store*", "Map")
                .replace("*name*", mapName)
        );
        if (!map.isEmpty()) {
            String localTemplate = MAP_ELEM.replace("*place*", place);
            int elemIndex = 0;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                rsl.add(localTemplate
                        .replace("*index*", String.valueOf(elemIndex++))
                        .replace("*key*", prettyWrapObject(entry.getKey()))
                        .replace("*val*", prettyWrapObject(entry.getValue()))
                );
            }
        }
        return rsl.toString();
    }
    
    /**
     * Need for Easy extend object formatting, just add {@code else if}.
     * *not for List, it's for <elementType>.
     */
    private static String prettyWrapObject(Object obj) {
        if (obj instanceof String) {
            return "\"" + obj + "\"";
        } else {
            return obj.toString();
        }
    }
    private static String upperCasePlace(String place) {
        return place.substring(0, 1).toUpperCase() + place.substring(1);
    }
    



//    public static void log(String place, String info, Object obj) {
//        System.err.println("LOG :: [" + place + "] -- " + info + " : " + obj);
//    }
//
//    public static void log(String place, String info) {
//        System.err.println("LOG :: [" + place + "] -- " + info);
//    }
    
    
}
