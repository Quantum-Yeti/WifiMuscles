package me.theoria.wifimuscles.feature.wizard;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import me.theoria.wifimuscles.R;
import me.theoria.wifimuscles.model.SupportWizardModel;

public class SupportWizardManager {

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
                2, 9,
                R.drawable.icon_wifi
        ));

        // Step 2 - What issue are you experiencing?
        wizardSteps.add(new SupportWizardModel(
                "Experiencing Issues",
                "What kind of issue are you experiencing?",
                "Slow Speeds", "More Issues",
                3, 36,
                R.drawable.icon_wifi
        ));

        // Step 3 - Slow Speeds intro
        wizardSteps.add(new SupportWizardModel(
                "Slow Speeds",
                "Let's troubleshoot slow internet speeds.",
                "Next", "Exit",
                4, 0,
                R.drawable.icon_speed
        ));

        // Step 4 - Restart Router slow speeds
        wizardSteps.add(new SupportWizardModel(
                context.getString(R.string.slowSpeeds_title),
                context.getString(R.string.slowSpeeds_description),
                "Restart Wizard", "Exit",
                0, 0,
                R.drawable.icon_speed
        ));

        // Step 5 - Intermittent Wi-Fi intro
        wizardSteps.add(new SupportWizardModel(
                "Intermittent Wi-Fi",
                "Let's troubleshoot intermittent Wi-Fi issues.",
                "Next", "Exit",
                6, 0,
                R.drawable.icon_info
        ));

        // Step 6 - Check Modem Lights
        wizardSteps.add(new SupportWizardModel(
                "Check Modem Lights",
                "Are the modem lights blinking or off?",
                "Blinking", "Off",
                7, 8,
                R.drawable.icon_router
        ));

        // Step 7 - Guide to reboot modem/router
        wizardSteps.add(new SupportWizardModel(
                "Reboot Modem",
                "We’ll walk you through rebooting your modem and router.",
                "Start", "Exit",
                18, 0,
                R.drawable.icon_router
        ));

        // Step 8 - Modem offline advice
        wizardSteps.add(new SupportWizardModel(
                "Modem Issue",
                "Your modem may be offline. Contact your ISP for further assistance.",
                "Restart Wizard", "Exit",
                0, 0,
                R.drawable.icon_agent
        ));

        // Step 9 - Device type (No Wi-Fi)
        wizardSteps.add(new SupportWizardModel(
                "Device Type",
                "What device are you trying to connect?",
                "TV/Streaming Device", "Computer or Mobile",
                10, 37,
                R.drawable.icon_devices
        ));

        // Step 10 - Smart TV Tips
        wizardSteps.add(new SupportWizardModel(
                "Smart TV/Streaming Device Tips",
                "Turn off then unplug the power cord to the TV, wait a minute and plug back in, finally check network connection.",
                "Restart Wizard", "Exit",
                0, 0,
                R.drawable.icon_tv
        ));

        // Step 11 - Computer type
        wizardSteps.add(new SupportWizardModel(
                "Computer Type",
                "What kind of computer are you using?",
                "Windows", "Mac",
                12, 13,
                R.drawable.icon_computer
        ));

        // Step 12 - Windows Wi-Fi fix
        wizardSteps.add(new SupportWizardModel(
                "Windows Wi-Fi Fix",
                "If everything else in your home is connected, your Wi-Fi is working. Try forgetting the Wi-Fi network and reconnect. Rebooting your PC is also suggested.",
                "Restart Wizard", "Exit",
                0, 0,
                R.drawable.icon_windows
        ));

        // Step 13 - Mac Wi-Fi fix
        wizardSteps.add(new SupportWizardModel(
                "Mac Wi-Fi Fix",
                "Go to System Settings → Network → Forget and reconnect to your Wi-Fi.",
                "Restart Wizard", "Exit",
                0, 0,
                R.drawable.icon_mac
        ));

        // Step 14 - Phone or Tablet selection
        wizardSteps.add(new SupportWizardModel(
                "Phone or Tablet?",
                "Are you using a phone or a tablet?",
                "Phone", "Tablet",
                15, 16,
                R.drawable.icon_devices
        ));

        // Step 15 - Phone Wi-Fi fix steps
        wizardSteps.add(new SupportWizardModel(
                "Phone Wi-Fi Fix",
                "Try toggling airplane mode, forgetting the network, or restarting the phone.",
                "More Help", "Exit",
                23, 0,
                R.drawable.icon_phone
        ));

        // Step 16 - Tablet Wi-Fi fix steps
        wizardSteps.add(new SupportWizardModel(
                "Tablet Wi-Fi Fix",
                "Ensure Wi-Fi is enabled, restart the tablet, and move closer to the router.",
                "Restart Wizard", "Exit",
                0, 0,
                R.drawable.icon_tablet
        ));

        // Step 17 - (Optional) Reboot network devices
        wizardSteps.add(new SupportWizardModel(
                "Reboot Network Devices",
                "Let’s walk you through rebooting your modem and router safely.",
                "Start", "Exit",
                18, 0,
                R.drawable.icon_router
        ));

        // Step 18 - Unplug devices
        wizardSteps.add(new SupportWizardModel(
                "Unplug Devices",
                "Unplug both your modem and router from the back of each device.",
                "Continue", "Exit",
                19, 0,
                R.drawable.icon_power
        ));

        // Step 19 - Wait before reconnecting
        wizardSteps.add(new SupportWizardModel(
                "Wait Before Reconnecting",
                "Important: Wait at least 30 seconds before plugging anything back in.",
                "Continue", "Exit",
                20, 0,
                R.drawable.icon_avg_time
        ));

        // Step 20 - Plug in modem first
        wizardSteps.add(new SupportWizardModel(
                "Plug in Modem",
                "Plug in your modem first. Wait until the lights are stable. This make take a few moments.",
                "Continue", "Exit",
                21, 0,
                R.drawable.icon_mode
        ));

        // Step 21 - Plug in router
        wizardSteps.add(new SupportWizardModel(
                "Plug in Router",
                "Now plug in your router. Wait for all lights to turn on and stabilize.",
                "Continue", "Exit",
                22, 0,
                R.drawable.icon_router
        ));

        // Step 22 - Check connection after reboot
        wizardSteps.add(new SupportWizardModel(
                "Check Connection",
                "Are you now able to connect to your Wi-Fi network?",
                "Yes", "No",
                0, 1,
                R.drawable.icon_wifi
        ));

        // Step 23 - Wi-Fi enabled?
        wizardSteps.add(new SupportWizardModel(
                "Wi-Fi Enabled?",
                "Is Wi-Fi turned on in your phone's settings?",
                "Yes", "No",
                24, 25,
                R.drawable.icon_wifi
        ));

        // Step 24 - Airplane mode off?
        wizardSteps.add(new SupportWizardModel(
                "Airplane Mode Off?",
                "Is Airplane Mode turned off?",
                "Yes", "No",
                26, 27,
                R.drawable.icon_airplane
        ));

        // Step 25 - Turn on Wi-Fi
        wizardSteps.add(new SupportWizardModel(
                "Turn On Wi-Fi",
                "Please enable Wi-Fi in your phone settings, then try connecting to your network.",
                "Done", "Exit",
                23, 0,
                R.drawable.icon_wifi
        ));

        // Step 26 - Forget & reconnect
        wizardSteps.add(new SupportWizardModel(
                "Forget & Reconnect",
                "Try forgetting the network and reconnecting manually from Wi-Fi settings.",
                "Done", "Exit",
                28, 0,
                R.drawable.icon_wifi
        ));

        // Step 27 - Disable airplane mode
        wizardSteps.add(new SupportWizardModel(
                "Disable Airplane Mode",
                "Please disable Airplane Mode and then try connecting again.",
                "Done", "Exit",
                26, 0,
                R.drawable.icon_airplane
        ));

        // Step 28 - Restart phone
        wizardSteps.add(new SupportWizardModel(
                "Restart Phone",
                "Try restarting your phone. This often resolves Wi-Fi connection issues.",
                "Restarted", "Exit",
                29, 0,
                R.drawable.icon_restart
        ));

        // Step 29 - Final check
        wizardSteps.add(new SupportWizardModel(
                "Final Check",
                "Are you now able to connect to your Wi-Fi network?",
                "Yes", "No",
                0, 9,
                R.drawable.icon_wifi
        ));

        // Step 30 - Weak Signal intro
        wizardSteps.add(new SupportWizardModel(
                "Weak Signal",
                "Let's troubleshoot poor Wi-Fi signal strength in your home.",
                "Start", "Exit",
                31, 0,
                R.drawable.icon_chart
        ));

        // Step 31 - Are you far from router?
        wizardSteps.add(new SupportWizardModel(
                "Distance Check",
                "Are you experiencing weak signal far away from your router?",
                "Yes", "No",
                32, 33,
                R.drawable.icon_distance
        ));

        // Step 32 - Move closer
        wizardSteps.add(new SupportWizardModel(
                "Try Moving Closer",
                "Try moving closer to your router and check signal strength again.",
                "Done", "Exit",
                34, 0,
                R.drawable.icon_closer
        ));

        // Step 33 - Obstructions
        wizardSteps.add(new SupportWizardModel(
                "Check for Obstructions",
                "Thick walls, appliances, or floors can weaken signal. Can you reposition your router?",
                "Yes", "No",
                34, 35,
                R.drawable.icon_fence
        ));

        // Step 34 - Signal improved?
        wizardSteps.add(new SupportWizardModel(
                "Signal Check",
                "Has the signal strength improved after moving or adjusting the router?",
                "Yes", "No",
                38, 35,
                R.drawable.icon_network_check
        ));

        // Step 35 - Suggest Mesh or Extender
        wizardSteps.add(new SupportWizardModel(
                "Wi-Fi Extender",
                "Consider a mesh Wi-Fi system or extender to improve coverage across your space.",
                "Restart Wizard", "Exit",
                0, 0,
                R.drawable.icon_mesh
        ));

        // Step 36 - More Issues Menu
        wizardSteps.add(new SupportWizardModel(
                "More Issues",
                "Select the issue you're experiencing.",
                "Intermittent Wi-Fi", "Weak Signal",
                5, 30,
                R.drawable.icon_info
        ));

        // Step 37 - Choose Device (from Step 9)
        wizardSteps.add(new SupportWizardModel(
                "Choose Device",
                "Which type of device are you using?",
                "Computer", "Phone/Tablet",
                11, 14,
                R.drawable.icon_devices
        ));

        // Step 38 - Great it works step
        wizardSteps.add(new SupportWizardModel(
                "Fixed!",
                "Great! You fixed your wi-fi!",
                "Restart", "Exit",
                0, -1,
                R.drawable.emoji_happy
        ));

        return wizardSteps;
    }
}
