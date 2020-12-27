package org.zkmaster.backend.repositories;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ConnectionRepositoryDefault implements ConnectionRepository {
    private final Map<String, ZKNodeRepository> repositories = new HashMap<>();

    @Override
    public void put(String host, ZKNodeRepository repository) {
        repositories.put(host, repository);
    }

    @Override
    public ZKNodeRepository get(String host) {
        return repositories.get(host);
    }

    @Override
    public void remove(String host) {
        repositories.remove(host);
    }

    @Override
    public boolean contains(String host) {
        return repositories.containsKey(host);
    }

    @Override
    public Map<String, Boolean> containsByHosts(List<String> hosts) {
        Map<String, Boolean> rsl = new HashMap<>(hosts.size());
        for (var host : hosts) {
            rsl.put(host, this.contains(host));
        }
        return rsl;
    }

    @Override
    public int size() {
        return repositories.size();
    }

}
