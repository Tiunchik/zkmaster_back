package org.zkmaster.backend.mocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.exceptions.HostWrongAddressException;
import org.zkmaster.backend.factories.HostFactory;
import org.zkmaster.backend.repositories.HostProvider;
import org.zkmaster.backend.repositories.HostProviderDefault;

@Service
public class HostFactoryFake implements HostFactory {
    private final ApplicationContext context;
    private Host hostFake;

    @Autowired
    public HostFactoryFake(ApplicationContext context) {
        this.context = context;
    }


    @Override
    public HostProvider makeHostProvider(String host) throws HostWrongAddressException {
        var watcher = context.getBean("watcherFake", ZKWatcherFake.class);
        watcher.setHost(host);

        var hostCast = (HostFake) hostFake;
        hostCast.setWatcherFake(watcher);
        return new HostProviderDefault(hostFake);
    }

    public Host getHostFake() {
        return hostFake;
    }

    public void setHostFake(Host hostFake) {
        this.hostFake = hostFake;
    }

}
