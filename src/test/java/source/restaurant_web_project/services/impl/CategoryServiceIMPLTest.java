package source.restaurant_web_project.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.builders.CategoryBuilder.CategoryBuilder;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.ConflictException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.models.dto.category.CategoryAddDto;
import source.restaurant_web_project.models.dto.category.CategoryEditDTO;
import source.restaurant_web_project.models.entity.Category;
import source.restaurant_web_project.repositories.CategoryRepository;
import source.restaurant_web_project.util.AmazonS3Cloud;
import source.restaurant_web_project.util.LanguageTranslator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CategoryServiceIMPLTest {

    private CategoryServiceIMPL test;
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private LanguageTranslator languageTranslator;

    @Mock
    private AmazonS3Cloud fileUploaderToS3;

    @BeforeEach
    void setUp() throws IOException {
        CategoryBuilder categoryBuilder = new CategoryBuilder(new ModelMapper());
        test = new CategoryServiceIMPL(categoryRepository, fileUploaderToS3, languageTranslator, categoryBuilder);
    }

    @Test
    void addCategory() throws SQLException, IOException {

        when(fileUploaderToS3.upload(any(),any())).thenReturn(null);


        when(categoryRepository.findCategoryByName(any())).thenReturn(new Category());

        MultipartFile multipartFile = new MockMultipartFile("name", "name", "images/jpeg", new byte[2]);
        Assertions.assertThrows(ConflictException.class, () -> test.addCategory(multipartFile, new CategoryAddDto()));

        when(categoryRepository.findCategoryByName(any())).thenReturn(null);

        CategoryAddDto category = new CategoryAddDto();
        category.setName("test");
        category.setDescription("desc");

        when(categoryRepository.count()).thenReturn(1L);

        when(fileUploaderToS3.upload(any(),any())).thenReturn(null);
        test.addCategory(multipartFile, category);
        verify(categoryRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void editCategory() throws SQLException, IOException {

        when(fileUploaderToS3.upload(any(),any())).thenReturn(null);

        CategoryEditDTO categoryEditDTO = new CategoryEditDTO();
        MultipartFile multipartFile = new MockMultipartFile("name", new byte[2]);

        when(categoryRepository.findCategoryByPosition(3L)).thenReturn(null);
        Assertions.assertThrows(NotFoundException.class, () -> test.editCategory(multipartFile, 3L, categoryEditDTO));

        Category category = new Category();

        categoryEditDTO.setPosition(4L);
        category.setPosition(3L);
        when(categoryRepository.findCategoryByPosition(3L)).thenReturn(category);
        when(categoryRepository.count()).thenReturn(2L);
        Assertions.assertThrows(BadRequestException.class, () -> test.editCategory(multipartFile, 3L, categoryEditDTO));

        categoryEditDTO.setPosition(1L);

        Category oldCategory = new Category();
        when(categoryRepository.findCategoryByPosition(1L)).thenReturn(oldCategory);

        category.setName("name1");
        categoryEditDTO.setName("name2");

        Category findAllCategory = new Category();
        findAllCategory.setName(categoryEditDTO.getName());
        when(categoryRepository.findAll()).thenReturn(List.of(findAllCategory));
        when(languageTranslator.translateFromJson("name1")).thenReturn("name1");
        when(languageTranslator.translateFromJson("name2")).thenReturn("name2");

        Assertions.assertThrows(ConflictException.class, () -> test.editCategory(multipartFile, 3L, categoryEditDTO));

        categoryEditDTO.setName("name1");

        test.editCategory(multipartFile, 3L, categoryEditDTO);
        verify(categoryRepository, times(1)).saveAllAndFlush(any());
    }

    @Test
    void deleteCategory() {
        Assertions.assertThrows(NotFoundException.class, () -> test.deleteCategory(3L));
        Category category = new Category();
        when(categoryRepository.findCategoryByPosition(3L)).thenReturn(category);

        category.setPosition(1L);
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        test.deleteCategory(3L);
        verify(categoryRepository, times(1)).delete(category);
        verify(categoryRepository, times(1)).saveAllAndFlush(any());
    }

    @Test
    void getAllCategories() throws SQLException {
        Category category = new Category();
        category.setName("name");
        category.setDescription("desc");

        when(categoryRepository.findAll()).thenReturn(List.of(category));
        Assertions.assertEquals(test.getAllCategories().size(), 1);
    }
}