<?php

namespace App\Contracts;

interface UserServiceInterface
{
  /* 
  getUserData() - Return data stored in db for user
   credentials - email or user entity
    - NotFoundException - we dont have user with this email
    - return json contains email, array with name of user`s roles
  */
  public function getUserData($credentials);

  /*
  changeEmail() - Changing email of authenticated user
   - ConflictException - email is same like authenticated
   - sucessfull email changing
   -- logging out after email changing
  */
  public function changeEmail($newEmail);

  /*
  changePassword() - Changing password of authenticated user
   - BadCredentialsException - current password is wrong
   - sucessfull password changing
   -- logging out after email changing
  */
  public function changePassword($currentPassword,$newPassword);

  /*
  deleteUser() - Deleting user from app
    - NotFoundException - We dont have user with this email
  */
  public function deleteUser($email);

  /* getAllUsers() - Return array with all users in db */

  public function getAllUsers();

  /* editUserData($email, $enabled, $roles)
       - NotFoundException
       --- Dont have user with this email
       --- Dont have role with this name
  */

  public function editUserData($email, $enabled, $roles);



}
