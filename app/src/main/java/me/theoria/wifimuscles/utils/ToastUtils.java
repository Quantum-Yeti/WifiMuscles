package me.theoria.wifimuscles.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtils is a reusable utility class for displaying common toast
 * messages throughout the app which enables the removal of
 * messy logic directly in fragment or activities.
 */
public class ToastUtils {

    /**
     * Method to display a short toast.
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to display a long toast.
     *
     * @param context
     * @param message
     */
    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Method to display a short toast for the Wifi signal level.
     *
     * @param context
     * @param level
     */
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
