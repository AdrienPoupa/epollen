<?php

namespace App\Http\Controllers\Api;

use App\Department;
use App\Http\Controllers\Controller;
use App\Risk;
use App\Tree;

class TreeController extends Controller {
    /**
     *
     * @return \Illuminate\Http\Response
     * @param $tree_id int
     * @param $department_id string
     */
    public function getTreeDepartment($tree_id, $department_id) {
        $header = array (
            'Content-Type' => 'application/json; charset=UTF-8',
            'charset' => 'utf-8'
        );

        try {
            $departments_id = explode('-', $department_id);
            $departments = Department::WhereIn('id', $departments_id)->get();
            $tree = Tree::Where('id', $tree_id)->first();
            $risks = Risk::WhereIn('department_id', $departments_id)->where('tree_id', $tree->id)->get();

            $departmentJson = [];
            $riskJson = null;

            foreach ($departments as $department) {
                array_push($departmentJson, [
                    'id' => $department->id,
                    'name' => $department->name,
                    'number' => $department->code
                ]);
            }

            foreach ($risks as $risk) {
                if(!is_array($riskJson)){
                    $riskJson = [
                        'name' => $risk->tree->name,
                        'number' => $risk->tree->code,
                        'risk' => 0
                    ];
                }

                $riskJson['risk'] += ($risk->risk / count($departments_id));
            }

            return response()->json([
                'return' => 'OK',
                'departments' => [
                    $departmentJson
                ],
                'tree' => [
                    'id' => $tree->id,
                    'name' => $tree->name,
                    'number' => $tree->code
                ],
                'risk' => $riskJson
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
