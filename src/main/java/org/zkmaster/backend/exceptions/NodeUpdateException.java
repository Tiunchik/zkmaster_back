package org.zkmaster.backend.exceptions;

public class NodeUpdateException extends Exception {

    public NodeUpdateException(String host, String path, String value) {
        super("ZKM EXCEPTION: NodeUpdateException: "
                + "host=" + host + ", path=" + path + ", value=" + value
                + " update is failed");
    }
}
