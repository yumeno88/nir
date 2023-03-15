package ru.yumeno.nir;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yumeno.nir.controller.NewsController;
import ru.yumeno.nir.dto.NewsRequestDTO;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// @TestPropertySource("/application-test.properties")
class NewsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NewsController controller;
//    @MockBean
//    private NewsService newsService;

    @Test
    public void controllerIsNotNullTest() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void getAllNewsTest() throws Exception {
        this.mockMvc.perform(get("/news"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].header", Matchers.equalTo("header2")));
    }

    @Test
    public void getNewsByIdTest() throws Exception {
        int id = 4;
        this.mockMvc.perform(get("/news/" + id))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.header", Matchers.equalTo("header")));
    }

    @Test
    public void addValidNewsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/news")
                        .content(asJsonString(NewsRequestDTO.builder()
                                .header("header")
                                .body("body")
                                .imageUrl("url")
                                .tags(Collections.emptyList())
                                .build())
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header", Matchers.equalTo("header")));
    }

    @Test
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
                .andExpect(status().isBadRequest());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
