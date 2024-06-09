package source.restaurant_web_project.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.builders.ItemBuilder.ItemBuilder;
import source.restaurant_web_project.models.entity.Category;
import source.restaurant_web_project.models.entity.Item;
import source.restaurant_web_project.repositories.CategoryRepository;
import source.restaurant_web_project.repositories.ItemRepository;

import javax.sql.rowset.serial.SerialBlob;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Category category;
    private Item item;

    private ItemBuilder itemBuilder;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws SQLException {
        itemBuilder = new ItemBuilder(new ModelMapper());
        Category tempCategory = new Category();
        tempCategory.setPosition(111111111);
        category = categoryRepository.saveAndFlush(tempCategory);

        Item tempItem = new Item();
        tempItem.setName("{\"en_EN\":\"Burger\",\"bg_BG\":\"Бургер\"}");
        tempItem.setPosition(111111111);
        tempItem.setCategory(category);
        tempItem.setPrice(BigDecimal.valueOf(2));

        item = itemRepository.saveAndFlush(tempItem);

    }

    @AfterEach
    void tearDown() {
        if(category!=null) {
            categoryRepository.delete(categoryRepository.findById(category.getId()).get());
        }
    }

    @Test
    void getAllItemsByCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/items/category/111111111"))
                .andExpect(status().isOk());
    }

    @Test
    void findItemByCategoryPositionAndItemPosition() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/items/category/111111111/item/111111111"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "OWNER")
    @Test
    void addItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/items/add")
                        .file("image", new byte[2])
                        .param("name", "testName123")
                        .param("description", "descriptionn")
                        .param("price", "1.20")
                        .param("categoryPosition","" + category.getPosition()))
                .andExpect(status().isCreated());
    }

    @WithMockUser(roles = "OWNER")
    @Test
    void deleteItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/items/delete/category/"+category.getPosition()+"/item/"+item.getPosition()))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "OWNER")
    @Test
    void editItem() throws Exception {
        long position = categoryRepository.findCategoryByPosition(category.getPosition()).getItems().size()+1;

        Item item1 = itemBuilder.buildItem()
                .withDescription("{\"en_EN\":\"name2ewu2i1h4eu1ihqewuiahdsjui2wieqhiejwqr2wr2name2ewu2i1h4eu1ihqewuiahdsjui2wieqhiejwqr2wr2\"}")
                .withName("{\"en_EN\":\"Desc\"}")
                .withCategory(category)
                .withPosition(position)
                .build();

        itemRepository.saveAndFlush(item1);

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/items/edit/category/"+category.getPosition()+"/item/"+item.getPosition())
                        .file("image",new byte[2])
                        .param("position","" + item1.getPosition())
                        .param("name","name")
                        .param("description", "desc")
                        .param("price","1")
                        .with(request -> {
                            request.setMethod(HttpMethod.PUT.toString());
                            return request;
                        }))
                .andExpect(status().isOk());
    }
}