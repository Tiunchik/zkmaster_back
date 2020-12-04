package org.zkmaster.backend.services;

import org.zkmaster.backend.repositories.ZKNodeRepository;

public interface ZKConnectionManager {

    /**
     * create connection.
     *
     * @param hostUrl -
     * @return repository for wrk with data OR null!
     */
    ZKNodeRepository createConnection(String hostUrl);

    /**
     * re-create connection.
     *
     * @param hostUrl -
     * @return repository for wrk with data.
     */
    ZKNodeRepository reconnect(String hostUrl);

    /**
     * delete connection.
     * <p>
     * In future, it should clean a back-end from useless connections.
     * Like: time in 3 hours, Front-end need to send MSG that "we still need this connection". Else:
     * Delete connection by this method.
     *
     * @param hostUrl -
     */
    void deleteConnection(String hostUrl);


}
