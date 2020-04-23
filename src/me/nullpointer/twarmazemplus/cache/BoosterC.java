package me.nullpointer.twarmazemplus.cache;

import me.nullpointer.twarmazemplus.utils.armazem.supliers.Booster;

import java.util.ArrayList;
import java.util.List;

public class BoosterC {

    public static List<Booster> boosters = new ArrayList<>();

    public static void put(Booster booster) {
        boosters.add(booster);
    }

    public static void remove(Booster b) {
        boosters.remove(b);
    }

    public static void remove(String key) {
        remove(get(key));
    }

    public static boolean has(String key) {
        return boosters.stream().anyMatch(booster -> booster.getKey().equalsIgnoreCase(key));
    }

    public static Booster get(String key) {
        return boosters.stream().filter(booster -> booster.getKey().equalsIgnoreCase(key)).findFirst().orElseGet(() -> boosters.get(0));
    }
}
