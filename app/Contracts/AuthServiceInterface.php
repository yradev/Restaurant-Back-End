<?php

namespace App\Contracts;

/*
 Authentication with sanctum
    - abilities
       - saved in db as roles thats are user, staff and owner
       - many2many relation between user and staff
    - using auth token from sanctum
    - reset password validation from sanctum with custom notifier
*/

interface AuthServiceInterface
{
    /*
    login() - login into server and get auth token for authenticated operations
    -  NotFoundException - username not found
    -  BadCredentialsException - password is wrong
    -  http status 200 - with body of auth token
    */
    public function login($email, $password);

    /*
    register() - register into server, logging required it is not logging after registration.
    - ConflictException - email exists
    - https status 200 - username registered sucessfully
    */
    public function register($email, $password);

    /* 
    sendVerification() - sending verification email with link for later steps of reseting the password.
    credentials: email, url: frontend url that will be used for verification etc http://myurl.com/reset-password and it will generate url http://myyrl.com/reset-password/email/token
    - ConflictException - we alredy have verification email sended
    - http status 200 - verification sended to email sucessfully
    */
    public function sendVerification($email, $url);

    /*
    resetPassword() - resetting password from url
     - BadCredentialsError - token is wrong or we dont have token
     - status 200 - password is changed
    */
    public function resetPassword($email, $token, $password);
}
