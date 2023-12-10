<?php

namespace App\Http\Controllers;

use App\Exceptions\BadCredentialsException;
use App\Services\UserService;
use App\Utils\Validator;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

/* 
UserController - Controlling user actions
 */

class UserController extends Controller
{
    protected UserService $userService;

    public function __construct(UserService $userService)
    {
        $this->userService = $userService;
    }

    /*
     getUserData() - grtting data for logged user
    */
    public function getLoggedUserData()
    {
        return $this->userService->getUserData(Auth::user() -> email);
    }

    /* Changing email of authenticated user 
        - Validator - validate new email input
        - userService -> changeEmail() -- calling changing email functionality
    */
    public function changeEmail($email)
    {
        try {
            Validator::validateRequestData(
                ['email' => $email],
                [
                    'email' => 'required|email'
                ]
            );
        } catch (Exception $exc) {
            throw new BadCredentialsException($exc->getMessage());
        }

        $this->userService->changeEmail($email);
    }

    /*
    changePassword() - Changing password of authenticated user
     - Validator - validate variables from request
     - userService -> changePassword() - changing password functionality
    */
    public function changePassword(Request $request)
    {
        $currentPassword = $request->currentPassword;
        $newPassword = $request->newPassword;

        try {
            Validator::validateRequestData(
                ['password' => $newPassword],
                [
                    'password' => 'required|string|min:8'
                ]
            );
        } catch (Exception $exc) {
            throw new BadCredentialsException($exc->getMessage());
        }

        $this->userService->changePassword($currentPassword, $newPassword);
    }

    /*
    deleteLoggedUser() - Delete current logged user
    */

    public function deleteLoggedUser(Request $request){
        $this->userService->deleteUser($request -> user() -> email);
    }

    /* getUserData() -> Get data of user (required Owner role) */
    public function getUserData($email){
        return response() -> json($this -> userService -> getUserData($email));
    }

    /* getAllUsers() -> Get data of all users (required Owner role) */
    public function getAllUsers(){
        return response() -> json($this -> userService -> getAllUsers());
    }

    /* userEdit() -> editing data of user (required Owner role)
       credentials: enabled, roles array  
    */

    public function userEdit(Request $request, $email){
        return $this -> userService -> editUserData($email, $request -> enabled, $request -> roles);
    }
}
