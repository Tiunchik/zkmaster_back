package org.zkmaster.backend.exceptions;

/**
 * Meaning: User Enter host address that is wrong.
 * Possible: misspell || real-server with that address (doesn't exits || closed).
 */
public class HostWrongAddressException extends Exception {

    public HostWrongAddressException(String hostAddress) {
        super("ZKM EXCEPTION: HostWrongAddressException:"
                + " host=" + hostAddress
                + " Can't create connection by this host address!");
    }

}
