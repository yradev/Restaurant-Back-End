<?php

namespace App\Http\Controllers;

use App\Services\AuthService;
use App\Utils\Validator;
use Exception;
use Illuminate\Http\Request;

/*
  AuthController - Responsive for authentication operations
*/
class AuthController extends Controller
{
    protected $authService;

    public function __construct(AuthService $authService)
    {
        $this->authService = $authService;
    }

    /* 
    login() -> Authorization in app
      - Valiadtor - validating data from request
      - authService -> login - calling login functionality and returning response with authentication token
    */
    public function login(Request $request)
    {
        $credentials =
            [
                'email' => $request->email,
                'password' => $request->password
            ];

        try {
            Validator::validateRequestData($credentials, [
                'email' => 'required|email',
                'password' => 'required|string|min:8'
            ]);
        } catch (Exception $exc) {
            return response()->json(json_decode($exc->getMessage()), 400);
        }

        return $this->authService->login($credentials['email'], $credentials['password']);
    }

    /* 
    register() -> Register new user in app
      - Valiadtor - validating data from request
      - authService -> register - calling register functionality
    */
    public function register(Request $request)
    {
        $credentials = [
            'email' => $request->email,
            'password' => $request->password
        ];

        try {
            Validator::validateRequestData($credentials, [
                'email' => 'required|email',
                'password' => 'required|string|min:5'
            ]);
        } catch (Exception $exc) {
            return response()->json(json_decode($exc->getMessage()), 400);
        }

        $this->authService->register($credentials['email'], $credentials['password']);
    }

    /* 
    sendResetLinkEmail() -> Sending reset password validation link to email
      - Valiadtor - validating data from request
      - authService -> sendVerification - calling sending verification functionality
    */
    public function sendResetLinkEmail(Request $request)
    {
        $credentials = [
            'email' => $request->email,
            'url' => $request->url
        ];

        try {
            Validator::validateRequestData($credentials, [
                'email' => 'required|email',
                'url' => 'required|string'
            ]);
        } catch (Exception $exc) {
            return response()->json(json_decode($exc->getMessage()), 400);
        }

        $this->authService->sendVerification($credentials['email'], $credentials['url']);
    }

    /* 
    resetPassword() -> Reseting password with token from email
      - Valiadtor - validating data from request
      - authService -> resetPassword - calling reset password functionality
    */
    public function resetPassword(Request $request, $email, $token)
    {
        $credentials = [
            'email' => $email,
            'token' => $token,
            'password' => $request->password
        ];

        try {
            Validator::validateRequestData($credentials, [
                'email' => 'required|email',
                'token' => 'required|string',
                'password' => 'required|string|min:8'
            ]);
        } catch (Exception $exc) {
            return response()->json(json_decode($exc->getMessage()), 400);
        }

        $this->authService->resetPassword($credentials['email'], $credentials['token'], $credentials['password']);
    }
}
