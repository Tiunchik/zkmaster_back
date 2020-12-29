package org.zkmaster.backend.exceptions;

public class WrongHostAddressException extends Exception {

    public WrongHostAddressException(String hostAddress) {
        super("ZKM EXCEPTION: WrongHostAddressException:"
                + " host=" + hostAddress
                + " isn't exists! Can't create connection with not exists server.");
    }

}
