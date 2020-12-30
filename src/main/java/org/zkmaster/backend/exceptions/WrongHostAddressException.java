package org.zkmaster.backend.exceptions;

/**
 * Meaning: User Enter host address that is wrong.
 * Possible: misspell || real-server with that address (doesn't exits || closed).
 */
public class WrongHostAddressException extends Exception {

    public WrongHostAddressException(String hostAddress) {
        super("ZKM EXCEPTION: WrongHostAddressException:"
                + " host=" + hostAddress
                + " Can't create connection by this host address!");
    }

}
