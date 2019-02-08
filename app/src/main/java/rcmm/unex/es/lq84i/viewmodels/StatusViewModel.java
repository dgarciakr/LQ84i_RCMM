package rcmm.unex.es.lq84i.viewmodels;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import rcmm.unex.es.lq84i.R;
import rcmm.unex.es.lq84i.utility.CSVTool;

public class StatusViewModel extends ViewModel {

    /**
     * Número de campos a mostrar
     */
    private static final Integer FIELDS = 8;

    /**
     * Datos a recoger y mostrar
     */
    private Map<Integer, String> data;

    /**
     * Mapa que ayuda a la hora de mostrar el tipo de red
     */
    private Map<Integer, String> networkTypeData;

    /**
     * Mapa que ayuda a la hora de mostrar el subtipo de red
     */
    private Map<Integer, String> networkSubtypeData;

    /**
     * TelephonyManager para tomar los datos
     */
    private TelephonyManager tm;

    public StatusViewModel(TelephonyManager tm) {
        data = new LinkedHashMap<>();
        this.tm = tm;
        initializeBaseData();
        updateData();
    }

    /**
     * Actualiza la vista con los últimos datos
     *
     * @param v Vista que contiene los TextViews con los datos a actualizar7
     * @param resources Recursos del sistema
     */
    @SuppressLint("SetTextI18n")
    public void updateView(View v, Resources resources) {
        TextView currView;
        //TODO Sustituir todos los currView.getText().toString() por resources.getString(R.string.ID). El ID es el mismo nombre que el del R.id para el findViewByID en cada caso
        currView = v.findViewById(R.id.device_id);
        currView.setText(currView.getText().toString() + data.get(0));
        currView = v.findViewById(R.id.phone_num);
        currView.setText(currView.getText().toString() + data.get(1));
        currView = v.findViewById(R.id.software_ver);
        currView.setText(currView.getText().toString() + data.get(2));
        currView = v.findViewById(R.id.op_name);
        currView.setText(currView.getText().toString() + data.get(3));
        currView = v.findViewById(R.id.sim_op);
        currView.setText(currView.getText().toString() + data.get(4));
        currView = v.findViewById(R.id.sub_id);
        currView.setText(currView.getText().toString() + data.get(5));
        currView = v.findViewById(R.id.network_type);
        currView.setText(currView.getText().toString() + data.get(6));
        currView = v.findViewById(R.id.voice_radio_type);
        currView.setText(currView.getText().toString() + data.get(7));
    }

    public boolean saveData() {
        updateData();
        String[] ids = {"device_id", "phone_num", "software_ver", "op_name", "sim_op", "sub_id",
                "network_type", "voice_radio_type"};
        Map<String, String> content = new LinkedHashMap<>();
        for (int i = 0; i < FIELDS; i++) {
            content.put(ids[i], Objects.requireNonNull(data.get(i)));
        }
        return CSVTool.saveAsCSV("data.csv", content);
    }

    /**
     * Actualiza los datos
     */
    @SuppressLint("HardwareIds")
    private void updateData() {
        try {
            Integer networkType = tm.getNetworkType();
            data.put(6, networkTypeData.get(networkType) +
                    " (" + networkSubtypeData.get(networkType) + ")");

            //getDeviceID está deprecated, toca usar esto
            if (Objects.equals(networkTypeData.get(networkType), "CDMA")) {
                data.put(0, tm.getMeid());
            } else {
                data.put(0, tm.getImei());
            }
            data.put(1, tm.getLine1Number());
            data.put(2, tm.getDeviceSoftwareVersion());
            data.put(3, tm.getNetworkOperatorName());
            data.put(4, tm.getNetworkOperator());
            data.put(5, tm.getSubscriberId());
            Integer voiceNetworkType = tm.getVoiceNetworkType();
            data.put(7, networkTypeData.get(voiceNetworkType) +
                    " (" + networkSubtypeData.get(voiceNetworkType) + ")");
        } catch (SecurityException ex) {
            for (int i = 0; i < FIELDS; i++) {
                data.put(i, "Permiso requerido");
            }
        }
    }

    /**
     * Inicializa los datos base
     */
    private void initializeBaseData() {
        networkTypeData = new LinkedHashMap<>();
        networkSubtypeData = new LinkedHashMap<>();
        String CDMA = "CDMA";
        String GSM = "GSM";
        String UMTS = "UMTS";
        String LTE = "LTE";
        String unknown = "???";
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_1xRTT, CDMA);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_1xRTT, "1xRTT");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_CDMA, CDMA);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_CDMA, "CDMA");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_EDGE, GSM);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_EDGE, "EDGE");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_EHRPD, CDMA);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_EHRPD, "EHRPD");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_EVDO_0, CDMA);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_EVDO_0, "EVDO_0");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_EVDO_A, CDMA);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_EVDO_A, "EVDO_A");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_EVDO_B, CDMA);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_EVDO_B, "EVDO_B");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_GPRS, GSM);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_GPRS, "GPRS");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_GSM, GSM);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_GSM, "GSM");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_HSDPA, UMTS);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_HSDPA, "HDSPA");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_HSPA, UMTS);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_HSPA, "HSPA");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_HSPAP, UMTS);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_HSPAP, "HSPA+");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_HSUPA, UMTS);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_HSUPA, "HSUPA");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_IDEN, unknown);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_IDEN, "iDen");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_IWLAN, unknown);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_IWLAN, "IWLAN");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_LTE, LTE);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_LTE, "LTE");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_TD_SCDMA, CDMA);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_TD_SCDMA, "TD_SCDMA");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_UMTS, UMTS);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_UMTS, "UMTS");
        networkTypeData.put(TelephonyManager.NETWORK_TYPE_UNKNOWN, unknown);
        networkSubtypeData.put(TelephonyManager.NETWORK_TYPE_UNKNOWN, "???");
    }
}
