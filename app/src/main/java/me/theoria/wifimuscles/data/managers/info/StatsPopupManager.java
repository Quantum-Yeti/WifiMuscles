package me.theoria.wifimuscles.data.managers.info;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import me.theoria.wifimuscles.R;

public class StatsPopupManager {

    private final Context context;

    public StatsPopupManager(Context context) {
        this.context = context;
    }

    /**
     * Shows a tooltip with a given message.
     *
     * @param anchorView The view to anchor the tooltip to
     * @param message    The message to display
     */
    public void showInfoPopup(View anchorView, String message) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.info_popup, null);
        TextView textView = popupView.findViewById(R.id.infoPopup);
        textView.setText(message);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true // Focusable
        );

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setElevation(2f);

        if (anchorView.isLaidOut()) {
            popupWindow.showAsDropDown(anchorView, 0, 0, Gravity.START);
        } else {
            popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
        }
    }

    public void wifiLevelPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.wifi_signal_popup));
    }

    public void capabilitiesPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.capability_popup_txt));
    }

    public void channelWidthPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.channel_width_txt));
    }

    public void centerFreq0Popup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.center_freq_0_txt));
    }

    public void centerFreq1Popup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.center_freq_1_txt));
    }

    public void passpointPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.passpoint_txt));
    }

    public void responderPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.responder_txt));
    }

    public void downstreamPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.downstream_txt));
    }

    public void upstreamPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.upstream_txt));
    }

    public void gatewayPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.gateway_txt));
    }

    public void netmaskPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.netmask_txt));
    }

    public void dns1Popup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.dns1_txt));
    }

    public void dns2Popup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.dns2_txt));
    }

    public void leasePopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.lease_txt));
    }

    public void transportPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.transport_txt));
    }

    public void internetPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.internet_txt));
    }

    public void validationPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.validated_txt));
    }

    public void meteredPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.metered_txt));
    }

    public void advancedPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.description_subHeader));
    }

    public void interferencePopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.interference_txt));
    }

    public void ssidPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.ssid_txt));
    }

    public void standardPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.wifi_standard_popup));
    }

    public void vpnPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.vpn_txt));
    }

    public void linkSpeedPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.link_speed_txt));
    }

    public void maxLinkSpeedPopup(View anchorView) {
        showInfoPopup(anchorView, context.getString(R.string.max_link_speed_txt));
    }

}
