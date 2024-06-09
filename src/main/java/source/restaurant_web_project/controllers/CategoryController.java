package source.restaurant_web_project.controllers;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.dto.category.CategoryAddDto;
import source.restaurant_web_project.models.dto.category.CategoryEditDTO;
import source.restaurant_web_project.services.CategoryService;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping()
    public ResponseEntity<?> categories() {
        return ResponseEntity.ok()
                .body(categoryService.getAllCategories());
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping( value = "add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> categoryAdd(@RequestParam("image") MultipartFile image, @Valid CategoryAddDto categoryAddDto) throws SQLException, IOException {
        categoryService.addCategory(image,categoryAddDto);
        return ResponseEntity.created(URI.create("")).build();
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping("edit/{currentPosition}")
    public ResponseEntity<?> categoryEdit(@RequestParam(value = "image",required = false) MultipartFile image, @PathVariable long currentPosition, @Valid CategoryEditDTO categoryDTO) throws SQLException, IOException {
        categoryService.editCategory(image, currentPosition, categoryDTO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @DeleteMapping("delete/{position}")
    public ResponseEntity<?> categoryDelete(@PathVariable long position) {
        categoryService.deleteCategory(position);
        return ResponseEntity.ok().build();
    }
}
