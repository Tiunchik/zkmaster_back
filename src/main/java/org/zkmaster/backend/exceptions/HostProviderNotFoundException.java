package org.zkmaster.backend.exceptions;

import org.zkmaster.backend.repositories.HostProvider;

/**
 * Meaning: {@link HostProvider} doesn't find.
 * Possible: I don't know, seriously, if you catch this Exception,
 * check the logic in code and any code-magic, like multi-threading...
 * Finally it just a reinsurance, in situation then magic happen...
 */
public class HostProviderNotFoundException extends Exception {

    public HostProviderNotFoundException(String host) {
        super("ZKM EXCEPTION: HostProviderNotFoundException:"
                + " host=" + host
                + " host doesn't found.");
    }

}
