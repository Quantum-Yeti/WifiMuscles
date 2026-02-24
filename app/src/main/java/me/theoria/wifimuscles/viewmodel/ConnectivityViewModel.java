package me.theoria.wifimuscles.viewmodel;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.stream.Collectors;

import me.theoria.wifimuscles.model.ConnectivityModel;

public class ConnectivityViewModel extends AndroidViewModel {

    private final MutableLiveData<ConnectivityModel> connectivityLiveData = new MutableLiveData<>();
    private final ConnectivityManager connectivityManager;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable updateRunnable;

    private boolean updating = false;

    public ConnectivityViewModel(@NonNull Application application) {
        super(application);
        connectivityManager = (ConnectivityManager) application.getSystemService(Application.CONNECTIVITY_SERVICE);

        updateRunnable = new Runnable() {
            @Override
            public void run() {
                updateConnectivityStatus();
                handler.postDelayed(this, 5000);
            }
        };
    }

    public LiveData<ConnectivityModel> getConnectivityStatus() {
        return connectivityLiveData;
    }

    public void updateConnectivityStatus() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(ConnectivityManager.class);

        if (cm == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            connectivityLiveData.postValue(null);
            return;
        }

        Network network = cm.getActiveNetwork();
        NetworkCapabilities caps = cm.getNetworkCapabilities(network);
        LinkProperties props = cm.getLinkProperties(network);

        if (network == null || caps == null) {
            connectivityLiveData.postValue(null);
            return;
        }

        ConnectivityModel.TransportType transportType;
        if (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            transportType = ConnectivityModel.TransportType.WIFI;
        } else if (caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            transportType = ConnectivityModel.TransportType.CELLULAR;
        } else if (caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            transportType = ConnectivityModel.TransportType.ETHERNET;
        } else if (caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
            transportType = ConnectivityModel.TransportType.VPN;
        } else {
            transportType = ConnectivityModel.TransportType.UNKNOWN;
        }

        // domains is a String, so get directly from LinkProperties
        String domains = props != null ? props.getDomains() : null;

        boolean isVpn = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN);

        ConnectivityModel model = new ConnectivityModel(
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET),
                caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED),
                cm.isActiveNetworkMetered(),
                transportType,
                caps.getLinkDownstreamBandwidthKbps(),
                caps.getLinkUpstreamBandwidthKbps(),
                props != null ? props.getInterfaceName() : null,
                domains,
                isVpn
        );

        connectivityLiveData.postValue(model);
    }

    public void startAutoUpdate() {
        if (!updating) {
            updating = true;
            handler.post(updateRunnable);
        }
    }

    public void stopAutoUpdate() {
        if (updating) {
            handler.removeCallbacks(updateRunnable);
            updating = false;
        }
    }

    private List<String> toStringList(List<?> input) {
        return input.stream().map(Object::toString).collect(Collectors.toList());
    }
}
