package me.theoria.wifimuscles.utils;

import java.util.function.Consumer;

public class PingUtil {

    public static void ping(String ip, Consumer<String> callBack) {
        new Thread(() -> {
            try {
                long startTime = System.nanoTime();
                Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -W 1 " + ip);
                int exit = process.waitFor();
                long endTime = System.nanoTime();

                if (exit == 0) {
                    long latencyMs = (endTime - startTime) / 1_000_000;
                    callBack.accept(latencyMs + " ms");
                } else {
                    callBack.accept("Timeout");
                }
            } catch (Exception e) {
                callBack.accept("Error");
            }
        }).start();
    }
}
