package org.zkmaster.backend.exceptions.node;

public class NodeDeleteException extends Exception {

    public NodeDeleteException(String host, String path) {
        super("ZKM EXCEPTION: NodeDeleteException:"
                + " host=" + host + ", path=" + path
                + " update is failed.");
    }
}
