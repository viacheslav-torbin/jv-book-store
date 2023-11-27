package org.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bookstore.dto.category.CategoryDto;
import org.bookstore.dto.category.CreateCategoryRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {
        "classpath:/scripts/categories/delete-categories.sql",
        "classpath:/scripts/categories/create-categories.sql"
})
@Slf4j
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static ObjectMapper objectMapper;

    @BeforeAll
    @SneakyThrows
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Create category with valid dto")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    @Sql(scripts = "classpath:/scripts/categories/delete-categories.sql")
    void createCategory_validDto_CategoryDto() throws Exception {
        CreateCategoryRequestDto request = new CreateCategoryRequestDto(
                "horror",
                "some scary text"
        );

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );
        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", request.name())
                .hasFieldOrPropertyWithValue("description", request.description());
    }

    @Test
    @DisplayName("Getting all categories")
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void getAll_existingCategories_ListCategoryDto() throws Exception {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MvcResult result = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> actual = Arrays.stream(objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        CategoryDto[].class))
                .toList();
        assertThat(actual).hasSize(3);
        assertThat(actual)
                .element(0)
                .hasFieldOrPropertyWithValue("name", "Horror")
                .hasFieldOrPropertyWithValue("description", "Horror_desc");
    }

    @Test
    @DisplayName("Getting category by id")
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"})
    void getCategoryById_existingCategories_CategoryDto() throws Exception {
        long id = 1;
        MvcResult result = mockMvc.perform(get("/categories/" + id))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );
        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "Horror")
                .hasFieldOrPropertyWithValue("description", "Horror_desc");
    }

    @Test
    @DisplayName("Update category with valid dto")
    @WithMockUser(username = "admin", authorities = {"USER", "ADMIN"})
    void updateCategory_validDto_CategoryDto() throws Exception {
        long id = 1;
        CreateCategoryRequestDto updateRequest = new CreateCategoryRequestDto(
                "horror",
                "some new scary text"
        );
        MvcResult result = mockMvc.perform(put("/categories/" + id)
                        .content(objectMapper.writeValueAsBytes(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );
        assertThat(actual)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", updateRequest.name())
                .hasFieldOrPropertyWithValue("description", updateRequest.description());
    }
}
