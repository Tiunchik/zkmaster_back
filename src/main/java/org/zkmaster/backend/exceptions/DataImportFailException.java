package org.zkmaster.backend.exceptions;

/**
 * Meaning: Fail to Import ${this.data} into ${this.host}.
 * Possible: Any problems with real-serve or something wrong with transaction.
 */
public class DataImportFailException extends Exception {

    public DataImportFailException(String host, String type, int size) {
        super("ZKM EXCEPTION: DataImportFailException:"
                + " host=" + host + ", type=" + type + ", dataLength=" + size
                + " import is failed.");
    }

}
