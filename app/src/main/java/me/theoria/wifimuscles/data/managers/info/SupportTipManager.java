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
                context.getString(R.string.rebootInstructions),
                R.drawable.icon_info
        ));

        tips.add(new SupportModel(
                context.getString(R.string.intermittency_title),
                context.getString(R.string.intermittency_description),
                context.getString(R.string.intermittency_instruction),
                R.drawable.icon_info

        ));
        tips.add(new SupportModel(
                context.getString(R.string.slowSpeeds_title),
                context.getString(R.string.slowSpeeds_description),
                context.getString(R.string.slowSpeeds_instruction),
                R.drawable.icon_speed
        ));
        /*tips.add(new SupportModel(
                "Reduce Interference",
                "Turn off unused Wi-Fi devices or move away from microwave ovens.",
                //"Many devices emit signals that interfere with Wi-Fi. Limit them if possible."
        ));*/
        return tips;
    }

}
