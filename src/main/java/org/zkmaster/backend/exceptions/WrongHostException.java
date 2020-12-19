package org.zkmaster.backend.exceptions;

public class WrongHostException extends Exception {

    public WrongHostException(String wrongHostValue) {
        super("ZKM EXCEPTION: WrongHostException: "
                + "host: " + wrongHostValue
                + " isn't exists! Can't create connection with not exists server.");
    }

}
