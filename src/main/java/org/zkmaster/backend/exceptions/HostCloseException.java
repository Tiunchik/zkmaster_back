package org.zkmaster.backend.exceptions;

/**
 * Meaning: Fail to close connection with real server.
 * Possible: usually any IOExceptions.
 */
public class HostCloseException extends Throwable {
    
    public HostCloseException(String host) {
        super("ZKM EXCEPTION: HostCloseException: Fail to close host(" + host + ')');
    }
    
}
