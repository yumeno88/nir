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
import ru.yumeno.nir.controller.DistrictController;
import ru.yumeno.nir.dto.DistrictDTO;
import ru.yumeno.nir.dto.StreetDTO;
import ru.yumeno.nir.exception_handler.exceptions.DeletionFailedException;
import ru.yumeno.nir.exception_handler.exceptions.ResourceAlreadyExistException;
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
class DistrictControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DistrictController controller;

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
    @Sql(value = {"/sql/add-districts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-districts-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllDistrictsTest() throws Exception {
        this.mockMvc.perform(get("/districts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @Sql(value = {"/sql/add-districts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-districts-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDistrictByIdTest() throws Exception {
        int id = 4;
        this.mockMvc.perform(get("/districts/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.equalTo("district1")));
    }

    @Test
    @Sql(value = {"/sql/add-districts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-districts-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDistrictByNonExistentIdTest() throws Exception {
        int id = 100;
        this.mockMvc.perform(get("/districts/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("District not exist with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql(value = {"/sql/add-districts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-districts-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addValidDistrictTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/districts")
                        .content(asJsonString(DistrictDTO.builder().name("district3").build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.equalTo("district3")));
    }

    @Test
    @Sql(value = {"/sql/add-districts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-districts-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addInvalidDistrictTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/districts")
                        .content(asJsonString(DistrictDTO.builder().name("").build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    @Sql(value = {"/sql/add-districts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-districts-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addAlreadyExistDistrictTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/districts")
                        .content(asJsonString(DistrictDTO.builder().name("district1").build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResourceAlreadyExistException));
    }

    @Test
    @Sql(value = {"/sql/add-districts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-districts-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateValidDistrictTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/districts")
                        .content(asJsonString(StreetDTO.builder()
                                .id(4)
                                .name("district")
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.equalTo("district")));
    }

    @Test
    @Sql(value = {"/sql/add-districts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-districts-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateInvalidDistrictTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/districts")
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
    @Sql(value = {"/sql/add-districts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-districts-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteDistrictByIdTest() throws Exception {
        int id = 5;
        this.mockMvc.perform(delete("/districts/" + id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/sql/add-districts-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-districts-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteDistrictByNonExistentIdTest() throws Exception {
        int id = 100;
        this.mockMvc.perform(delete("/districts/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("District not exist with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql(value = {"/sql/add-streets-and-districts-with-ref-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-streets-and-districts-with-ref-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteHasRefDistrictByIdTest() throws Exception {
        int id = 4;
        this.mockMvc.perform(delete("/districts/" + id))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DeletionFailedException))
                .andExpect(result -> assertEquals("District cannot be deleted with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}

