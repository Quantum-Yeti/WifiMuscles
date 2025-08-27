package me.theoria.wifimuscles.data.managers.info;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.data.model.SupportWizardModel;

public class SupportWizardManager {

    /**
     * Method to construct the troubleshooting decision tree as an array list.
     * @param context is used to get application resources
     * @return wizardSteps: Returns the current step index
     */
    public static List<SupportWizardModel> getWizardSteps(Context context) {
        List<SupportWizardModel> wizardSteps = new ArrayList<>();

        // Step 0 - Start screen
        wizardSteps.add(new SupportWizardModel(
                "Troubleshoot Your Connection",
                "Tap start to begin.",
                "Start", "",
                1, -1,
                R.drawable.icon_wizard
        ));

        // Step 1 - Are you connected to Wi-Fi?
        wizardSteps.add(new SupportWizardModel(
                "Wi-Fi Status",
                "Are you connected to a Wi-Fi network?",
                "Yes", "No",
                2, 10,
                R.drawable.icon_wifi
        ));

        // Step 2 - What issue are you experiencing?
        wizardSteps.add(new SupportWizardModel(
                "Experiencing Issues",
                "What kind of issue are you experiencing?",
                "Slow Speeds", "Intermittency",
                3, 6,
                R.drawable.icon_wifi
        ));

        // Step 3 - Slow Speeds intro
        wizardSteps.add(new SupportWizardModel(
                "Slow Speeds",
                "Let's troubleshoot slow internet speeds.",
                "Next", "Exit",
                4, -1,
                R.drawable.icon_speed
        ));

        // Step 4 - Restart Router slow speeds
        wizardSteps.add(new SupportWizardModel(
                "Restart Router",
                "Please unplug your router's power cord, wait 30 seconds and plug back in. Did it help?",
                "Yes", "No",
                5, 18,
                R.drawable.icon_router
        ));

        // Step 5 - Slow Speeds additional info
        wizardSteps.add(new SupportWizardModel(
                context.getString(R.string.slowSpeeds_title),
                context.getString(R.string.slowSpeeds_description),
                "Restart Wizard", "Exit",
                0, -1,
                R.drawable.icon_speed
        ));

        // Step 6 - Intermittent Wi-Fi intro
        wizardSteps.add(new SupportWizardModel(
                "Intermittent Wi-Fi",
                "Let's troubleshoot intermittent Wi-Fi issues.",
                "Next", "Exit",
                7, -1,
                R.drawable.icon_info
        ));

        // Step 7 - Check Modem Lights (Intermittency branch)
        wizardSteps.add(new SupportWizardModel(
                "Check Modem Lights",
                "Are the modem lights blinking or off?",
                "Blinking", "Off",
                8, 9,
                R.drawable.icon_router
        ));

        // Step 8 - Guide to reboot modem/router
        wizardSteps.add(new SupportWizardModel(
                "Reboot Modem",
                "We’ll walk you through rebooting your modem and router.",
                "Start", "Exit",
                19, -1,
                R.drawable.icon_router
        ));

        // Step 9 - Modem offline advice
        wizardSteps.add(new SupportWizardModel(
                "Modem Issue",
                "Your modem may be offline. Contact your ISP for further assistance.",
                "Restart Wizard", "Exit",
                0, -1,
                R.drawable.icon_agent
        ));

        // Step 10 - Device type (No Wi-Fi connection branch)
        wizardSteps.add(new SupportWizardModel(
                "Device Type",
                "What device are you trying to connect?",
                "TV", "Computer",
                11, 12,
                R.drawable.icon_devices
        ));

        // Step 11 - Smart TV Tips
        wizardSteps.add(new SupportWizardModel(
                "Smart TV Tips",
                "Try restarting the TV, checking distance from the router, and reconnecting to Wi-Fi.",
                "Restart Wizard", "Exit",
                0, -1,
                R.drawable.icon_tv
        ));

        // Step 12 - Computer type (Windows/Mac)
        wizardSteps.add(new SupportWizardModel(
                "Computer Type",
                "What kind of computer are you using?",
                "Windows", "Mac",
                13, 14,
                R.drawable.icon_computer
        ));

        // Step 13 - Windows Wi-Fi fix
        wizardSteps.add(new SupportWizardModel(
                "Windows Wi-Fi Fix",
                "Forget the Wi-Fi network and reconnect. Reboot your PC if needed.",
                "Restart Wizard", "Exit",
                0, -1,
                R.drawable.icon_windows
        ));

        // Step 14 - Mac Wi-Fi fix
        wizardSteps.add(new SupportWizardModel(
                "Mac Wi-Fi Fix",
                "Go to System Settings → Network → Forget and reconnect to your Wi-Fi.",
                "Restart Wizard", "Exit",
                0, -1,
                R.drawable.icon_mac
        ));

        // Step 15 - Phone or Tablet selection (not yet connected devices)
        wizardSteps.add(new SupportWizardModel(
                "Phone or Tablet?",
                "Are you using a phone or a tablet?",
                "Phone", "Tablet",
                16, 17,
                R.drawable.icon_devices
        ));

        // Step 16 - Phone Wi-Fi fix steps
        wizardSteps.add(new SupportWizardModel(
                "Phone Wi-Fi Fix",
                "Try toggling airplane mode, forgetting the network, or restarting the phone.",
                "Restart Wizard", "Exit",
                0, -1,
                R.drawable.icon_phone
        ));

        // Step 17 - Tablet Wi-Fi fix steps
        wizardSteps.add(new SupportWizardModel(
                "Tablet Wi-Fi Fix",
                "Ensure Wi-Fi is enabled, restart the tablet, and move closer to the router.",
                "Restart Wizard", "Exit",
                0, -1,
                R.drawable.icon_tablet
        ));

        // Step 18 - Reboot network devices intro (Slow speeds branch no)
        wizardSteps.add(new SupportWizardModel(
                "Reboot Network Devices",
                "Let’s walk you through rebooting your modem and router safely.",
                "Start", "Exit",
                19, -1,
                R.drawable.icon_router
        ));

        // Step 19 - Unplug devices
        wizardSteps.add(new SupportWizardModel(
                "Unplug Devices",
                "Unplug both your modem and router from the power source.",
                "Done", "Exit",
                20, -1,
                R.drawable.icon_power
        ));

        // Step 20 - Wait before reconnecting
        wizardSteps.add(new SupportWizardModel(
                "Wait Before Reconnecting",
                "Wait at least 30 seconds before plugging anything back in.",
                "Done", "Exit",
                21, -1,
                R.drawable.icon_avg_time
        ));

        // Step 21 - Plug in modem first
        wizardSteps.add(new SupportWizardModel(
                "Plug in Modem",
                "Plug in your modem first. Wait until the lights are stable.",
                "Done", "Exit",
                22, -1,
                R.drawable.icon_mode
        ));

        // Step 22 - Plug in router
        wizardSteps.add(new SupportWizardModel(
                "Plug in Router",
                "Now plug in your router. Wait for all lights to turn on and stabilize.",
                "Done", "Exit",
                23, -1,
                R.drawable.icon_router
        ));

        // Step 23 - Check connection after reboot
        wizardSteps.add(new SupportWizardModel(
                "Check Connection",
                "Are you now able to connect to your Wi-Fi network?",
                "Yes", "No",
                -1, 2, // Yes goes back to slow speeds vs intermittency question
                R.drawable.icon_wifi
        ));

        // --- Additional detailed Phone Wi-Fi troubleshooting steps:

        // Step 24 - Wi-Fi enabled?
        wizardSteps.add(new SupportWizardModel(
                "Wi-Fi Enabled?",
                "Is Wi-Fi turned on in your phone's settings?",
                "Yes", "No",
                25, 26,
                R.drawable.icon_wifi
        ));

        // Step 25 - Airplane mode off?
        wizardSteps.add(new SupportWizardModel(
                "Airplane Mode Off?",
                "Is Airplane Mode turned off?",
                "Yes", "No",
                27, 28,
                R.drawable.icon_airplane
        ));

        // Step 26 - Turn on Wi-Fi instructions
        wizardSteps.add(new SupportWizardModel(
                "Turn On Wi-Fi",
                "Please enable Wi-Fi in your phone settings, then try connecting to your network.",
                "Done", "Exit",
                24, -1,
                R.drawable.icon_wifi
        ));

        // Step 27 - Forget & reconnect network
        wizardSteps.add(new SupportWizardModel(
                "Forget & Reconnect",
                "Try forgetting the network and reconnecting manually from Wi-Fi settings.",
                "Done", "Exit",
                29, -1,
                R.drawable.icon_wifi
        ));

        // Step 28 - Disable airplane mode instructions
        wizardSteps.add(new SupportWizardModel(
                "Disable Airplane Mode",
                "Please disable Airplane Mode and then try connecting again.",
                "Done", "Exit",
                27, -1,
                R.drawable.icon_airplane
        ));

        // Step 29 - Restart phone
        wizardSteps.add(new SupportWizardModel(
                "Restart Phone",
                "Try restarting your phone. This often resolves Wi-Fi connection issues.",
                "Restarted", "Exit",
                30, -1,
                R.drawable.icon_restart
        ));

        // Step 30 - Final check after restart
        wizardSteps.add(new SupportWizardModel(
                "Final Check",
                "Are you now able to connect to your Wi-Fi network?",
                "Yes", "No",
                -1, 10,
                R.drawable.icon_wifi
        ));

        // Step 31 - Weak Signal Introduction
        wizardSteps.add(new SupportWizardModel(
                "Weak Signal",
                "Let's troubleshoot poor Wi-Fi signal strength in your home.",
                "Start", "Exit",
                32, -1,
                R.drawable.icon_chart
        ));

        // Step 32 - Are you far from the router?
        wizardSteps.add(new SupportWizardModel(
                "Distance Check",
                "Are you experiencing weak signal far away from your router?",
                "Yes", "No",
                33, 34,
                R.drawable.icon_distance
        ));

        // Step 33 - Move closer to router
        wizardSteps.add(new SupportWizardModel(
                "Try Moving Closer",
                "Try moving closer to your router and check signal strength again.",
                "Done", "Exit",
                35, -1,
                R.drawable.icon_closer
        ));

        // Step 34 - Obstructions
        wizardSteps.add(new SupportWizardModel(
                "Check for Obstructions",
                "Thick walls, appliances, or floors can weaken signal. Can you reposition your router?",
                "Yes", "No",
                35, 36,
                R.drawable.icon_fence
        ));

        // Step 35 - Signal improved?
        wizardSteps.add(new SupportWizardModel(
                "Signal Check",
                "Has the signal strength improved after moving or adjusting the router?",
                "Yes", "No",
                -1, 36,
                R.drawable.icon_network_check
        ));

        // Step 36 - Suggest Mesh or Extender
        wizardSteps.add(new SupportWizardModel(
                "Wi-Fi Extender",
                "Consider a mesh Wi-Fi system or extender to improve coverage across your space.",
                "Restart Wizard", "Exit",
                0, -1,
                R.drawable.icon_mesh
        ));


        return wizardSteps;
    }

}
