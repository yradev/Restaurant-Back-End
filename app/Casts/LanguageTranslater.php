<?php

namespace App\Casts;

use Illuminate\Contracts\Database\Eloquent\CastsAttributes;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\App;

class LanguageTranslater implements CastsAttributes
{

    /**
     * Cast the given value.
     *
     * @param  array<string, mixed>  $attributes
     */
    public function get(Model $model, string $key, mixed $value, array $attributes): mixed
    {
        if ($value == null) {
            return null;
        }

        $values = collect(json_decode($value, true));

        $filteredValues = $values
            ->filter(function ($currValue) {
                return $currValue['lang'] == App::getLocale();
            })
            ->values()
            ->collect();

        if ($filteredValues->count() == 0) {
            return null;
        }

        return $filteredValues->get(0)['value'];
    }

    /**
     * Prepare the given value for storage.
     *
     * @param  array<string, mixed>  $attributes
     * 
     */
    public function set(Model $model, string $key, mixed $value, array $attributes): mixed
    {
        $values = collect(json_decode($attributes[$key], true));

        $newValue = [
            'lang' => App::getLocale(),
            'value' => $value
        ];

        if ($values->isEmpty()) {
            return json_encode(
                [
                    $newValue
                ]
            );
        }

        $filteredValues = $values
            ->filter(function ($value) {
                return $value['lang'] == App::getLocale();
            })
            ->values()
            ->collect();

        if ($filteredValues->count() == 0) {
            $values -> push($newValue);
        } else {
            $values -> put($values->search($filteredValues -> get(0)), $newValue);
        }
        return json_encode($values);
    }
}
