package org.zkmaster.backend.exceptions.node;

public class NodeReadException extends Exception {

    public NodeReadException(String host, String path) {
        super("ZKM EXCEPTION: NodeReadException:"
                + " host=" + host + ", path=" + path
                + " read is failed.");
    }
}
