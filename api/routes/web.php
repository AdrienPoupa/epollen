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

Route::group(['prefix' => 'api'], function () {
    Route::get('department', 'Api\DepartmentController@getAll')
        ->name('api-department-all');

    Route::get('department/{department_id}', 'Api\DepartmentController@getDepartments')
        ->name('api-department-get');

    Route::get('tree/{tree_id}/{department_id}', 'Api\TreeController@getTreeDepartment')
        ->name('api-tree-get');
});

Route::get('', 'HomeController@home')
    ->name('home');

Route::any('update-data', 'CronController@updateData')
    ->name('cron-update-data');