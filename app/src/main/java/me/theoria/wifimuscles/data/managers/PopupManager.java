package me.theoria.wifimuscles.data.managers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import me.theoria.wifimuscles.R;

public class PopupManager {

    private Context context;

    public PopupManager(Context context) {
        this.context = context;
    }

    /**
     * Displays a popup window for a given layout.
     *
     * @param anchorView The view to anchor the popup to
     * @param layoutResId The layout resource ID to inflate for the popup
     */
    public void showPopup(View anchorView, int layoutResId) {
        // Inflate the popup layout using the provided layout resource ID
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(layoutResId, null);

        // Create the PopupWindow
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true // focusable
        );

        // Optional: Allow dismiss on outside touch
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Show the popup anchored below the view
        popupWindow.showAsDropDown(anchorView, 0, 0);
    }

    /**
     * Shows the WiFi level popup with a specific layout
     * @param anchorView the View to anchor the popup to
     */
    public void wifiLevelPopup(View anchorView) {
        showPopup(anchorView, R.layout.popup_wifi_signal);
    }

    /**
     * Shows the Info popup with a specific layout
     * @param anchorView the View to anchor the popup to
     */
    public void infoPopup(View anchorView) {
        showPopup(anchorView, R.layout.popup_wifi_signal);
    }

    // Additional methods for other popups can be added similarly
}
