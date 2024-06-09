package source.restaurant_web_project.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import source.restaurant_web_project.models.dto.item.ItemAddDto;
import source.restaurant_web_project.models.dto.item.ItemEditDTO;
import source.restaurant_web_project.services.ItemService;


import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }



    @GetMapping("category/{position}")
    public ResponseEntity<?>getAllItemsByCategory(@PathVariable long position){
        return ResponseEntity.ok(itemService.getAllItemsByCategory(position));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findItemsById(@PathVariable long id) throws SQLException, IOException {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PostMapping("add")
    public ResponseEntity<?> addItem(@RequestParam("image") MultipartFile image, @Valid ItemAddDto itemAddDto) throws SQLException, IOException {
        itemService.addItem(image, itemAddDto);
        return ResponseEntity.created(null).build();
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @DeleteMapping("delete/category/{categoryPosition}/item/{itemPosition}")
    public ResponseEntity<?> deleteItem(@PathVariable long categoryPosition, @PathVariable long itemPosition) {
        itemService.deleteItem(categoryPosition,itemPosition);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping("edit/category/{categoryPosition}/item/{itemPosition}")
    public ResponseEntity<?> editItem(@RequestParam(value = "image",required = false) MultipartFile image, @PathVariable long categoryPosition, @PathVariable long itemPosition, @Valid ItemEditDTO itemEditDTO) throws SQLException, IOException {
        itemService.editItem(image,categoryPosition, itemPosition, itemEditDTO);
        return ResponseEntity.ok().build();
    }
}
