package rcmm.unex.es.lq84i.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import rcmm.unex.es.lq84i.R;

public class PreferenceFrag extends PreferenceFragment {
    public static final String KEY_LISTENER_PREFERENCE = "listener_type";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
