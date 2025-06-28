package me.theoria.wifimuscles.data.managers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import me.theoria.wifimuscles.R;

public class PopupManager {

    private final Context context;

    public PopupManager(Context context) {
        this.context = context;
    }

    /**
     * Displays a popup window for a given layout.
     *
     * @param anchorView The view to anchor the popup to
     */
    public void showPopup(View anchorView, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View popupView = inflater.inflate(R.layout.info_popup, null);

        TextView popupText = popupView.findViewById(R.id.infoPopup);
        popupText.setText(message);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAsDropDown(anchorView, 0, 0);
    }

    /**
     * Shows the popups with a specific layout
     * @param anchorView the View to anchor the popup to
     */
    public void wifiLevelPopup(View anchorView) {
        String message = context.getString(R.string.wifi_signal_popup);
        showPopup(anchorView, message);
    }

    public void capabilitiesPopup(View anchorView) {
        String message = context.getString(R.string.capability_popup_txt);
        showPopup(anchorView, message);
    }

    public void channelwidthPopup(View anchorView) {
        String message = context.getString(R.string.channel_width_txt);
        showPopup(anchorView, message);
    }

    public void centerfreq0Popup(View anchorView) {
        String message = context.getString(R.string.center_freq_0_txt);
        showPopup(anchorView, message);
    }

    public void centerfreq1Popup(View anchorView) {
        String message = context.getString(R.string.center_freq_1_txt);
        showPopup(anchorView, message);
    }

    public void passpointPopup(View anchorView) {
        String message = context.getString(R.string.passpoint_txt);
        showPopup(anchorView, message);
    }

    public void responderPopup(View anchorView) {
        String message = context.getString(R.string.responder_txt);
        showPopup(anchorView, message);
    }

    public void downstreamPopup(View anchorView) {
        String message = context.getString(R.string.downstream_txt);
        showPopup(anchorView, message);
    }

    public void upstreamPopup(View anchorView) {
        String message = context.getString(R.string.upstream_txt);
        showPopup(anchorView, message);
    }

    public void gatewayPopup(View anchorView) {
        String message = context.getString(R.string.gateway_txt);
        showPopup(anchorView, message);
    }

    public void netmaskPopup(View anchorView) {
        String message = context.getString(R.string.netmask_txt);
        showPopup(anchorView, message);
    }

    public void dns1Popup(View anchorView) {
        String message = context.getString(R.string.dns1_txt);
        showPopup(anchorView, message);
    }

    public void dns2Popup(View anchorView) {
        String message = context.getString(R.string.dns2_txt);
        showPopup(anchorView, message);
    }

    public void leasePopup(View anchorView) {
        String message = context.getString(R.string.lease_txt);
        showPopup(anchorView, message);
    }

    public void transportPopup(View anchorView) {
        String message = context.getString(R.string.transport_txt);
        showPopup(anchorView, message);
    }

    public void internetPopup(View anchorView) {
        String message = context.getString(R.string.internet_txt);
        showPopup(anchorView, message);
    }

    public void validationPopup(View anchorView) {
        String message = context.getString(R.string.validated_txt);
        showPopup(anchorView, message);
    }

    public void meteredPopup(View anchorView) {
        String message = context.getString(R.string.metered_txt);
        showPopup(anchorView, message);
    }

    public void advancedPopup(View anchorView) {
        String message = context.getString(R.string.description_subheader);
        showPopup(anchorView, message);
    }




}
