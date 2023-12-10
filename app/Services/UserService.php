<?php

namespace App\Services;

use App\Contracts\UserServiceInterface;
use App\Exceptions\BadCredentialsException;
use App\Exceptions\ConflictException;
use App\Exceptions\NotFoundException;
use App\Models\Role;
use App\Models\User;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use Spatie\FlareClient\Http\Exceptions\NotFound;

class UserService implements UserServiceInterface
{
    public function getUserData($email)
    {
        $user = User::where('email', $email)->first();

        if ($user === null) {
            throw new NotFoundException(__('errors.emailNotFound'));
        }

        $user -> roles = $user -> roles -> all();

        return $user;
    }

    public function changeEmail($newEmail)
    {
        $user = Auth::user();

        if ($user->email === $newEmail) {
            throw new ConflictException(__('errors.authEmailConflict'));
        }

        $user->email = $newEmail;
        $user->save();

        $user->tokens->last()->delete();
    }

    public function changePassword($currentPassword, $newPassword)
    {
        $user = Auth::user();

        if (!Hash::check($currentPassword, $user->password)) {
            throw new BadCredentialsException(__('errors.wrongPassword'));
        }

        $password = Hash::make($newPassword);

        $user->password = $password;
        $user->save();
        $user->tokens->last()->delete();
    }

    public function deleteUser($email)
    {
        $user = User::where('email', $email)->first();

        if ($user == null) {
            throw new NotFoundException(__('errors.emailNotFound'));
        }

        $user->deleted = true;
        $user->save();
        $user->tokens->last()->delete();
    }

    public function getAllUsers()
    {
        return User::all()
            ->map(function ($user) {
                return ['email' => $user->email];
            })
            ->collect();
    }

    public function editUserData($email, $enabled, $roles)
    {
        $user = User::where('email', $email)->first();

        if ($user == null) {
            throw new NotFoundException(__('errors.emailNotFound'));
        }

        $roles = array_map(function ($roleName) {
            $role = Role::where('name', $roleName)->first();
            if ($role == null) {
                throw new NotFoundException(__('errors.roleNotFound'));
            }
            return $role->id;
        }, $roles);

        $user->roles()->sync($roles);
        $user->save();

        $token = $user->tokens->last();

        if ($token != null) {
            $user->tokens->delete();
        }
    }
}
