package org.zkmaster.backend.exceptions.node;

public class NodeCreateException extends Exception {

    public NodeCreateException(String host, String path, String value) {
        super("ZKM EXCEPTION: NodeCreateException:"
                + " host=" + host + ", path=" + path + ", value=" + value
                + " create is failed.");
    }
}
