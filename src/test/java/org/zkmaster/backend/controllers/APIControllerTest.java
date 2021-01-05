package org.zkmaster.backend.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.zkmaster.backend.BackendApplication;
import org.zkmaster.backend.devutil.DevLog;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.utils.ZKNodes;
import org.zkmaster.backend.mocks.HostFake;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BackendApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = ControllersTestEnvironment.class)
class APIControllerTest {
    @Autowired
    private ControllersTestEnvironment template;
    @Autowired
    private MockMvc mockMvc;
    private static final String CRUD_CONTROLLER_URL = "/api/zkm/data";
    private static final String API_CONTROLLER_URL = "/api/zkm";


    @BeforeEach
    public void setUp() throws Exception {
        template.initNewTestTempleState();

        this.mockMvc.perform(get(CRUD_CONTROLLER_URL + "/localhost:2181"))
                .andExpect(status().isOk());
    }

    @AfterEach
    void tearDown() {
        template.clearEnvironmentContext();
    }

    @Test
    void checkHostsHealth() throws Exception {
        // prepare - connect some servers for real multi check
        template.factorySetHostFake(new HostFake("localhost:2182", new ZKNode("/", "")));
        this.mockMvc.perform(get(CRUD_CONTROLLER_URL + "/localhost:2182")).andExpect(status().isOk());
        template.factorySetHostFake(new HostFake("localhost:2182", new ZKNode("/", "")));
        this.mockMvc.perform(get(CRUD_CONTROLLER_URL + "/localhost:2182")).andExpect(status().isOk());
        template.factorySetHostFake(new HostFake("localhost:2183", new ZKNode("/", "")));
        this.mockMvc.perform(get(CRUD_CONTROLLER_URL + "/localhost:2183")).andExpect(status().isOk());
        // test
        var requestBodyJson
                = "[\n"
                + "  \"localhost:2181\", \"localhost:2182\", \"localhost:2183\", \"localhost:2184\"\n"
                + "]";
        var expectedResponseJson
                = "{\n"
                + "  \"localhost:2181\": true,\n"
                + "  \"localhost:2182\": true,\n"
                + "  \"localhost:2183\": true,\n"
                + "  \"localhost:2184\": false\n"
                + "}\n";
        this.mockMvc.perform(get(API_CONTROLLER_URL + "/conn/check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseJson))
                .andReturn();
    }

    @Test
    void injectFromToSaveValues() throws Exception {
        // prepare
        ZKNode secondFakeServerRoot =
                new ZKNode("/", "old val - not upd", List.of(
                        new ZKNode("/1", "old val - not upd", List.of(
                                new ZKNode("/1/2-1", "old val - not upd"),
                                new ZKNode("/1/2-2", "old val - not upd")
                        ))
                ));
        template.factorySetHostFake(new HostFake("localhost:2182", secondFakeServerRoot));
        this.mockMvc.perform(get(CRUD_CONTROLLER_URL + "/localhost:2182")).andExpect(status().isOk());
        //test
        var requestBodyJson
                = "{\n"
                + "  \"sourceHost\": \"localhost:2181\",\n"
                + "  \"sourceNodePath\": \"/1/2-1\",\n"
                + "  \"targetHost\": \"localhost:2182\",\n"
                + "  \"targetNodePath\": \"/1\",\n"
                + "  \"updOldValues\": false\n"
                + "}";

        this.mockMvc.perform(post(API_CONTROLLER_URL + "/data/injection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        DevLog.print("TEST", "print secondFakeServerRoot", secondFakeServerRoot);

        assertNodeChanged(secondFakeServerRoot, "/1/2-1", "old val - not upd");
        assertNodeChanged(secondFakeServerRoot, "/1/2-1/3-1", "v");
        assertNodeChanged(secondFakeServerRoot, "/1/2-1/3-1/4", "v");
        assertNodeChanged(secondFakeServerRoot, "/1/2-1/3-2", "v");
        assertNodeChanged(secondFakeServerRoot, "/1/2-1/3-2/4", "v");
    }

    @Test
    void injectFromToUpdateValues() throws Exception {
        // prepare
        ZKNode secondFakeServerRoot =
                new ZKNode("/", "v", List.of(
                        new ZKNode("/1", "old val", List.of(
                                new ZKNode("/1/2-1", "old val", List.of(
                                        new ZKNode("/1/2-1/3-1", "old val", List.of(
                                                new ZKNode("/1/2-1/3-1/4", "old val", List.of())
                                        )),
                                        new ZKNode("/1/2-1/3-2", "old val", List.of(
                                                new ZKNode("/1/2-1/3-2/4", "old val", List.of())
                                        )),
                                        new ZKNode("/1/2-1/a", "value a", List.of(
                                                new ZKNode("/1/2-1/a/b", "value b", List.of())
                                        ))
                                )),
                                new ZKNode("/1/2-2", "old val")
                        ))
                ));
        template.factorySetHostFake(new HostFake("localhost:2182", secondFakeServerRoot));
        this.mockMvc.perform(get(CRUD_CONTROLLER_URL + "/localhost:2182")).andExpect(status().isOk());
        //test
        var requestBodyJson
                = "{\n"
                + "  \"sourceHost\": \"localhost:2181\",\n"
                + "  \"sourceNodePath\": \"/1/2-1\",\n"
                + "  \"targetHost\": \"localhost:2182\",\n"
                + "  \"targetNodePath\": \"/1\",\n"
                + "  \"updOldValues\": true\n"
                + "}";

        this.mockMvc.perform(post(API_CONTROLLER_URL + "/data/injection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        assertNodeChanged(secondFakeServerRoot, "/1/2-1", "v");
        assertNodeChanged(secondFakeServerRoot, "/1/2-1/3-1", "v");
        assertNodeChanged(secondFakeServerRoot, "/1/2-1/3-1/4", "v");
        assertNodeChanged(secondFakeServerRoot, "/1/2-1/3-2", "v");
        assertNodeChanged(secondFakeServerRoot, "/1/2-1/3-2/4", "v");
    }

    private void assertNodeChanged(ZKNode root, String path, String newValue) {
        ZKNode testNode = ZKNodes.getSubNode(root, path);
        assertNotNull(testNode);
        assertEquals(newValue, testNode.getValue());
    }

}