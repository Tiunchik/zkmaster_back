package org.zkmaster.backend.exceptions;

public class ImportFailException extends Exception {

    public ImportFailException(String host, String type, int size) {
        super("ZKM EXCEPTION: ImportFailException: "
                + "host=" + host + ", type=" + type + ", dataLength=" + size
                + " import is failed");
    }

}
