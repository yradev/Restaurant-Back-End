package source.restaurant_web_project.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.builders.CategoryBuilder.CategoryBuilder;
import source.restaurant_web_project.errors.*;
import source.restaurant_web_project.models.dto.category.CategoryEditDTO;
import source.restaurant_web_project.models.dto.category.CategoryViewDTO;
import source.restaurant_web_project.models.dto.category.CategoryAddDto;
import source.restaurant_web_project.models.entity.Category;
import source.restaurant_web_project.models.entity.Item;
import source.restaurant_web_project.repositories.CategoryRepository;
import source.restaurant_web_project.services.CategoryService;
import source.restaurant_web_project.util.AmazonS3Cloud;
import source.restaurant_web_project.util.LanguageTranslator;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceIMPL implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final AmazonS3Cloud cloud;
    private final LanguageTranslator languageTranslator;

    private final CategoryBuilder categoryBuilder;


    public CategoryServiceIMPL(CategoryRepository categoryRepository, AmazonS3Cloud cloud, LanguageTranslator languageTranslator, CategoryBuilder categoryBuilder) {
        this.categoryRepository = categoryRepository;
        this.cloud = cloud;
        this.languageTranslator = languageTranslator;
        this.categoryBuilder = categoryBuilder;
    }

    @Override
    public void addCategory(MultipartFile image, CategoryAddDto categoryAddDto) throws IOException, SQLException {
        String name = languageTranslator.translateToJSON(categoryAddDto.getName(), null);
        String description = languageTranslator.translateToJSON(categoryAddDto.getDescription(), null);

        if (categoryRepository.findCategoryByName(name) != null) {
            throw new ConflictException("We have category with this name!");
        }

        String imageUrl = cloud.upload("img-" + UUID.randomUUID(), image);

        Category category = categoryBuilder.buildCategory()
                .mapToClass(categoryAddDto)
                .withName(name)
                .withDescription(description)
                .withPosition(categoryRepository.count() + 1)
                .withImageUrl(imageUrl)
                .build();

        categoryRepository.saveAndFlush(category);
    }

    @Override
    public void editCategory(MultipartFile image, long position, CategoryEditDTO categoryDTO) throws IOException, SQLException {
        Category category = categoryRepository.findCategoryByPosition(position);

        if (category == null) {
            throw new NotFoundException();
        }

        if (categoryDTO.getPosition() < 0 || categoryDTO.getPosition() > categoryRepository.count()) {
            throw new BadRequestException();
        }

        if (!languageTranslator.translateFromJson(category.getName()).equals(categoryDTO.getName())) {
            boolean haveCategory = categoryRepository.findAll().stream()
                    .map(a -> languageTranslator.translateFromJson(a.getName()))
                    .anyMatch(a -> a.equals(categoryDTO.getName()));

            if (haveCategory) {
                throw new ConflictException();
            }
        }

        List<Category> categoryList = new ArrayList<>();

        if (categoryDTO.getPosition() > 0 && category.getPosition() != categoryDTO.getPosition()) {
            Category oldCategory = categoryRepository.findCategoryByPosition(categoryDTO.getPosition());
            oldCategory.setPosition(category.getPosition());
            categoryList.add(oldCategory);
        }

        String imageUrl = category.getImageUrl();

        if (image != null) {
            if (imageUrl != null) {
                cloud.delete(category.getImageUrl().substring(category.getImageUrl().indexOf("img-")));
            }
            imageUrl = cloud.upload("img-" + UUID.randomUUID(), image);
        }

        Category editedCategory = categoryBuilder.buildCategory()
                .mapToClass(categoryDTO)
                .withId(category.getId())
                .withName(languageTranslator.translateToJSON(categoryDTO.getName(), category.getName()))
                .withDescription(languageTranslator.translateToJSON(categoryDTO.getDescription(), category.getDescription()))
                .withImageUrl(imageUrl)
                .build();

        categoryList.add(editedCategory);
        categoryRepository.saveAllAndFlush(categoryList);
    }

    @Override
    public void deleteCategory(long position) {
        Category category = categoryRepository.findCategoryByPosition(position);
        if (category == null) {
            throw new NotFoundException();
        }

        categoryRepository.delete(category);

        if (category.getImageUrl() != null) {
            cloud.delete(category.getImageUrl().substring(category.getImageUrl().indexOf("img-")));
        }

        List<Category> categories = categoryRepository.findAll().stream()
                .filter(a -> a.getPosition() > category.getPosition())
                .peek(a -> a.setPosition(a.getPosition() - 1))
                .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Override
    public List<CategoryViewDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .sorted(Comparator.comparingLong(Category::getPosition))
                .map(a -> categoryBuilder.buildViewCategory()
                        .mapToClass(a)
                        .withName(languageTranslator.translateFromJson(a.getName()))
                        .withDescription(languageTranslator.translateFromJson(a.getDescription()))
                        .withItemsCount(a.getItems().size())
                        .withTotalPrice(a.getItems().stream().map(Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add))
                        .build()
                )
                .toList();
    }
}
