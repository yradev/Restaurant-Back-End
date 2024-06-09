package source.restaurant_web_project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import source.restaurant_web_project.errors.UnauthorizedException;
import source.restaurant_web_project.models.dto.user.UserChangePasswordDTO;
import source.restaurant_web_project.models.dto.user.UserControlDTO;
import source.restaurant_web_project.models.dto.user.UserDataViewDTO;
import source.restaurant_web_project.services.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserDataViewDTO> getUserData(Principal principal) {

        if(principal == null){
            throw new UnauthorizedException();
        }

        return ResponseEntity.ok(userService.getUserData(principal.getName()));
    }

   @PutMapping("change/email/{email}")
   public ResponseEntity<?>changeUserEmail(@PathVariable String email){
        userService.changeEmail(email);
        return ResponseEntity.ok().build();
   }

   @PutMapping("change/password")
   public ResponseEntity<?>changeUserPassword(@RequestBody UserChangePasswordDTO userChangePasswordDTO){
        userService.changePassword(userChangePasswordDTO);
        return ResponseEntity.ok().build();
   }

   @DeleteMapping("delete")
   public ResponseEntity<?>deleteCurrentUser(){
        userService.deleteCurrentUser();
        return ResponseEntity.ok().build();
   }
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("{email}")
    public ResponseEntity<UserControlDTO> getUserData(@PathVariable String email) {
        UserControlDTO userControlDTO = userService.getUserCoreSettings(email);
        return ResponseEntity.ok(userControlDTO);
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("filter/all")
    public ResponseEntity<List<UserDataViewDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsersByEmail(null));
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("filter/{email}")
    public ResponseEntity<List<UserDataViewDTO>> getAllUsersByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUsersByEmail(email));
    }
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("roles")
    public ResponseEntity<?> getActiveRoles() {
        return ResponseEntity.ok(userService.getActiveRoles());
    }

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @PutMapping("control/{email}")
    public ResponseEntity<?> editUserCore(@PathVariable String email, @RequestBody UserControlDTO userData) {
        userService.changeUserCoreData(email, userData);
        return ResponseEntity.ok().build();
    }
}
