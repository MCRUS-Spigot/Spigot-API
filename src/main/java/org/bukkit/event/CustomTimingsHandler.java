package org.bukkit.event;


import org.bukkit.Bukkit;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Extends RegisteredListener to include timing information
 */
public class CustomTimingsHandler {

    private final String name;
    public int count = 0;
    public long totalTime = 0;
    long start = 0;

    public static ArrayList<CustomTimingsHandler> allList = new ArrayList<CustomTimingsHandler>();
    public CustomTimingsHandler(String name) {
        this.name = name;
        allList.add(this);
    }

    static public void printTimings(PrintStream printStream) {
        printStream.println("Minecraft - ** indicates it's already counted by another timing");
        for (CustomTimingsHandler t : allList) {
            long time = t.totalTime;
            int count = t.count;
            if (count == 0) continue;
            long avg = time / count;

            printStream.println("    " + t.name + " Time: " + time + " Count: " + count + " Avg: " + avg);
        }
    }

    static public void reload() {
        if (!Bukkit.getServer().getPluginManager().useTimings()) return;
        for (CustomTimingsHandler t : allList) {
            t.reset();
        }
    }

    public void startTiming() {
        if (!Bukkit.getServer().getPluginManager().useTimings()) return;
        start = System.nanoTime();
    }

    public void stopTiming() {
        if (!Bukkit.getServer().getPluginManager().useTimings()) return;
        totalTime += System.nanoTime() - start;
        count++;
    }

    public void reset() {
        count = 0;
        totalTime = 0;
    }
}

