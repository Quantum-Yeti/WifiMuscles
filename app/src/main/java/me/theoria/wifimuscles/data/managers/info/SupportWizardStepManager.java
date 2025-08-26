package me.theoria.wifimuscles.data.managers.info;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.SupportWizardModel;

public class SupportWizardStepManager {

    public static List<SupportWizardModel> getWizardSteps(Context context) {
        List<SupportWizardModel> wizardSteps = new ArrayList<>();

        wizardSteps.add(new SupportWizardModel(
                "Wi-Fi Status",
                "Are you connected to a Wi-Fi network?",
                "Yes", "No",
                1,2,
                R.drawable.icon_wifi
        ));

        wizardSteps.add(new SupportWizardModel(
                "Restart Router",
                "Try restarting your router. Did it help?",
                "Yes", "No",
                3,5,
                R.drawable.icon_router
        ));

        wizardSteps.add(new SupportWizardModel(
                "Call ISP",
                "Contact your internet provider if issues persist.",
                "Restart Wizard", "Exit",
                0,-1,
                R.drawable.icon_agent
        ));


        return wizardSteps;
    }

}
