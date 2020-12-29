package org.zkmaster.backend.exceptions;

public class HostProviderNotFoundException extends Exception {

    public HostProviderNotFoundException(String host) {
        super("ZKM EXCEPTION: HostProviderNotFoundException:"
                + " host=" + host
                + " host doesn't found.");
    }

}
