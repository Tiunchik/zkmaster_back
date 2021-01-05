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
import org.zkmaster.backend.entity.utils.ZKNodes;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BackendApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = ControllersTestEnvironment.class)
class TransformControllerTest {
    @Autowired
    private ControllersTestEnvironment template;
    @Autowired
    private MockMvc mockMvc;
    private static final String CRUD_CONTROLLER_URL = "/api/zkm/data/localhost:2181";
    private static final String TRANSFORM_CONTROLLER_URL = "/api/zkm/transform/localhost:2181";


    @BeforeEach
    public void setUp() throws Exception {
        template.initNewTestTempleState();

        this.mockMvc.perform(get(CRUD_CONTROLLER_URL))
                .andExpect(status().isOk());
    }

    @AfterEach
    void tearDown() {
        template.clearEnvironmentContext();
    }

    @Test
    void export() throws Exception {
        final String requestBodyJson
                = "{\n"
                + "  \"type\": \"TXT\"\n"
                + "}";
        var rsl = this.mockMvc.perform(get(TRANSFORM_CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(status().isOk())
                .andReturn();

        var temp = rsl.getResponse().getContentAsString();
        assertTrue(temp.contains("/ : v"));
        assertTrue(temp.contains("/1 : v"));
        assertTrue(temp.contains("/1/2-1 : v"));
        assertTrue(temp.contains("/1/2-1/3-1 : v"));
        assertTrue(temp.contains("/1/2-1/3-1/4 : v"));
        assertTrue(temp.contains("/1/2-1/3-2 : v"));
        assertTrue(temp.contains("/1/2-1/3-2/4 : v"));
        assertTrue(temp.contains("/1/2-2 : v"));
    }

    @Test
    void importData() throws Exception {
        final String requestBodyJson
                = "{\n"
                + "  \"type\": \"TXT\",\n"
                + "  \"content\": [\n"
                + "    \"/1/2-2 : value\",\n"
                + "    \"/1/2-2/i1 : value\",\n"
                + "    \"/1/2-2/i1/i2 : value\",\n"
                + "    \"/1/2-2/i1/i2/i3 : value\",\n"
                + "    \"/1/2-2/i1/i2/i3/i4 : value\"\n"
                + "  ]\n"
                + "}\n";
        this.mockMvc.perform(post(TRANSFORM_CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(status().isOk());

        template.printRoot();

        assertNotNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-2/i1"));
        assertNotNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-2/i1/i2"));
        assertNotNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-2/i1/i2/i3"));
        assertNotNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-2/i1/i2/i3/i4"));
    }

}