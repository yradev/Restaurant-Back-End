package source.restaurant_web_project.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.builders.ItemBuilder.ItemBuilder;
import source.restaurant_web_project.errors.BadRequestException;
import source.restaurant_web_project.errors.ConflictException;
import source.restaurant_web_project.errors.NotFoundException;
import source.restaurant_web_project.models.dto.item.*;
import source.restaurant_web_project.models.dto.item.ItemViewDTO;
import source.restaurant_web_project.models.entity.Category;
import source.restaurant_web_project.models.entity.Item;
import source.restaurant_web_project.repositories.CategoryRepository;
import source.restaurant_web_project.repositories.ItemRepository;
import source.restaurant_web_project.services.ItemService;
import source.restaurant_web_project.util.AmazonS3Cloud;
import source.restaurant_web_project.util.LanguageTranslator;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ItemServiceIMPL implements ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final LanguageTranslator languageTranslator;
    private final ItemBuilder itemBuilder;

    private final AmazonS3Cloud cloud;

    public ItemServiceIMPL(ItemRepository itemRepository, CategoryRepository categoryRepository, LanguageTranslator languageTranslator, ItemBuilder itemBuilder, AmazonS3Cloud cloud) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.languageTranslator = languageTranslator;
        this.itemBuilder = itemBuilder;
        this.cloud = cloud;
    }

    @Override
    public void addItem(MultipartFile image, ItemAddDto itemAddDto) throws IOException, SQLException {
        Category category = categoryRepository.findCategoryByPosition(itemAddDto.getCategoryPosition());

        if (category == null) {
            throw new NotFoundException();
        }

        if (checkItemNameAvaliable(itemAddDto.getName())) {
            throw new ConflictException();
        }

        String imageUrl = cloud.upload("img-" + UUID.randomUUID(), image);

        itemAddDto.setName(languageTranslator.translateToJSON(itemAddDto.getName(), null));
        itemAddDto.setDescription(languageTranslator.translateToJSON(itemAddDto.getDescription(), null));

        Item item = itemBuilder.buildItem()
                .mapToClass(itemAddDto)
                .withImageUrl(imageUrl)
                .withCategory(category)
                .withPosition(category.getItems().size() + 1)
                .build();

        itemRepository.saveAndFlush(item);
    }

    @Override
    public void deleteItem(long categoryPosition, long itemPosition) {
        Item currentItem = itemRepository.findItemsByCategory_Position(categoryPosition).stream()
                .filter(a -> a.getPosition() == itemPosition)
                .findFirst()
                .orElse(null);

        if (currentItem == null) {
            throw new NotFoundException();
        }

        Category category = currentItem.getCategory();
        category.getItems().remove(currentItem);

        itemRepository.delete(currentItem);

        if(currentItem.getImageUrl()!=null) {
            cloud.delete(currentItem.getImageUrl().substring(currentItem.getImageUrl().indexOf("img-")));
        }

        if (category.getItems().size() + 1 != currentItem.getPosition()) {
            List<Item> items = category.getItems().stream()
                    .peek(a -> a.setPosition(a.getPosition() - 1))
                    .toList();

            itemRepository.saveAllAndFlush(items);
        }
    }

    @Override
    public void editItem(MultipartFile image, long categoryPosition, long itemPosition, ItemEditDTO itemEditDTO) throws IOException, SQLException {
        List<Item> itemsForUpdate = new ArrayList<>();

        Item currentItem = itemRepository.findItemsByCategory_Position(categoryPosition).stream()
                .filter(a -> a.getPosition() == itemPosition)
                .findFirst()
                .orElseThrow(NotFoundException::new);

        String itemName = languageTranslator.translateFromJson(currentItem.getName());

        if (!itemName.equals(itemEditDTO.getName()) && checkItemNameAvaliable(itemEditDTO.getName())) {
            throw new ConflictException();
        }

        if (itemEditDTO.getPosition() < 0 || itemEditDTO.getPosition() > currentItem.getCategory().getItems().size()) {
            throw new BadRequestException();
        }


        if (currentItem.getPosition() != itemEditDTO.getPosition()) {
            Item oldItem = currentItem.getCategory().getItems().stream()
                    .filter(a -> a.getPosition() == itemEditDTO.getPosition())
                    .findFirst()
                    .map(a -> itemBuilder.buildItem()
                            .cloneObject(a)
                            .withPosition(currentItem.getPosition())
                            .build())
                    .get();

            itemsForUpdate.add(oldItem);
        }


        String name = languageTranslator.translateToJSON(itemEditDTO.getName(), currentItem.getName());
        String description = languageTranslator.translateToJSON(itemEditDTO.getDescription(), currentItem.getDescription());

        String imageUrl = currentItem.getImageUrl();

        if (image != null) {
            if(imageUrl!=null) {
                cloud.delete(currentItem.getImageUrl().substring(currentItem.getImageUrl().indexOf("img-")));
            }
            imageUrl = cloud.upload("img-" + UUID.randomUUID(), image);
        }

        Item updatedItem = itemBuilder.buildItem()
                .cloneObject(currentItem)
                .mapToClass(itemEditDTO)
                .withImageUrl(imageUrl)
                .withName(name)
                .withDescription(description)
                .build();

        itemsForUpdate.add(updatedItem);

        itemRepository.saveAllAndFlush(itemsForUpdate);
    }

    @Override
    public ItemsAllViewDTO getAllItemsByCategory(long position) {

        Category category = categoryRepository.findCategoryByPosition(position);

        if (category == null) {
            throw new NotFoundException();
        }


        List<ItemViewDTO> items = category.getItems().stream()
                .sorted(Comparator.comparingLong(Item::getPosition))
                .map(a -> itemBuilder.buildItemView()
                        .mapToClass(a)
                        .withImageUrl(a.getImageUrl())
                        .withName(languageTranslator.translateFromJson(a.getName()))
                        .withDescription(languageTranslator.translateFromJson(a.getDescription()))
                        .build()
                ).toList();

        ItemsAllViewDTO itemsAllViewDTO = new ItemsAllViewDTO();
        itemsAllViewDTO.setCategoryName(languageTranslator.translateFromJson(category.getName()));
        itemsAllViewDTO.setItems(items);

        return itemsAllViewDTO;
    }

    @Override
    public ItemFavoritesDTO getItemById(long id){
        Item item = itemRepository.findById(id).orElseThrow(NotFoundException::new);

        return itemBuilder.buildFavorites()
                .mapToClass(item)
                .withItemPosition(item.getPosition())
                .withItemName(languageTranslator.translateFromJson(item.getName()))
                .withCategoryPosition(item.getCategory().getPosition())
                .withCategoryName(languageTranslator.translateFromJson(item.getCategory().getName()))
                .withImageUrl(item.getImageUrl())
                .build();
    }

    private boolean checkItemNameAvaliable(String name) {
        return itemRepository.findAll().stream()
                .map(a -> languageTranslator.translateFromJson(a.getName()))
                .anyMatch(a -> a.equals(name));

    }

}