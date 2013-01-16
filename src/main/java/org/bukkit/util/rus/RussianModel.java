package org.bukkit.util.rus;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;

/**
 * Created by Alex Bond
 * Date: 25.12.12
 * Time: 20:22
 */
public class RussianModel {
    public static String getGamemode(GameMode gm) {
        switch (gm.getValue()){
            case 0: return "Выживание";
            case 1: return "Творческий";
            case 2: return "Приключенческий";
        }
        return gm.toString();
    }

    public static String getDifficulty(Difficulty difficulty){
        switch (difficulty.getValue()){
            case 0: return "Мирно";
            case 1: return "Легко";
            case 2: return "Нормально";
            case 3: return "Сложно";
        }
        return difficulty.toString();
    }

}
