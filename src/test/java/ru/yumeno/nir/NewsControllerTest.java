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
import ru.yumeno.nir.controller.NewsController;
import ru.yumeno.nir.dto.NewsRequestDTO;
import ru.yumeno.nir.entity.Tag;
import ru.yumeno.nir.exception_handler.exceptions.AdditionFailedException;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
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
class NewsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NewsController controller;

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
    @Sql(value = {"/sql/add-news-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-news-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllNewsTest() throws Exception {
        this.mockMvc.perform(get("/news"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].header", Matchers.equalTo("header2")));
    }

    @Test
    @Sql(value = {"/sql/add-news-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-news-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllNewsByTagsTest() throws Exception {
        String tags = "water";
        this.mockMvc.perform(get("/news/sort?tags=" + tags))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].header", Matchers.equalTo("header")));
    }

    @Test
    @Sql(value = {"/sql/add-news-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-news-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getNewsByIdTest() throws Exception {
        int id = 4;
        this.mockMvc.perform(get("/news/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header", Matchers.equalTo("header")))
                .andExpect(jsonPath("$.body", Matchers.equalTo("body")))
                .andExpect(jsonPath("$.tags[0].name", Matchers.equalTo("water")))
                .andExpect(jsonPath("$.imageUrl", Matchers.equalTo("url")));
    }

    @Test
    @Sql(value = {"/sql/add-news-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-news-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getNewsByNonExistentIdTest() throws Exception {
        int id = 100;
        this.mockMvc.perform(get("/news/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("News not exist with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql(value = {"/sql/add-news-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-news-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addValidNewsTest() throws Exception {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("water"));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/news")
                        .content(asJsonString(NewsRequestDTO.builder()
                                .header("заголовок")
                                .body("тело")
                                .imageUrl("url")
                                .tags(tags)
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header", Matchers.equalTo("заголовок")))
                .andExpect(jsonPath("$.body", Matchers.equalTo("тело")))
                .andExpect(jsonPath("$.tags[0].name", Matchers.equalTo("water")))
                .andExpect(jsonPath("$.imageUrl", Matchers.equalTo("url")));
    }

    @Test
    @Sql(value = {"/sql/add-news-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-news-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addInvalidNewsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/news")
                        .content(asJsonString(NewsRequestDTO.builder()
                                .header("h")
                                .body("body")
                                .imageUrl("url")
                                .tags(Collections.emptyList())
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    @Sql(value = {"/sql/add-news-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-news-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addNewsWithIdTest() throws Exception {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("water"));
        this.mockMvc.perform(MockMvcRequestBuilders.post("/news")
                        .content(asJsonString(NewsRequestDTO.builder()
                                .id(1)
                                .header("заголовок")
                                .body("тело")
                                .imageUrl("url")
                                .tags(tags)
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AdditionFailedException))
                .andExpect(result -> assertEquals("News with id cannot be added",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql(value = {"/sql/add-news-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-news-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateValidNewsTest() throws Exception {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("gas"));
        this.mockMvc.perform(MockMvcRequestBuilders.put("/news")
                        .content(asJsonString(NewsRequestDTO.builder()
                                .id(4)
                                .header("заголовок")
                                .body("тело")
                                .imageUrl("url")
                                .tags(tags)
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header", Matchers.equalTo("заголовок")))
                .andExpect(jsonPath("$.body", Matchers.equalTo("тело")))
                .andExpect(jsonPath("$.tags[0].name", Matchers.equalTo("gas")))
                .andExpect(jsonPath("$.imageUrl", Matchers.equalTo("url")));
    }

    @Test
    @Sql(value = {"/sql/add-news-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-news-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateInvalidNewsTest() throws Exception { // TODO not passed
        this.mockMvc.perform(MockMvcRequestBuilders.put("/news")
                        .content(asJsonString(NewsRequestDTO.builder()
                                .id(4)
                                .header("header")
                                .body("body")
                                .imageUrl("")
                                .tags(Collections.emptyList())
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    @Sql(value = {"/sql/add-news-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-news-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteNewsByIdTest() throws Exception {
        int id = 5;
        this.mockMvc.perform(delete("/news/" + id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/sql/add-news-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-news-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteNewsByNonExistentIdTest() throws Exception {
        int id = 100;
        this.mockMvc.perform(delete("/news/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("News not exist with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
