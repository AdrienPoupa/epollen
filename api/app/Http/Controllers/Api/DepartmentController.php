<?php

namespace App\Http\Controllers\Api;

use App\Department;
use App\Http\Controllers\Controller;
use App\Risk;
use Illuminate\Support\Facades\DB;

class DepartmentController extends Controller {
    /**
     * Get all departments
     * @return \Illuminate\Http\Response
     */
    public function getAll() {
        $header = array (
            'Content-Type' => 'application/json; charset=UTF-8',
            'charset' => 'utf-8'
        );

        try {
            $departments = Department::all();
            $departmentJson = [];

            foreach ($departments as $department) {
                //$risks = Risk::Where('department_id', $department->id)->get();
                $risk = Risk::find(DB::table('risks')->max('risk'));

                array_push($departmentJson, [
                    'id' => $department->id,
                    'name' => $department->name,
                    'number' => $department->number,
                    'risk' => $risk->risk,
                    'color' => call_user_func(function() use ($risk) {
                        switch($risk->risk) {
                            case 0:
                                return 'white';
                                break;

                            case 1:
                                return 'green-1';
                                break;

                            case 2:
                                return 'green-2';
                                break;

                            case 3:
                                return 'yellow';
                                break;

                            case 4:
                                return 'orange';
                                break;

                            case '5':
                                return 'red';
                                break;

                            default:
                                return 'white';
                                break;
                        }
                    })
                ]);
            }

            return response()->json([
                'return' => 'OK',
                'departments' => $departmentJson
            ], 200, $header, JSON_UNESCAPED_UNICODE);
        }
        catch(\Exception $e){
            return response()->json([
                'return' => '500',
                'error' => 'Server'
            ], 200, $header, JSON_UNESCAPED_UNICODE);
        }
    }

    /**
     * Get one or several departments data
     * @param $department_id
     * @return \Illuminate\Http\Response
     */
    public function getDepartments($department_id) {
        $header = array (
            'Content-Type' => 'application/json; charset=UTF-8',
            'charset' => 'utf-8'
        );

        try {
            $departments_id = explode('-', $department_id);
            $departments = Department::WhereIn('id', $departments_id)->get();
            $risks = Risk::WhereIn('department_id', $departments_id)->get();

            $departmentJson = [];
            $riskData = [];
            $riskJson = [];

            foreach ($departments as $department) {
                array_push($departmentJson, [
                    'id' => $department->id,
                    'name' => $department->name,
                    'number' => $department->number
                ]);
            }

            foreach ($risks as $risk) {
                if(!array_key_exists($risk->tree_id, $riskData)) {
                    $riskData[$risk->tree_id] = [
                        'name' => $risk->tree->name,
                        'risk' => 0
                    ];
                }

                $riskData[$risk->tree_id]['risk'] += round($risk->risk / count($departments_id), 2);
            }

            foreach ($riskData as $riskDatum) {
                array_push($riskJson, [
                    'name' => $riskDatum['name'],
                    'risk' => $riskDatum['risk']
                ]);
            }

            return response()->json([
                'return' => 'OK',
                'departments' => $departmentJson,
                'risks' => $riskJson
            ], 200, $header, JSON_UNESCAPED_UNICODE);
        }
        catch(\Exception $e){
            return response()->json([
                'return' => '500',
                'error' => 'Server'
            ], 200, $header, JSON_UNESCAPED_UNICODE);
        }
    }
}
