<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

/**
 * App\Tree
 *
 * @mixin \Eloquent
 * @property int $id
 * @property string $name
 * @property string $number
 * @method static \Illuminate\Database\Query\Builder|\App\Tree whereId($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Tree whereName($value)
 * @method static \Illuminate\Database\Query\Builder|\App\Tree whereNumber($value)
 */
class Tree extends Model
{
    protected $fillable = [
        'name', 'number'
    ];
}
