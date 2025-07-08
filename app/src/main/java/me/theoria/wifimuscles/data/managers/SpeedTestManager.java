package me.theoria.wifimuscles.data.managers;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class SpeedTestManager {

    private final Activity activity;
    private final TextView speedTestResult;
    private final ProgressBar speedTestProgressBar;

    public SpeedTestManager(Activity activity, TextView speedTestResult, ProgressBar speedTestProgressBar) {
        this.activity = activity;
        this.speedTestResult = speedTestResult;
        this.speedTestProgressBar = speedTestProgressBar;
    }

    public void runSpeedTest() {
        speedTestResult.setText("Testing...");
        speedTestProgressBar.setVisibility(View.VISIBLE); // Show spinner

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                URL url = new URL("https://speed.hetzner.de/100KB.bin");
                long startTime = System.currentTimeMillis();

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setUseCaches(false);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(10000);

                InputStream input = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                long totalBytesRead = 0;

                while ((bytesRead = input.read(buffer)) != -1) {
                    totalBytesRead += bytesRead;
                }
                input.close();

                long endTime = System.currentTimeMillis();
                long timeTakenMillis = endTime - startTime;

                double speedMbps = (totalBytesRead * 8.0) / (timeTakenMillis / 1000.0) / 1_000_000.0;

                activity.runOnUiThread(() -> {
                    speedTestProgressBar.setVisibility(View.GONE);
                    speedTestResult.setText(String.format("Download speed: %.2f Mbps", speedMbps));
                });

            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> {
                    speedTestProgressBar.setVisibility(View.GONE);
                    speedTestResult.setText("Speed test failed");
                });
            }
        });
    }
}
