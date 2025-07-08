package me.theoria.wifimuscles.data.managers;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.TooltipCompat;

import me.theoria.wifimuscles.R;

public class PopupManager {

    private final Context context;

    public PopupManager(Context context) {
        this.context = context;
    }

    /**
     * Shows a tooltip with a given message.
     *
     * @param anchorView The view to anchor the tooltip to
     * @param message    The message to display
     */
    public void showTooltip(View anchorView, String message) {
        TooltipCompat.setTooltipText(anchorView, message);
    }

    public void wifiLevelPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.wifi_signal_popup));
    }

    public void capabilitiesPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.capability_popup_txt));
    }

    public void channelWidthPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.channel_width_txt));
    }

    public void centerFreq0Popup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.center_freq_0_txt));
    }

    public void centerFreq1Popup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.center_freq_1_txt));
    }

    public void passpointPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.passpoint_txt));
    }

    public void responderPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.responder_txt));
    }

    public void downstreamPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.downstream_txt));
    }

    public void upstreamPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.upstream_txt));
    }

    public void gatewayPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.gateway_txt));
    }

    public void netmaskPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.netmask_txt));
    }

    public void dns1Popup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.dns1_txt));
    }

    public void dns2Popup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.dns2_txt));
    }

    public void leasePopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.lease_txt));
    }

    public void transportPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.transport_txt));
    }

    public void internetPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.internet_txt));
    }

    public void validationPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.validated_txt));
    }

    public void meteredPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.metered_txt));
    }

    public void advancedPopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.description_subHeader));
    }

    public void interferencePopup(View anchorView) {
        showTooltip(anchorView, context.getString(R.string.interference_txt));
    }
}
