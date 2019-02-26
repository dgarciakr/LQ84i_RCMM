package rcmm.unex.es.lq84i.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import rcmm.unex.es.lq84i.R;

public class PreferenceFrag extends PreferenceFragmentCompat {
    public static final String KEY_LISTENER_PREFERENCE = "listener_type";
    private static final String TAG = "PreferencesFrag";

    public PreferenceFrag() {
        //Requisito de la librería
    }


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        Log.i(TAG, "Entrando en onCreatePreferences");
        String TAG = PreferenceFrag.TAG + ".onCreatePreferences";

        addPreferencesFromResource(R.xml.preferences);
        Log.v(TAG, "Preferencias cargadas");
        Log.i(PreferenceFrag.TAG, "Saliendo de onCreatePreferences");
    }

}
