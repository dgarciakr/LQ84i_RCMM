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

    public StatusViewModelFactory(TelephonyManager tm, LocationManager lm) {
        this.tm = tm;
        this.lm = lm;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StatusViewModel(tm, lm);
    }
}
