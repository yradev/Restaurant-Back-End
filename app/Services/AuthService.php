<?php

namespace App\Services;

use App\Contracts\AuthServiceInterface;
use App\Exceptions\BadCredentialsException;
use App\Exceptions\ConflictException;
use App\Exceptions\ForbiddenAccessException;
use App\Exceptions\NotFoundException;
use App\Models\Builder\UserBuilder;
use App\Models\Role;
use App\Models\User;
use App\Notifications\CustomResetPasswordNotification;
use Carbon\Carbon;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Notification;
use Illuminate\Support\Facades\Password;

class AuthService implements AuthServiceInterface
{
    public function login($email, $password)
    {
        $authTokenExpireMinutes = config('sanctum.expiration');

        $currentUser = User::where('email', $email)->first();

        if ($currentUser == null || $currentUser->deleted) {
            throw new NotFoundException(__("errors.emailNotFound"));
        }

        if (!$currentUser -> enabled) {
            throw new ForbiddenAccessException(__("errors.acountIsDisabled"));
        }

        if (!Hash::check($password, $currentUser->password)) {
            throw new BadCredentialsException(__("errors.wrongPassword"));
        }

        $roles = array_map(function ($value) {
            return $value->ability;
        }, $currentUser->roles->all());

        $activeToken = $currentUser->tokens->last();

        if ($activeToken != null) {
            $activeToken->delete();
        }

        return $currentUser->createToken('authToken', $roles, now()->addMinutes($authTokenExpireMinutes))->plainTextToken;
    }

    public function register($email, $password)
    {
        $user = User::where('email', $email)->first();
        $roles = [];
        $roles[] = Role::where('name', 'ROLE_USER')->first();

        if ($user != null) {
            if (!$user->deleted) {
                throw new ConflictException(__("errors.emailConflict"));
            }else{
                $user -> deleted = false;
                $user -> password = Hash::make($password);
                $user -> roles = $roles;
                $user -> save();
                return;
            }
        }

      

        if (User::count() === 0) {
            $rolesAll = Role::all();

            foreach ($rolesAll as $role) {
                if ($role->name != 'ROLE_USER') {
                    $roles[] = $role;
                }
            }
        }

        (new UserBuilder())
            ->withEmail($email)
            ->withPassword($password)
            ->withRoles($roles)
            ->isDeleted(false)
            ->isEnabled(true)
            ->build();
    }

    public function sendVerification($email, $url)
    {
        $tokenExpireMinutes = config('auth.passwords.users.expire');

        $currentToken = DB::table('password_reset_tokens')
            ->where('email', $email)
            ->first();

        if ($currentToken !== null) {
            $currentDateTime = now();
            $tokenExpireTime = Carbon::parse($currentToken->created_at)->addMinutes($tokenExpireMinutes);

            if ($currentDateTime < $tokenExpireTime) {
                throw new ConflictException(__("errors.conflictToken"));
            }
        }

        $user = User::where('email', $email)->first();
        $token = Password::createToken($user);

        Notification::send($user, new CustomResetPasswordNotification($url, $email, $token));
    }

    public function resetPassword($email, $token, $password)
    {
        $response = Password::reset(
            [
                'email' => $email,
                'password' => $password,
                'password_confirmation' => $password,
                'token' => $token,
            ],
            function ($user, $password) {
                $user->password = Hash::make($password);
                $user->save();
            }
        );

        if ($response != Password::PASSWORD_RESET) {
            throw new BadCredentialsException(__($response));
        }
    }
}
