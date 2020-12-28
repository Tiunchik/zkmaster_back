package org.zkmaster.backend.zdeprecated;

import org.springframework.stereotype.Repository;
import org.zkmaster.backend.repositories.HostProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
@Repository
public class HostServiceDepDefault implements HostServiceDep {
    private final Map<String, HostProvider> repositories = new HashMap<>();

    @Override
    public void put(String host, HostProvider repository) {
        repositories.put(host, repository);
    }

    @Override
    public HostProvider get(String host) {
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
