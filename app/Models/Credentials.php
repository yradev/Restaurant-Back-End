<?php

namespace App\Models;

use App\Casts\LanguageTranslater;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Credentials extends Model
{
    use HasFactory;

    protected $table = 'app_credentials';

    protected $fillabe = [
        'phone',
        'firm',
        'facebookLink',
        'instagramLink',
        'twitterLink',
        'email',
        'openTime',
        'closeTime',
        'openDay',
        'closeDay'
    ];

    protected $hidden = [
        'id'
    ];

    protected $casts = [
        'name' => LanguageTranslater::class,
        'subname' => LanguageTranslater::class,
        'location' => LanguageTranslater::class,
        'street' => LanguageTranslater::class,
        'city' => LanguageTranslater::class,
    ];
}
