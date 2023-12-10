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
        Schema::create('app_credentials', function (Blueprint $table) {
            $table->id();
            $table->string('name')-> nullable(true);
            $table->string('subname')-> nullable(true);
            $table->string('phone')-> nullable(true);
            $table->string('firm')-> nullable(true);
            $table->string('location')-> nullable(true);
            $table->string('facebookLink')-> nullable(true);
            $table->string('instagramLink')-> nullable(true);
            $table->string('twitterLink')-> nullable(true);
            $table->string('email') -> nullable(true);
            $table->string('street')-> nullable(true);
            $table->string('city') -> nullable(true);
            $table->time('openTime')-> nullable(true);
            $table->time('closeTime')-> nullable(true);
            $table->string('openDay')-> nullable(true);
            $table->string('closeDay')-> nullable(true);
            $table->timestamps();
        });

        DB::table('app_credentials')->insert([
            [
                'name' => null,
                'subname' => null,
                'phone' => null,
                'firm' => null,
                'location' => null,
                'facebookLink' => null,
                'instagramLink' => null,
                'twitterLink' => null,
                'email' => null,
                'street' => null,
                'city' => null,
                'openTime' => null,
                'closeTime' => null,
                'openDay' => null,
                'closeDay' => null
            ],
        ]);
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('app_credentials');
    }
};
