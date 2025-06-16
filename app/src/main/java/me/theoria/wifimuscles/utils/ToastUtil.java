package me.theoria.wifimuscles.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showToastForLevel(Context context, int level) {
        String message = "";
        switch (level) {
            case 5:
                message = "Excellent coverage!";
                break;
            case 4:
                message = "Good coverage!";
                break;
            case 3:
                message = "Consider placing an extender here!";
                break;
            case 2:
                message = "Add an extender slightly closer to the router!";
                break;
            case 1:
                message = "Unusable WiFi reception!";
                break;
        }
        showToast(context, message);
    }
}
