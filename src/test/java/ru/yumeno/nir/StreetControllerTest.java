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
import ru.yumeno.nir.controller.StreetController;
import ru.yumeno.nir.dto.StreetDTO;
import ru.yumeno.nir.exception_handler.exceptions.DeletionFailedException;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;

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
class StreetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StreetController controller;

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
    @Sql(value = {"/sql/add-streets-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-streets-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllStreetsTest() throws Exception {
        this.mockMvc.perform(get("/streets"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @Sql(value = {"/sql/add-streets-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-streets-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getStreetByIdTest() throws Exception {
        int id = 4;
        this.mockMvc.perform(get("/streets/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.equalTo("street1")));
    }

    @Test
    @Sql(value = {"/sql/add-streets-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-streets-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getStreetByNonExistentIdTest() throws Exception {
        int id = 100;
        this.mockMvc.perform(get("/streets/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Street not exist with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql(value = {"/sql/add-streets-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-streets-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addValidStreetTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/streets")
                        .content(asJsonString(StreetDTO.builder().name("street3").build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.equalTo("street3")));
    }

    @Test
    @Sql(value = {"/sql/add-streets-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-streets-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addInvalidStreetTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/streets")
                        .content(asJsonString(StreetDTO.builder().name("").build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    @Sql(value = {"/sql/add-streets-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-streets-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateValidStreetTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/streets")
                        .content(asJsonString(StreetDTO.builder()
                                .id(4)
                                .name("street")
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.equalTo("street")));
    }

    @Test
    @Sql(value = {"/sql/add-streets-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-streets-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateInvalidStreetTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/streets")
                        .content(asJsonString(StreetDTO.builder()
                                .id(4)
                                .name("")
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    @Sql(value = {"/sql/add-streets-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-streets-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteStreetByIdTest() throws Exception {
        int id = 5;
        this.mockMvc.perform(get("/streets/" + id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/sql/add-streets-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-streets-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteStreetByNonExistentIdTest() throws Exception {
        int id = 100;
        this.mockMvc.perform(delete("/streets/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Street not exist with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql(value = {"/sql/add-streets-and-districts-with-ref-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-streets-and-districts-with-ref-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteHasRefStreetByIdTest() throws Exception {
        int id = 4;
        this.mockMvc.perform(delete("/streets/" + id))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DeletionFailedException))
                .andExpect(result -> assertEquals("Street cannot be deleted with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}
