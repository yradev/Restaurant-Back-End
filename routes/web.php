<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\CredentialsController;
use App\Http\Controllers\UserController;
use App\Models\Credentials;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "web" middleware group. Make something great!
|
*/

/* Everyone can catch this routes */
Route::get('/',[CredentialsController::class, 'getCredentials']);
Route::post('/credentials/set',[CredentialsController::class, 'setCredentials']);

/* Only for not logged users */
Route::middleware('guest')->group(function () {
    Route::post('/auth/login', [AuthController::class, 'login']);
    Route::post('/auth/register', [AuthController::class, 'register']);
    Route::post('/auth/reset-password/verification/send', [AuthController::class, 'sendResetLinkEmail']);
    Route::post('/auth/reset-password/verify/{email}/{token}', [AuthController::class, 'resetPassword'])->name('password.reset');
});
/* Only for logged users with user role */
Route::middleware('auth:sanctum', 'abilities:user')->group(function(){
    Route::get('/user', [UserController::class, 'getLoggedUserData']);
    Route::put('/user/change/email/{email}', [UserController::class, 'changeEmail']);
    Route::put('/user/change/password', [UserController::class, 'changePassword']);
    Route::delete('/user/delete', [UserController::class, 'deleteLoggedUser']);
});

/* Only for logged users with owner role */
Route::middleware('auth:sanctum', 'abilities:owner')->group(function(){
    Route::get('/user/{email}', [UserController::class, 'getUserData']);
    Route::get('/user/filter/all', [UserController::class, 'getAllUsers']);
    Route::put('/user/edit/{email}', [UserController::class, 'userEdit']);
});