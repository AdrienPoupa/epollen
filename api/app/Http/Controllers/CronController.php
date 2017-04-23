<?php

namespace App\Http\Controllers;

use App\Department;

class CronController extends Controller {
    /**
     *
     * @return \Illuminate\Http\Response
     * @param int
     */
    public function updateData() {

        $departments = Department::all();

        foreach ($departments as $department) {
            $i = 0;
            $im = imagecreatefromgif("http://internationalragweedsociety.org/vigilance/d%20" . $department->number . ".gif");
            $x = 115;
            $y = 47;

            while ($i != 19) {
                $rgb = imagecolorat($im, $x, $y);
                $colors = imagecolorsforindex($im, $rgb);

                $r = ($rgb >> 16) & 0xFF;
                $g = ($rgb >> 8) & 0xFF;
                $b = $rgb & 0xFF;

                if ($colors['red'] == 255 && $colors['green'] == 255 && $colors['blue'] == 255) {
                    $pollens[$i]["color"] = "brown";
                    $pollens[$i]["class"] = 0;
                }
                elseif ($colors['red'] == 0 && $colors['green'] == 255 && $colors['blue'] == 0) {
                    $pollens[$i]["color"] = "rgb(0,255,0)";
                    $pollens[$i]["class"] = 1;
                }
                elseif ($colors['red'] == 0 && $colors['green'] == 176 && $colors['blue'] == 80) {
                    $pollens[$i]["color"] = "rgb(0,176,80)";
                    $pollens[$i]["class"] = 2;
                }
                elseif ($colors['red'] == 255 && $colors['green'] == 255 && $colors['blue'] == 0) {
                    $pollens[$i]["color"] = "rgb(255,255,0)";
                    $pollens[$i]["class"] = 3;
                }
                elseif ($colors['red'] == 247 && $colors['green'] == 150 && $colors['blue'] == 70) {
                    $pollens[$i]["color"] = "rgb(247,150,70)";
                    $pollens[$i]["class"] = 4;
                }
                else {
                    $pollens[$i]["color"] = "red";
                    $pollens[$i]["class"] = 5;
                }

                $pollens[$i]['x'] = $x;
                $pollens[$i]['y'] = $y;

                $y = $y + 20;
                $i++;
            }

            $pollens[0]["name"] = "crupressacées";
            $pollens[1]["name"] = "noisetier";
            $pollens[2]["name"] = "aulne";
            $pollens[3]["name"] = "peuplier";
            $pollens[4]["name"] = "saule";
            $pollens[5]["name"] = "frene";
            $pollens[6]["name"] = "charme";
            $pollens[7]["name"] = "bouleau";
            $pollens[8]["name"] = "platane";
            $pollens[9]["name"] = "chene";
            $pollens[10]["name"] = "olivier";
            $pollens[11]["name"] = "tilleul";
            $pollens[12]["name"] = "chataigner";
            $pollens[13]["name"] = "rumex";
            $pollens[14]["name"] = "graminées";
            $pollens[15]["name"] = "plantain";
            $pollens[16]["name"] = "urticacées";
            $pollens[17]["name"] = "armoises";
            $pollens[18]["name"] = "ambroisies";

            var_dump($pollens);
            echo '<br/>';


            //affiche dirctement les donnees sur la page web
            $j = 0;

            while ($j != 19) {
                echo '<div style="color: ' . $pollens[$j]["color"] . '"> ' . $pollens[$j]["name"] . ' : ' . $pollens[$j]["class"] . '</div>';
                $j++;
            }
        }
    }
}
