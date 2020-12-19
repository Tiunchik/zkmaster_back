package org.zkmaster.backend.exceptions;

public class NodeExistsException extends Exception {

    public NodeExistsException(String path) {
        super("ZKM EXCEPTION: NodeExistsException: "
                + "path : " + path
                + " exists! Can't create node that exists.");
    }

}
