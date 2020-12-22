package org.zkmaster.backend.exceptions;

public class NodeRenameException extends Exception {

    public NodeRenameException(String host, String path, String value) {
        super("ZKM EXCEPTION: NodeRenameException:"
                + " host : " + host
                + " path : " + path
                + " value : " + value
                + " Rename failed! Something went wrong.");
    }

}
