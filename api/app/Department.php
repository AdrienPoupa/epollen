<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * App\Department
 *
 * @mixin \Eloquent
 * @property int $id
 * @property string $name
 * @property string $number
 * @method static \Illuminate\Database\Query\Builder|\App\Department whereId($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Department whereName($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Department whereNumber($value)
 * @property string $code
 * @method static \Illuminate\Database\Query\Builder|\App\Department whereCode($value)
 */
class Department extends Model
{
    protected $fillable = [
        'name', 'number', 'code'
    ];
}
