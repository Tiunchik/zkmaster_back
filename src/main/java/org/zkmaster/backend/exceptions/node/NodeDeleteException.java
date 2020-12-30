package org.zkmaster.backend.exceptions.node;

/**
 * Meaning: Fail to Delete new Node on real-server.
 * Possible: Any problems with real-serve.
 */
public class NodeDeleteException extends Exception {

    public NodeDeleteException(String host, String path) {
        super("ZKM EXCEPTION: NodeDeleteException:"
                + " host=" + host + ", path=" + path
                + " update is failed.");
    }
}
