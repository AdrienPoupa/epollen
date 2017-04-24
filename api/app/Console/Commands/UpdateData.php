<?php

namespace App\Console\Commands;

use App\Department;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\DB;

class UpdateData extends Command
{
    /**
     * The name and signature of the console command.
     * @var string
     */
    protected $signature = 'update:data';

    /**
     * The console command description.
     * @var string
     */
    protected $description = 'Update data from pollens.fr';

    /**
     * Create a new command instance.
     */
    public function __construct()
    {
        parent::__construct();
    }

    /**
     * Execute the console command.
     * @return mixed
     */
    public function handle()
    {
        $departments = Department::all();
        DB::table('risks')->truncate();

        foreach ($departments as $department) {
            $img = imagecreatefromgif("http://internationalragweedsociety.org/vigilance/d%20" . $department->number . ".gif");
            $i = 0;
            $x = 115;
            $y = 47;

            while ($i != 19) {
                $rgb = imagecolorat($img, $x, $y);
                $colors = imagecolorsforindex($img, $rgb);

                if ($colors['red'] == 255 && $colors['green'] == 255 && $colors['blue'] == 255) {
                    $pollens[$i]["color"] = "brown";
                    $pollens[$i]["risk"] = 0;
                }
                elseif ($colors['red'] == 0 && $colors['green'] == 255 && $colors['blue'] == 0) {
                    $pollens[$i]["color"] = "rgb(0,255,0)";
                    $pollens[$i]["risk"] = 1;
                }
                elseif ($colors['red'] == 0 && $colors['green'] == 176 && $colors['blue'] == 80) {
                    $pollens[$i]["color"] = "rgb(0,176,80)";
                    $pollens[$i]["risk"] = 2;
                }
                elseif ($colors['red'] == 255 && $colors['green'] == 255 && $colors['blue'] == 0) {
                    $pollens[$i]["color"] = "rgb(255,255,0)";
                    $pollens[$i]["risk"] = 3;
                }
                elseif ($colors['red'] == 247 && $colors['green'] == 150 && $colors['blue'] == 70) {
                    $pollens[$i]["color"] = "rgb(247,150,70)";
                    $pollens[$i]["risk"] = 4;
                }
                else {
                    $pollens[$i]["color"] = "red";
                    $pollens[$i]["risk"] = 5;
                }

                DB::table('risks')->insert(
                    [
                        'risk' => $pollens[$i]["risk"],
                        'department_id' => $department->id,
                        'tree_id' => $i + 1
                    ]
                );

                $y = $y + 20;
                $i++;
            }
        }
    }
}
