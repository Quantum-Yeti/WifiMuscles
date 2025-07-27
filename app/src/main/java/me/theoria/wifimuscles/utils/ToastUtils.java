package me.theoria.wifimuscles.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

/**
 * ToastUtils is a reusable utility class for displaying common toast
 * messages throughout the app which enables the removal of
 * messy logic directly in fragment or activities.
 */
public class ToastUtils {

    /**
     * Method to display a short toast.
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method to display a long toast.
     */
    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Method to display a short toast for the Wifi signal level.
     */
    public static void snackBarExtenderNotice(View view, int level) {
        String message = "";
        switch (level) {
            case 5:
                message = "Excellent and ideal coverage!";
                break;
            case 4:
                message = "Good coverage!";
                break;
            case 3:
                message = "Consider using an extender!";
                break;
            case 2:
                message = "Weak signal!";
                break;
            case 1:
                message = "Unusable WiFi reception!";
                break;
        }

        if (view !=null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
