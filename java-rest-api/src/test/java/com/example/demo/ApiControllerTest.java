package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.controller.ApiController;
import com.example.demo.model.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ApiController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception {
       
        Item item = new Item(1L, "Item 1", "Description 1");
        mockMvc.perform(post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item)));
    }

    @Test
    public void testGetItemJson() throws Exception {
        mockMvc.perform(get("/api/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"name\":\"Item 1\",\"description\":\"Description 1\"}"));
    }

    @Test
    public void testGetItemXml() throws Exception {
        mockMvc.perform(get("/api/items/1")
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML))
                .andExpect(content().xml("<Item><id>1</id><name>Item 1</name><description>Description 1</description></Item>"));
    }

    @Test
    public void testCreateItem() throws Exception {
        Item newItem = new Item(null, "Item 2", "Description 2");
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateItemWithEmptyNameShouldReturnBadRequest() throws Exception {
        Item invalidItem = new Item(null, "", "Description 2");
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidItem)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome do item é obrigatório e não pode ser vazio."));
    }

    @Test
    public void testCreateItemWithNullDescriptionShouldReturnBadRequest() throws Exception {
        Item invalidItem = new Item(null, "Item 2", null);
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidItem)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Descrição do item é obrigatória e não pode ser vazia."));
    }

    @Test
    public void testUpdateItemSuccess() throws Exception {
        Item updatedItem = new Item(null, "Updated Item 1", "Updated Description 1");
        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Updated Item 1\",\"description\":\"Updated Description 1\"}"));
    }

    @Test
    public void testUpdateItemNotFound() throws Exception {
        Item updatedItem = new Item(null, "Updated Name", "Updated Description");
        mockMvc.perform(put("/api/items/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPatchItemSuccess() throws Exception {
        Item patchItem = new Item(null, "Patched Item 1", null);
        mockMvc.perform(patch("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchItem)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Patched Item 1\",\"description\":\"Description 1\"}"));
    }

    @Test
    public void testPatchItemWithEmptyNameShouldReturnBadRequest() throws Exception {
        Item patchItem = new Item(null, "", null);
        mockMvc.perform(patch("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchItem)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome do item não pode ser vazio."));
    }

    @Test
    public void testPatchItemNotFound() throws Exception {
        Item patchItem = new Item(null, "Patched Name", null);
        mockMvc.perform(patch("/api/items/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchItem)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteItemSuccess() throws Exception {
        mockMvc.perform(delete("/api/items/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/items/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteItemNotFound() throws Exception {
        mockMvc.perform(delete("/api/items/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSearchItemsByName() throws Exception {
        Item specialItem = new Item(null, "Special Name", "Special Description");
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(specialItem)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/items/search")
                        .param("name", "Special")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"Special Name\",\"description\":\"Special Description\"}]"));
    }

    @Test
    public void testSearchItemsWithEmptyNameShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/items/search")
                        .param("name", "")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Parâmetro de busca 'name' é obrigatório e não pode ser vazio."));
    }

    @Test
    public void testGetItemCount() throws Exception {
        mockMvc.perform(get("/api/items/count")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        Item anotherItem = new Item(null, "Item 2", "Description 2");
        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(anotherItem)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/items/count")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    public void testCreateItemsBulkSuccess() throws Exception {
        Item item2 = new Item(null, "Item 2", "Description 2");
        Item item3 = new Item(null, "Item 3", "Description 3");
        mockMvc.perform(post("/api/items/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Item[]{item2, item3})))
                .andExpect(status().isCreated())
                .andExpect(content().json("[{\"id\":2,\"name\":\"Item 2\",\"description\":\"Description 2\"},{\"id\":3,\"name\":\"Item 3\",\"description\":\"Description 3\"}]"));
    }

    @Test
    public void testCreateItemsBulkWithInvalidItemShouldReturnBadRequest() throws Exception {
        Item item2 = new Item(null, "Item 2", "Description 2");
        Item invalidItem = new Item(null, "", "Description 3");
        mockMvc.perform(post("/api/items/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Item[]{item2, invalidItem})))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Nome do item é obrigatório e não pode ser vazio."));
    }

    @Test
    public void testUpdateItemWithEmptyDescriptionShouldReturnBadRequest() throws Exception {
        Item invalidItem = new Item(null, "Updated Item 1", "");
        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidItem)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Descrição do item é obrigatória e não pode ser vazia."));
    }

    @Test
    public void testPatchItemWithNoFieldsShouldReturnBadRequest() throws Exception {
        Item patchItem = new Item();
        mockMvc.perform(patch("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchItem)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Pelo menos um campo deve ser informado para atualização parcial."));
    }

    @Test
    public void testGetItemNotFound() throws Exception {
        mockMvc.perform(get("/api/items/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}