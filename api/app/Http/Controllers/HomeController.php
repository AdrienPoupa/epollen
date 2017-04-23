<?php

namespace App\Http\Controllers;

class HomeController extends Controller {
    /**
     *
     * @return \Illuminate\Http\Response
     * @param int
     */
    public function home() {
        $header = array (
            'Content-Type' => 'application/json; charset=UTF-8',
            'charset' => 'utf-8'
        );

        return response()->json([
            'return' => 'OK'
        ], 200, $header, JSON_UNESCAPED_UNICODE);
    }
}
