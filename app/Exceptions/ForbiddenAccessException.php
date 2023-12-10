<?php

namespace App\Exceptions;

use Exception;

class ForbiddenAccessException extends Exception
{
    public function render(){
        $message = $this -> message;
        if(json_decode($message) !== null){
            return response() -> json(json_decode($message), 403);
        }else{
            return response($this -> message, 409);
        }
    }
}
