package me.theoria.wifimuscles.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import me.theoria.wifimuscles.data.model.DHCPModel;

public class DHCPViewModel extends AndroidViewModel {

    private final WifiManager wifiManager;
    private final MutableLiveData<DHCPModel> dhcpModelLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable dhcpFetcher = new Runnable() {
        @Override
        public void run() {
            fetchDhcpInfo();
            handler.postDelayed(this, 3000); // refresh every 3 seconds
        }
    };

    public DHCPViewModel(@NonNull Application application) {
        super(application);
        wifiManager = (WifiManager) application.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void fetchDhcpInfo() {
        if (wifiManager == null) {
            errorLiveData.postValue("WifiManager is unavailable.");
            dhcpModelLiveData.postValue(null);
            return;
        }

        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        if (dhcpInfo == null) {
            errorLiveData.postValue("DHCP Info unavailable.");
            dhcpModelLiveData.postValue(null);
            return;
        }

        DHCPModel model = new DHCPModel(
                dhcpInfo.gateway,
                dhcpInfo.netmask,
                dhcpInfo.dns1,
                dhcpInfo.dns2,
                dhcpInfo.serverAddress,
                dhcpInfo.leaseDuration
        );

        dhcpModelLiveData.postValue(model);
        errorLiveData.postValue(null);  // clear error if successful
    }

    public void startAutoUpdate() {
        dhcpFetcher.run();
    }

    public void stopAutoUpdate() {
        handler.removeCallbacks(dhcpFetcher);
    }

    public LiveData<DHCPModel> getDhcpModelLiveData() {
        return dhcpModelLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopAutoUpdate();
    }
}
