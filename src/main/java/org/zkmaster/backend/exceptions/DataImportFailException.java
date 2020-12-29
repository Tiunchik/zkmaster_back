package org.zkmaster.backend.exceptions;

public class DataImportFailException extends Exception {

    public DataImportFailException(String host, String type, int size) {
        super("ZKM EXCEPTION: DataImportFailException:"
                + " host=" + host + ", type=" + type + ", dataLength=" + size
                + " import is failed.");
    }

}
