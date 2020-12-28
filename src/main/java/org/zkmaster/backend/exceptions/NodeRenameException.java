package org.zkmaster.backend.exceptions;

public class NodeRenameException extends Exception {

    public NodeRenameException(String host, String path, String newName) {
        super("ZKM EXCEPTION: NodeRenameException:"
                + " host : " + host
                + " path : " + path
                + " newName : " + newName
                + " Rename failed! Something went wrong.");
    }

}
