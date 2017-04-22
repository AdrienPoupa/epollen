<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

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
