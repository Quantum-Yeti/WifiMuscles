package me.theoria.wifimuscles.data.managers.info;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.SupportModel;

public class SupportTipManager {

    public static List<SupportModel> getSupportTips(Context context) {
        List<SupportModel> tips = new ArrayList<>();
        tips.add(new SupportModel(
                context.getString(R.string.rebootTitle),
                context.getString(R.string.rebootDescription),
                context.getString(R.string.rebootInstructions)
        ));
        /*tips.add(new SupportModel(
                context.
        ));*/
        /*tips.add(new SupportModel(
                "Forget & Reconnect to Network",
                "Go to Wi-Fi settings and reconnect.",
                //"In your device settings, select the network, choose 'Forget', then rejoin with the password."
        ));
        tips.add(new SupportModel(
                "Switch Frequency Band",
                "Try switching between 2.4GHz and 5GHz.",
                //"5GHz is faster but has shorter range. Switch based on your distance from the router."
        ));
        tips.add(new SupportModel(
                "Reduce Interference",
                "Turn off unused Wi-Fi devices or move away from microwave ovens.",
                //"Many devices emit signals that interfere with Wi-Fi. Limit them if possible."
        ));*/
        return tips;
    }

}
