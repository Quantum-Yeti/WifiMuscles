package me.theoria.wifimuscles.data.managers.info;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import me.theoria.wifimuscles.R;

public class ChartPopupManager {

    private final Context context;

    public ChartPopupManager(Context context) {
        this.context = context;
    }

    public void showChartPopup(View anchorView, String title, String description) {
        View chartPopupView = LayoutInflater.from(context).inflate(R.layout.info_chart_popup, null);

        TextView titleView = chartPopupView.findViewById(R.id.popupTitle);
        TextView descView = chartPopupView.findViewById(R.id.popupDescription);

        titleView.setText(title);
        descView.setText(description);

        PopupWindow popupWindow = new PopupWindow(
                chartPopupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);

        // Position popup above or below based on space
        chartPopupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = chartPopupView.getMeasuredHeight();
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);

        boolean showAbove = location[1] > popupHeight;

        popupWindow.showAsDropDown(anchorView, 0, showAbove ? -popupHeight - anchorView.getHeight() : 0, Gravity.START);
    }
}
