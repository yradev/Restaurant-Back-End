package source.restaurant_web_project.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.ConflictException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.models.builders.CategoryBuilder.CategoryBuilder;
import source.restaurant_web_project.models.builders.ItemBuilder.ItemBuilder;
import source.restaurant_web_project.models.dto.item.ItemAddDto;
import source.restaurant_web_project.models.dto.item.ItemEditDTO;
import source.restaurant_web_project.models.entity.Category;
import source.restaurant_web_project.models.entity.Item;
import source.restaurant_web_project.repositories.CategoryRepository;
import source.restaurant_web_project.repositories.ItemRepository;
import source.restaurant_web_project.util.AmazonS3Cloud;
import source.restaurant_web_project.util.LanguageTranslator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceIMPLTest {

    private ItemServiceIMPL test;
    @Mock
    private ItemRepository itemRepository;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private LanguageTranslator languageTranslator;

    private ItemBuilder itemBuilder = new ItemBuilder(new ModelMapper());
    private CategoryBuilder categoryBuilder = new CategoryBuilder(new ModelMapper());

    @Mock
    private AmazonS3Cloud fileUploaderToS3;

    @BeforeEach
    void setUp() {
        ItemBuilder itemBuilder = new ItemBuilder(new ModelMapper());
        test = new ItemServiceIMPL(itemRepository, categoryRepository, languageTranslator, itemBuilder, fileUploaderToS3);
    }

    @Test
    void addItem() throws SQLException, IOException {
        when(fileUploaderToS3.upload(any(),any())).thenReturn(null);


        MultipartFile multipartFile = new MockMultipartFile("name", new byte[2]);
        ItemAddDto itemAddDto = new ItemAddDto();
        itemAddDto.setCategoryPosition(1L);
        itemAddDto.setName("name");
        when(categoryRepository.findCategoryByPosition(1L)).thenReturn(null);
        Assertions.assertThrows(NotFoundException.class, () -> test.addItem(multipartFile, itemAddDto));

        Category category = new Category();

        category.setName("value");
        category.setPosition(1L);
        category.setItems(List.of(new Item()));
        when(categoryRepository.findCategoryByPosition(1L)).thenReturn(category);

        Item item = new Item();
        item.setName("trueName");

        when(itemRepository.findAll()).thenReturn(List.of(item));
        when(languageTranslator.translateFromJson(any())).thenReturn("name");

        Assertions.assertThrows(ConflictException.class, () -> test.addItem(multipartFile, itemAddDto));

        when(languageTranslator.translateFromJson(any())).thenReturn("trueName");

        test.addItem(multipartFile, itemAddDto);
        verify(itemRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void deleteItem() {

        Assertions.assertThrows(NotFoundException.class, () -> test.deleteItem(1L, 1L));

        Category category = categoryBuilder.buildCategory()
                .withName("test")
                .withPosition(999999999)
                .build();

        Item item = itemBuilder.buildItem()
                .withName("test")
                .withPosition(9999999)
                .withCategory(category)
                .build();

        List<Item> items = new ArrayList<>();
        items.add(item);
        category.setItems(items);

        when(itemRepository.findItemsByCategory_Position(999999)).thenReturn(List.of(item));

        test.deleteItem(999999, 9999999);
        verify(itemRepository, times(1)).delete(any());
    }

    //
    @Test
    void editItem() throws SQLException, IOException {
        when(fileUploaderToS3.upload(any(),any())).thenReturn(null);


        MultipartFile multipartFile = new MockMultipartFile("name", new byte[2]);

        ItemEditDTO itemEditDTO = new ItemEditDTO();
        itemEditDTO.setName("newName");
        Assertions.assertThrows(NotFoundException.class, () -> test.editItem(multipartFile, 1L, 1L, itemEditDTO));

        Item item = new Item();
        item.setName("currentName");
        item.setPosition(1L);

        when(itemRepository.findItemsByCategory_Position(1L)).thenReturn(List.of(item));
        when(languageTranslator.translateFromJson("currentName")).thenReturn("currentName");
        when(languageTranslator.translateFromJson("translatedItem")).thenReturn("newName");

        Item translatedItem = new Item();
        translatedItem.setName("translatedItem");
        when(itemRepository.findAll()).thenReturn(List.of(translatedItem));

        Assertions.assertThrows(ConflictException.class, () -> test.editItem(multipartFile, 1L, 1L, itemEditDTO));

        when(languageTranslator.translateFromJson(any())).thenReturn("value");

        itemEditDTO.setPosition(-1L);

        Assertions.assertThrows(BadRequestException.class, () -> test.editItem(multipartFile, 1L, 1L, itemEditDTO));

        itemEditDTO.setPosition(2L);
        Category category = new Category();

        Item tempItem1 = new Item();
        Item tempItem2 = new Item();
        tempItem2.setPosition(2L);
        tempItem1.setPosition(1L);
        category.setItems(List.of(tempItem1, tempItem2));
        item.setCategory(category);


        test.editItem(multipartFile, 1L, 1L, itemEditDTO);
        verify(itemRepository, times(1)).saveAllAndFlush(any());
    }

    @Test
    void getAllItemsByCategory() throws SQLException {
        Assertions.assertThrows(NotFoundException.class, () -> test.getAllItemsByCategory(1L));

        Item item = new Item();
        item.setPosition(1L);

        Category category = new Category();
        category.setItems(List.of(item));
        category.setName("name");

        when(categoryRepository.findCategoryByPosition(1L)).thenReturn(category);
        when(languageTranslator.translateFromJson(any())).thenReturn("value");

        Assertions.assertEquals(test.getAllItemsByCategory(1L).getItems().size(), 1);
    }

    @Test
    void getItemByCategoryAndItemPosition() throws SQLException, IOException {
        Assertions.assertThrows(NotFoundException.class, () -> test.getItemByCategoryAndItemPosition(1L, 1L));

        Item item = new Item();

        Category category = new Category();
        category.setName("name");
        category.setPosition(1L);

        item.setCategory(category);

        when(languageTranslator.translateFromJson(any())).thenReturn("value");
        when(itemRepository.findItemByCategory_PositionAndPosition(1L, 1L)).thenReturn(item);

        item.setPosition(1L);
        item.setName("name");

        Assertions.assertEquals(test.getItemByCategoryAndItemPosition(1L, 1L).getItemName(), "value");
    }
}