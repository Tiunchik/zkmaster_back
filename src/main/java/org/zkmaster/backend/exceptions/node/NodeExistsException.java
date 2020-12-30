package org.zkmaster.backend.exceptions.node;

/**
 * Meaning: Node this ${this.path} already exists.
 * Possible: bad request.
 */
public class NodeExistsException extends Exception {

    public NodeExistsException(String path) {
        super("ZKM EXCEPTION: NodeExistsException:"
                + " path=" + path
                + " exists! Can't create node that exists.");
    }

}
