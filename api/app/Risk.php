<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * App\Risk
 *
 * @property-read \App\Department $department
 * @property-read \App\Tree $tree
 * @mixin \Eloquent
 * @property int $id
 * @property float $risk
 * @property int $department_id
 * @property int $tree_id
 * @method static \Illuminate\Database\Query\Builder|\App\Risk whereDepartmentId($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Risk whereId($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Risk whereRisk($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Risk whereTreeId($value)
 */
class Risk extends Model
{
    protected $fillable = [
        'tree_id', 'department_id', 'risk'
    ];

    /**
     * Risk has one Department
     * Foreign key: department_id
     * @return \Illuminate\Database\Eloquent\Relations\BelongsTo
     */
    public function department()
    {
        return $this->belongsTo('App\Department');
    }

    /**
     * Risk has one tree
     * Foreign key: tree_id
     * @return \Illuminate\Database\Eloquent\Relations\BelongsTo
     */
    public function tree()
    {
        return $this->belongsTo('App\Tree');
    }
}
