package org.zkmaster.backend.exceptions.node;
/**
 * Meaning: Fail to create new Node on real-server.
 * Possible: Any problems with real-serve.
 */
public class NodeSaveException extends Exception {

    public NodeSaveException(String host, String path, String value) {
        super("ZKM EXCEPTION: NodeSaveException:"
                + " host=" + host + ", path=" + path + ", value=" + value
                + " save is failed.");
    }
}
