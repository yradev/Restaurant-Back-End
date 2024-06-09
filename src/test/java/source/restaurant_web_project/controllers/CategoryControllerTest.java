package source.restaurant_web_project.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.builders.CategoryBuilder.CategoryBuilder;
import source.restaurant_web_project.models.dto.category.CategoryAddDto;
import source.restaurant_web_project.models.entity.Category;
import source.restaurant_web_project.repositories.CategoryRepository;

import javax.sql.rowset.serial.SerialBlob;

import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    private CategoryBuilder categoryBuilder;

    @BeforeEach
    void setUp() throws SQLException {
        categoryBuilder = new CategoryBuilder(new ModelMapper());
        Category tempCategory = new Category();
        tempCategory.setPosition(999999999);

        tempCategory.setName("{\"en_EN\":\"1l2k4k5m3n2\"}");

        category = categoryRepository.saveAndFlush(tempCategory);
    }

    @AfterEach
    void tearDown() {
        if (category != null) {
            Category tempCategory = categoryRepository.findCategoryByPosition(category.getPosition());
            categoryRepository.delete(tempCategory);
        }
    }


    @Test
    void categories() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/categories"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "OWNER")
    @Test
    void categoryAdd() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/categories/add")
                        .file("image", new byte[2])
                        .param("name", "1l2k4k52m3n2")
                        .param("description", "desc"))
                .andExpect(status().isCreated());

        categoryRepository.delete(categoryRepository.findCategoryByName("{\"en_EN\":\"1l2k4k52m3n2\"}"));
    }

    @WithMockUser(roles = "OWNER")
    @Test
    void categoryEdit() throws Exception {
        Category category1 = categoryBuilder.buildCategory()
                .withDescription("{\"en_EN\":\"name\"}")
                .withName("{\"en_EN\":\"Desc\"}")
                .withPosition(categoryRepository.count())
                .build();

        categoryRepository.saveAndFlush(category1);

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/categories/edit/" + category.getPosition())
                .file("image", new byte[2])
                .param("name", "testCatgegory123testtestCategory123test")
                .param("description", "desc")
                .param("position", "" + category1.getPosition())
                .with(request -> {
                    request.setMethod(HttpMethod.PUT.name());
                    return request;
                }))
                .andExpect(status().isOk());

        categoryRepository.delete(categoryRepository.findCategoryByPosition(category1.getPosition()));
    }

    @WithMockUser(roles = "OWNER")
    @Test
    void categoryDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/categories/delete/" + category.getPosition()))
                .andExpect(status().isOk());

        category = null;
    }
}