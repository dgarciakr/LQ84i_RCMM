package rcmm.unex.es.lq84i.viewmodels.factories;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import rcmm.unex.es.lq84i.viewmodels.StatusViewModel;

public class StatusViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TelephonyManager tm;

    public StatusViewModelFactory(TelephonyManager tm) {
        this.tm = tm;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StatusViewModel(tm);
    }
}
