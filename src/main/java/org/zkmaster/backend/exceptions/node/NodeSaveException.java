package org.zkmaster.backend.exceptions.node;

public class NodeSaveException extends Exception {

    public NodeSaveException(String host, String path, String value) {
        super("ZKM EXCEPTION: NodeSaveException:"
                + " host=" + host + ", path=" + path + ", value=" + value
                + " save is failed.");
    }
}
