package org.zkmaster.backend.exceptions;

public class NodeSaveException extends Exception {

    public NodeSaveException(String host, String path, String value) {
        super("ZKM EXCEPTION: NodeSaveException: "
                + "host=" + host + ", path=" + path + ", value=" + value
                + " update is failed");
    }
}
