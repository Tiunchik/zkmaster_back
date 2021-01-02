package org.zkmaster.backend.mocks;

import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.exceptions.HostWrongAddressException;
import org.zkmaster.backend.factories.HostFactory;
import org.zkmaster.backend.repositories.HostProvider;
import org.zkmaster.backend.repositories.HostProviderDefault;

public class HostFactoryMock implements HostFactory {
    private Host mockHost;
    @Override
    public HostProvider makeHostProvider(String host) throws HostWrongAddressException {
        return new HostProviderDefault(mockHost);
    }

    public Host getMockHost() {
        return mockHost;
    }

    public void setMockHost(Host mockHost) {
        this.mockHost = mockHost;
    }
}
