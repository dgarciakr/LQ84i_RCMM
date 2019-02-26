package rcmm.unex.es.lq84i.viewmodels.factories;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import rcmm.unex.es.lq84i.viewmodels.StatusViewModel;

public class StatusViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TelephonyManager tm;
    private LocationManager lm;
    private boolean time;

    public StatusViewModelFactory(TelephonyManager tm, LocationManager lm, boolean time) {
        this.tm = tm;
        this.lm = lm;
        this.time = time;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StatusViewModel(tm, lm, time);
    }
}
