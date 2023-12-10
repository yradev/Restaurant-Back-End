<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Role extends Model
{
    use HasFactory;

    protected $table = 'roles';

    protected $fillable = [
        'name',
    ];

    protected $hidden = [
        'id',
        'ability',
        'pivot'
    ];

    public function users(){
        return $this -> belongsToMany(User::class);
    }
}
