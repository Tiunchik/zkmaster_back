package org.zkmaster.backend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.zkmaster.backend.BackendApplication;
import org.zkmaster.backend.entity.ZKNodes;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BackendApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = AbstractTemplateControllersTest.class)
public class CRUDControllerTest {

    @Autowired
    private AbstractTemplateControllersTest template;
    @Autowired
    private MockMvc mockMvc;
    private static final String CRUD_CONTROLLER_URL = "/api/zkm/data/localhost:2181";


    @BeforeEach
    public void setUp() throws Exception {
        template.initNewTestTempleState();

        this.mockMvc.perform(get(CRUD_CONTROLLER_URL))
                .andExpect(status().isOk());
    }

    @Test
    public void getHostValue() throws Exception {
        var rsl = this.mockMvc.perform(get(CRUD_CONTROLLER_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String rslAsString = rsl.getResponse().getContentAsString();
        assertEquals(453, rslAsString.length());
    }

    @Test
    public void createNode() throws Exception {
        final String requestBodyJson
                = "{\n"
                + "  \"path\": \"/crud-test-node\",\n"
                + "  \"value\": \"value\"\n"
                + "}";
        this.mockMvc.perform(post(CRUD_CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(status().isOk())
                .andReturn();

        var createdNode = ZKNodes.getSubNode(template.getTestRoot(), "/crud-test-node");
        assertNotNull(createdNode);
    }

    @Test
    public void updateNodeSetValue() throws Exception {
        final String requestBodyJson
                = "{\n"
                + "  \"path\": \"/1/2-1\",\n"
                + "  \"value\": \"2-1&value-update\"\n"
                + "}";
        this.mockMvc.perform(put(CRUD_CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andExpect(status().isOk())
                .andReturn();

        var updatedNode = ZKNodes.getSubNode(template.getTestRoot(), "/1/2-1");
        assertNotNull(updatedNode);
        assertEquals("value-update", updatedNode.getValue());
    }

    @Test
    public void updateNodeRename() throws Exception {
        final String requestBodyJson
                = "{\n"
                + "  \"path\": \"/1/2-2\",\n"
                + "  \"value\": \"crud-test-node-update&v\"\n"
                + "}";
        this.mockMvc.perform(put(CRUD_CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var renamedNode = ZKNodes.getSubNode(template.getTestRoot(), "/1/crud-test-node-update");
        assertNotNull(renamedNode);
        assertEquals("crud-test-node-update", renamedNode.getName());
        assertEquals("v", renamedNode.getValue());
    }

    @Test
    public void updateNodeRenameAndSetValue() throws Exception {
        final String requestBodyJson
                = "{\n"
                + "  \"path\": \"/1/2-2\",\n"
                + "  \"value\": \"crud-test-node-update&value-update\"\n"
                + "}";
        this.mockMvc.perform(put(CRUD_CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var updatedNode = ZKNodes.getSubNode(template.getTestRoot(), "/1/crud-test-node-update");
        assertNotNull(updatedNode);
        assertEquals("crud-test-node-update", updatedNode.getName());
        assertEquals("value-update", updatedNode.getValue());
    }

    @Test
    public void updateNodeCascadeRenameAndSetValue() throws Exception {
        final String requestBodyJson
                = "{\n"
                + "  \"path\": \"/1/2-1\",\n"
                + "  \"value\": \"crud-test-node-update&value-update\"\n"
                + "}";
        this.mockMvc.perform(put(CRUD_CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        var updatedNode = ZKNodes.getSubNode(template.getTestRoot(), "/1/crud-test-node-update");

        template.printRoot();
        assertNotNull(updatedNode);
        assertEquals("crud-test-node-update", updatedNode.getName());
        assertEquals("value-update", updatedNode.getValue());
        assertNotNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/crud-test-node-update/3-1"));
        assertNotNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/crud-test-node-update/3-1/4"));
        assertNotNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/crud-test-node-update/3-2"));
        assertNotNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/crud-test-node-update/3-2/4"));
        assertNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2"));
    }

    @Test
    public void deleteNode() throws Exception {
        assertNotNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-2"));

        this.mockMvc.perform(delete(CRUD_CONTROLLER_URL + "/1/2-2"))
                .andExpect(status().isOk())
                .andReturn();

        assertNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-2"));
    }

    @Test
    public void deleteNodeCascade() throws Exception {
        assertNotNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-1"));

        this.mockMvc.perform(delete(CRUD_CONTROLLER_URL + "/1/2-1"))
                .andExpect(status().isOk())
                .andReturn();

        assertNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-1"));
        assertNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-1/3-1"));
        assertNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-1/3-1/4"));
        assertNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-1/3-2"));
        assertNull(ZKNodes.getSubNode(template.getTestRoot(), "/1/2-1/3-2/4"));
    }

}