<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('roles', function (Blueprint $table) {
            $table->id();
            $table->string('name');
            $table->string('ability',64);
        });
    
    
        DB::table('roles')->insert([
            [
                'name' => 'ROLE_OWNER',
                'ability' => 'owner',
            ],
            [
                'name' => 'ROLE_STAFF',
                'ability' => 'staff',
            ],
            [
                'name' => 'ROLE_USER',
                'ability' => 'user',
            ],
        ]);
    }



    /**
     * Reverse the migrations.
     * 
     */
    public function down(): void
    {
        Schema::dropIfExists('roles');
    }
};
