package org.zkmaster.backend.exceptions;

public class HostNotFoundException extends Exception {

    public HostNotFoundException(String host) {
        super("ZKM EXCEPTION: HostNotFoundException: "
                + "host=" + host);
    }

}
