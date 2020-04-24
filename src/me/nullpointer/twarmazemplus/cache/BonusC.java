package me.nullpointer.twarmazemplus.cache;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class BonusC {

    public static HashMap<String, Integer> bonus = Maps.newHashMap();

    public static void put(String permission, Integer discount) {
        bonus.put(permission, discount);
    }

    public static Integer get(Player p) {
        try {
            return bonus.get(bonus.keySet().stream().filter(p::hasPermission).findFirst().orElseGet(() -> bonus.keySet().stream().findFirst().get()));
        } catch (Exception e) {
            return 0;
        }
    }
}