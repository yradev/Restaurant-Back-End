<?php

namespace App\Models\Builder;

use App\Models\User;

class UserBuilder
{
    protected $values = [];
    protected $roles;

    public function withEmail($email)
    {
        $this->values['email'] = $email;
        return $this;
    }

    public function withPassword($password)
    {
        $this->values['password'] = $password;
        return $this;
    }

    public function withRoles($roles)
    {
        $this->roles = $roles;
        return $this;
    }

    public function isDeleted($isDeleted)
    {
        $this->values['deleted'] = $isDeleted;
        return $this;
    }

    public function isEnabled($isEnabled)
    {
        $this->values['enabled'] = $isEnabled;
        return $this;
    }

    public function build()
    {
        $user = User::create($this->values);

        foreach ($this->roles as $role) {
            $user->roles()->attach($role);
        }

        return $user;
    }
}
