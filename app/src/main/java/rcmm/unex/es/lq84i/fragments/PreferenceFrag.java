package rcmm.unex.es.lq84i.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import rcmm.unex.es.lq84i.R;
import rcmm.unex.es.lq84i.activities.MainActivity;

@SuppressLint("ValidFragment")
public class PreferenceFrag extends PreferenceFragmentCompat {
    public static final String KEY_LISTENER_PREFERENCE = "listener_type";
    public static final String KEY_LISTENER_PREFERENCE_TIME = "pref_updatetime";
    public static final String KEY_LISTENER_PREFERENCE_DISTANCE = "pref_updatetime";
    private static final String TAG = "PreferencesFrag";

    public PreferenceFrag() {
        //Requisito de la librería
    }


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        Log.i(TAG, "Entrando en onCreatePreferences");
        String TAG = PreferenceFrag.TAG + ".onCreatePreferences";
        addPreferencesFromResource(R.xml.preferences);
        final Preference prefTime = getPreferenceManager().findPreference(KEY_LISTENER_PREFERENCE_TIME);
        prefTime.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                ((MainActivity) getActivity()).newPreferenceTimeValue(o.toString());
                return true;
            }
        });

        final Preference prefDistance = getPreferenceManager().findPreference(KEY_LISTENER_PREFERENCE_DISTANCE);
        prefDistance.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                ((MainActivity) getActivity()).newPreferenceDistanceValue(o.toString());
                return true;
            }
        });
        Log.v(TAG, "Preferencias cargadas");
        Log.i(PreferenceFrag.TAG, "Saliendo de onCreatePreferences");
    }

}
