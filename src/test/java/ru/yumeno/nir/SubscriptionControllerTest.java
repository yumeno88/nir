package ru.yumeno.nir;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yumeno.nir.controller.SubscriptionController;
import ru.yumeno.nir.dto.SubscriptionDTO;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class SubscriptionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SubscriptionController controller;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void controllerIsNotNullTest() {
        assertThat(controller).isNotNull();
    }

    @Test
    @Sql(value = {"/sql/add-subscriptions-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-subscriptions-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllSubscriptionsTest() throws Exception {
        this.mockMvc.perform(get("/subscriptions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @Sql(value = {"/sql/add-subscriptions-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-subscriptions-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getSubscriptionByIdTest() throws Exception {
        int id = 4;
        this.mockMvc.perform(get("/subscriptions/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId", Matchers.equalTo("chatId1")))
                .andExpect(jsonPath("$.tags[0].name", Matchers.equalTo("gas")));
    }

    @Test
    @Sql(value = {"/sql/add-subscriptions-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-subscriptions-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getSubscriptionByNonExistentIdTest() throws Exception {
        int id = 100;
        this.mockMvc.perform(get("/subscriptions/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Subscription not exist with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql(value = {"/sql/add-subscriptions-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-subscriptions-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addValidSubscriptionTest() throws Exception {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("water"));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/subscriptions")
                        .content(asJsonString(SubscriptionDTO.builder()
                                .chatId("chatId1")
                                .tags(tags)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId", Matchers.equalTo("chatId1")))
                .andExpect(jsonPath("$.tags[0].name", Matchers.equalTo("water")));
    }

    @Test
    @Sql(value = {"/sql/add-subscriptions-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-subscriptions-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addInvalidSubscriptionTest() throws Exception {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("gas"));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/subscriptions")
                        .content(asJsonString(SubscriptionDTO.builder()
                                .chatId("")
                                .tags(tags)
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    @Sql(value = {"/sql/add-subscriptions-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-subscriptions-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateValidSubscriptionTest() throws Exception {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("water"));
        this.mockMvc.perform(MockMvcRequestBuilders.put("/subscriptions")
                        .content(asJsonString(SubscriptionDTO.builder()
                                .id(4)
                                .chatId("chatId")
                                .tags(tags)
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId", Matchers.equalTo("chatId")))
                .andExpect(jsonPath("$.tags[0].name", Matchers.equalTo("water")));
    }

    @Test
    @Sql(value = {"/sql/add-subscriptions-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-subscriptions-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateInvalidSubscriptionTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/subscriptions")
                        .content(asJsonString(SubscriptionDTO.builder()
                                .id(4)
                                .chatId("chatId")
                                .tags(null)
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    @Sql(value = {"/sql/add-subscriptions-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-subscriptions-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteSubscriptionByIdTest() throws Exception {
        int id = 5;
        this.mockMvc.perform(delete("/subscriptions/" + id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/sql/add-subscriptions-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-subscriptions-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteSubscriptionByNonExistentIdTest() throws Exception {
        int id = 100;
        this.mockMvc.perform(delete("/subscriptions/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Subscription not exist with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}


