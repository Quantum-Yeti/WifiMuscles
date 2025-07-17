package me.theoria.wifimuscles.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

public class ExternalUtil {

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

    public static void fetchPublicIP(Consumer<String> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                URL url = new URL("https://api.ipify.org");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String publicIP = br.readLine();
                br.close();

                callback.accept(publicIP);
            } catch (IOException e) {
                callback.accept("Unavailable");
            }
        });
    }

}
