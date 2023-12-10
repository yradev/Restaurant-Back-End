<?php

namespace App\Exceptions;

use Exception;

class BadCredentialsException extends Exception
{
    public function render(){
        $message = $this -> message;
        if(json_decode($message) !== null){
            return response() -> json(json_decode($message), 400);
        }else{
            return response($this -> message, 400);
        }
    }
}
