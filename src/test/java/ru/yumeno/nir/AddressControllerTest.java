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
import ru.yumeno.nir.controller.AddressController;
import ru.yumeno.nir.dto.AddressDTO;
import ru.yumeno.nir.entity.District;
import ru.yumeno.nir.entity.Street;
import ru.yumeno.nir.exception_handler.exceptions.AdditionFailedException;
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
class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AddressController controller;

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
    @Sql(value = {"/sql/add-addresses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllAddressesTest() throws Exception {
        this.mockMvc.perform(get("/addresses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @Sql(value = {"/sql/add-addresses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAddressByIdTest() throws Exception {
        int id = 4;
        this.mockMvc.perform(get("/addresses/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apartment", Matchers.equalTo("apart1")))
                .andExpect(jsonPath("$.house", Matchers.equalTo("house1")))
                .andExpect(jsonPath("$.porch", Matchers.equalTo("porch1")))
                .andExpect(jsonPath("$.district.name", Matchers.equalTo("district1")))
                .andExpect(jsonPath("$.street.name", Matchers.equalTo("street2")));
    }

    @Test
    @Sql(value = {"/sql/add-addresses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAddressByNonExistentIdTest() throws Exception {
        int id = 100;
        this.mockMvc.perform(get("/addresses/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Address not exist with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql(value = {"/sql/add-addresses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addValidAddressTest() throws Exception {
        Street street = new Street(4, "street");
        District district = new District(4, "district");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                        .content(asJsonString(AddressDTO.builder()
                                .apartment("apart")
                                .house("house")
                                .porch("porch")
                                .street(street)
                                .district(district)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apartment", Matchers.equalTo("apart")))
                .andExpect(jsonPath("$.house", Matchers.equalTo("house")))
                .andExpect(jsonPath("$.porch", Matchers.equalTo("porch")))
                .andExpect(jsonPath("$.street.name", Matchers.equalTo("street")))
                .andExpect(jsonPath("$.district.name", Matchers.equalTo("district")));
    }

    @Test
    @Sql(value = {"/sql/add-addresses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addInvalidAddressTest() throws Exception {
        Street street = new Street(4, "street");
        District district = new District(4, "district");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                        .content(asJsonString(AddressDTO.builder()
                                .apartment("")
                                .house("house")
                                .porch("porch")
                                .street(street)
                                .district(district)
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    @Sql(value = {"/sql/add-addresses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addAlreadyExistAddressTest() throws Exception {
        Street street = new Street(5, "street");
        District district = new District(4, "district");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                        .content(asJsonString(AddressDTO.builder()
                                .apartment("apart1")
                                .house("house1")
                                .porch("porch1")
                                .street(street)
                                .district(district)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof ResourceAlreadyExistException));
    }

    @Test
    @Sql(value = {"/sql/add-addresses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addAddressWithIdTest() throws Exception {
        Street street = new Street(5, "street");
        District district = new District(4, "district");
        this.mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                        .content(asJsonString(AddressDTO.builder()
                                .id(1)
                                .apartment("apart1")
                                .house("house1")
                                .porch("porch1")
                                .street(street)
                                .district(district)
                                .build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AdditionFailedException))
                .andExpect(result -> assertEquals("Address with id cannot be added",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql(value = {"/sql/add-addresses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateValidAddressTest() throws Exception {
        Street street = new Street(4, "street1");
        District district = new District(4, "district1");
        this.mockMvc.perform(MockMvcRequestBuilders.put("/addresses")
                        .content(asJsonString(AddressDTO.builder()
                                .id(4)
                                .apartment("apart")
                                .house("house")
                                .porch("porch")
                                .street(street)
                                .district(district)
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apartment", Matchers.equalTo("apart")))
                .andExpect(jsonPath("$.house", Matchers.equalTo("house")))
                .andExpect(jsonPath("$.porch", Matchers.equalTo("porch")))
                .andExpect(jsonPath("$.street.name", Matchers.equalTo("street1")))
                .andExpect(jsonPath("$.district.name", Matchers.equalTo("district1")));
    }

    @Test
    @Sql(value = {"/sql/add-addresses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateInvalidAddressTest() throws Exception {
        Street street = new Street(4, "street1");
        District district = new District(4, "district1");
        this.mockMvc.perform(MockMvcRequestBuilders.put("/addresses")
                        .content(asJsonString(AddressDTO.builder()
                                .id(4)
                                .apartment("")
                                .house("house")
                                .porch("porch")
                                .street(street)
                                .district(district)
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    @Sql(value = {"/sql/add-addresses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteAddressByIdTest() throws Exception {
        int id = 5;
        this.mockMvc.perform(delete("/addresses/" + id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/sql/add-addresses-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteAddressByNonExistentIdTest() throws Exception {
        int id = 100;
        this.mockMvc.perform(delete("/addresses/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(result -> assertEquals("Address not exist with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @Sql(value = {"/sql/add-addresses-with-ref-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/add-addresses-with-ref-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteHasRefAddressByIdTest() throws Exception {
        int id = 4;
        this.mockMvc.perform(delete("/addresses/" + id))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof DeletionFailedException))
                .andExpect(result -> assertEquals("Address cannot be deleted with id : " + id,
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}

