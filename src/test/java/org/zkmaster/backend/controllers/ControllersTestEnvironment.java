package org.zkmaster.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestComponent;
import org.zkmaster.backend.devutil.DevLog;
import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.factories.HostFactory;
import org.zkmaster.backend.mocks.HostFactoryFake;
import org.zkmaster.backend.mocks.HostFake;
import org.zkmaster.backend.repositories.HostContext;
import org.zkmaster.backend.repositories.HostContextDefault;

import java.util.List;

/**
 * CRUD
 * Create - WORK
 * Read - WORK
 * Update(setValue) -  WORK
 * Update(rename) -  WORK
 * Update(rename & setValue) -  WORK
 * Update cascade(rename & setValue) -  WORK
 * Delete -  WORK
 * Delete cascade -  WORK
 * <p>
 * Transform
 * Export - WORK
 * Import - WORK
 * <p>
 * API
 * HealthCheck - WORK
 * Injection save values - WORK
 * Injection update values - WORK
 */
@TestComponent("template")
public class ControllersTestEnvironment {
    private ZKNode testRoot;
    @Autowired
    @Qualifier("hostFactoryFake")
    private HostFactory testFactory;
    private Host testHost;
    @Qualifier("hostContextDefault")
    @Autowired
    private HostContext ctx;

    /**
     * Reset all inner state on default.
     */
    public void initNewTestTempleState() throws Exception {
        testRoot =
                new ZKNode("/", "v", List.of(
                        new ZKNode("/1", "v", List.of(
                                new ZKNode("/1/2-1", "v", List.of(
                                        new ZKNode("/1/2-1/3-1", "v", List.of(
                                                new ZKNode("/1/2-1/3-1/4", "v", List.of())
                                        )),
                                        new ZKNode("/1/2-1/3-2", "v", List.of(
                                                new ZKNode("/1/2-1/3-2/4", "v", List.of())
                                        ))
                                )),
                                new ZKNode("/1/2-2", "v", List.of(
                                ))
                        ))
                ));
        testHost = new HostFake("localhost:2181", testRoot);

        factorySetHostFake(testHost);
    }

    /**
     * Don't set Host for {object ControllersTestEnvironment}, use setter for set ${testHost}
     */
    public void factorySetHostFake(Host newFakeHost) {
        var castFactory = (HostFactoryFake) testFactory;
        castFactory.setHostFake(newFakeHost);

        var castCtx = (HostContextDefault) ctx;
        castCtx.setHostFactory(testFactory);
    }

    public void printRoot() {
        DevLog.print("Temple", "print root", testRoot);
    }

    public void clearEnvironmentContext() {
        ctx.clearContext();
    }

    public ZKNode getTestRoot() {
        return testRoot;
    }

    public HostFactory getTestFactory() {
        return testFactory;
    }

    public Host getTestHost() {
        return testHost;
    }

}
