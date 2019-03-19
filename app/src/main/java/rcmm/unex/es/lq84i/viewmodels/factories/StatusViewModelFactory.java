package rcmm.unex.es.lq84i.viewmodels.factories;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import rcmm.unex.es.lq84i.viewmodels.StatusViewModel;

public class StatusViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TelephonyManager tm;
    private LocationManager lm;
    private boolean time;
    private Context context;
    private Integer UPDATE_TIME;

    public StatusViewModelFactory(TelephonyManager tm, LocationManager lm, boolean time, Integer UPDATE_TIME, Context context) {
        this.tm = tm;
        this.lm = lm;
        this.time = time;
        this.context = context;
        this.UPDATE_TIME = UPDATE_TIME;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StatusViewModel(tm, lm, time, UPDATE_TIME, context);
    }
}
