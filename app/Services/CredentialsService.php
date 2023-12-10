<?php

namespace App\Services;

use App\Contracts\CredentialsServiceInterface;
use App\Models\Credentials;

class CredentialsService implements CredentialsServiceInterface
{
    public function getAllCredentials()
    {
        return Credentials::all()->first();
    }

    public function setCredentials($credentials)
    {
        $credentailsEntity = $this->getAllCredentials();

        $keys = array_keys($credentials);


        foreach ($keys as $credentialName) {
            $credentailsEntity->$credentialName = $credentials[$credentialName];
        }

        $credentailsEntity->save();
    }
}
