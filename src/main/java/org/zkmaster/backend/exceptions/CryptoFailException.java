package org.zkmaster.backend.exceptions;

/**
 * Meaning: Fail to encode|decode value.
 * Possible: Something wrong, IDK. -\_(-_-)_/-
 */
public class CryptoFailException extends Exception {

    public CryptoFailException(String value) {
        super("ZKM EXCEPTION: CryptoFailException:"
                + " value=" + value + " crypt is failed.");
    }

}
