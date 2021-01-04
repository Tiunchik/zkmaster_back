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
 * Update cascade(rename & setValue) - REPAIR!!!!!!!!!!!!!!!!!!!!!!!
 * Delete -  WORK
 * Delete cascade -  WORK
 *
 * Transform
 * Export -
 * Import -
 *
 * API
 * HealthCheck -
 * Injection -
 */
@TestComponent("template")
public class AbstractTemplateControllersTest {
    private ZKNode testRoot;
    @Autowired
    @Qualifier("hostFactoryFake")
    private HostFactory testFactory;
    private Host testHost;
    //    private HostProvider testProvider;
    @Qualifier("hostContextDefault")
    @Autowired
    private HostContext ctx;

    public AbstractTemplateControllersTest() {
    }

    /**
     * Reset all inner state on default.
     */
    public void initNewTestTempleState() throws Exception {
        testRoot = new ZKNode("/", "v", List.of(
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

        var castFactory = (HostFactoryFake) testFactory;
        castFactory.setHostFake(testHost);

//        testProvider = testFactory.makeHostProvider("");

        var castCtx = (HostContextDefault) ctx;
        castCtx.setHostFactory(testFactory);
    }

//    public String getHostAddressFake() {
//        return testHost.getHostAddress();
//    }

    public ZKNode getTestRoot() {
        return testRoot;
    }

    public HostFactory getTestFactory() {
        return testFactory;
    }

    public Host getTestHost() {
        return testHost;
    }

//    public HostProvider getTestProvider() {
//        return testProvider;
//    }


    public void printRoot() {
        DevLog.print("Temple", "print root", testRoot);
    }

}
