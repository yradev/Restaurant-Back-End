<?php

namespace App\Utils;

use Exception;
use Illuminate\Support\Facades\Validator as FacadesValidator;

/* Custom validatior that validate values from response */
class Validator
{
    public static function validateRequestData($credentials, $rules)
    {
        $validator = FacadesValidator::make($credentials, $rules);

        if ($validator->fails()) {
            throw new Exception($validator->errors());
        }
    }
}
