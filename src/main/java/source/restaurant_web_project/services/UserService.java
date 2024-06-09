package source.restaurant_web_project.services;

import source.restaurant_web_project.models.dto.user.*;

import java.util.List;

public interface UserService {
    UserDataViewDTO getUserData(String email);

    void changeEmail(String email);

    void changePassword(UserChangePasswordDTO userChangePasswordDTO);

    List<RoleDTO> getActiveRoles();
    void changeUserCoreData(String email,UserControlDTO userControlDTO);
    UserControlDTO getUserCoreSettings(String email);

    void deleteCurrentUser();

    List<UserDataViewDTO> getUsersByEmail(String email);
}
