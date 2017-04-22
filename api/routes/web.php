<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::group(['prefix' => 'api', 'middleware' => 'auth.api'], function () {
    Route::get('department', 'Api\DepartmentController@getAll')
        ->name('api-department-all');

    Route::get('department/{department_id}', 'Api\DepartmentController@getDepartment')
        ->name('api-department-get');

    Route::get('tree/{tree_id}/{department_id}', 'Api\TreeController@getTreeDepartment')
        ->name('api-tree-get');
});