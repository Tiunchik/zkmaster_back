package org.zkmaster.backend.exceptions.node;

import org.zkmaster.backend.repositories.HostProviderDefault;

/**
 * Meaning: Something happened while program read host-value from real-server.
 * Possible: {@link HostProviderDefault#readHostValue()}.
 */
public class NodeReadException extends Exception {

    public NodeReadException(String host, String path) {
        super("ZKM EXCEPTION: NodeReadException:"
                + " host=" + host + ", path=" + path
                + " read is failed.");
    }
}
