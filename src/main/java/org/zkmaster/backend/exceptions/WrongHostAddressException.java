package org.zkmaster.backend.exceptions;

public class WrongHostAddressException extends Exception {

    public WrongHostAddressException(String wrongHostValue) {
        super("ZKM EXCEPTION: WrongHostAddressException: "
                + "host: " + wrongHostValue
                + " isn't exists! Can't create connection with not exists server.");
    }

}
