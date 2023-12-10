<?php

namespace App\Contracts;

interface CredentialsServiceInterface
{
    /* getAllCredentials() - return json of all credentials needed in our app */
    public function getAllCredentials();
    
    /* setCredentials() - Saving credentials in db */
    public function setCredentials($credentials);
}
