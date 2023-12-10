<?php

namespace App\Http\Controllers;

use App\Exceptions\BadCredentialsException;
use App\Services\CredentialsService;
use App\Utils\Validator;
use Exception;
use Illuminate\Http\Request;

class CredentialsController extends Controller
{
    protected CredentialsService $credentialsService;

    public function __construct(CredentialsService $credentialsService)
    {
        $this->credentialsService = $credentialsService;
    }
    /* getCredentials() -- return all necessary credentials to work our app */
    public function getCredentials()
    {
        return response()->json($this->credentialsService->getAllCredentials());
    }

    /*
     setCrendials() -- Saving credentials in db
       - Validator: Validate credentials
       - credentialsService -> setCredentials: Save credentials functionality
    */

    public function setCredentials(Request $request)
    {
        $credentials = [
            'name' =>  $request->name,
            'subname' => $request->subname,
            'phone' => $request->phone,
            'firm' => $request->firm,
            'location' => $request->location,
            'facebookLink' => $request->facebookLink,
            'instagramLink' => $request->instagramLink,
            'twitterLink' => $request->twitterLink,
            'email' => $request->email,
            'street' => $request->street,
            'city' => $request->city,
            'openTime' => $request->openTime,
            'closeTime' => $request->closeTime,
            'openDay' => $request->openDay,
            'closeDay' => $request->closeDay
        ];


        try {
            Validator::validateRequestData($credentials, [
                'name' => 'nullable|string',
                'subname' => 'nullable|string',
                'phone' => 'nullable|string',
                'firm' => 'nullable|string',
                'location' => 'nullable|string',
                'facebookLink' => 'nullable|string',
                'instagramLink' => 'nullable|string',
                'twitterLink' => 'nullable|string',
                'email' => 'nullable|email',
                'street' => 'nullable|string',
                'city' => 'nullable|string',
                'openTime' => 'nullable|string',
                'closeTime' => 'nullable|string',
                'openDay' => 'nullable|string',
                'closeDay' => 'nullable|string'
            ]);
        } catch (Exception $exc) {
            throw new BadCredentialsException($exc->getMessage());
        }

        return $this->credentialsService->setCredentials($credentials);
    }
}
