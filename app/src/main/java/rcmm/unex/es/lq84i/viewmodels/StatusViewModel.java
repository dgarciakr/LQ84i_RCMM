package rcmm.unex.es.lq84i.viewmodels;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.List;
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

    /**
     * Datos tomados mediante mediciones
     */
    private StringBuffer measuredData;

    /**
     * Gestor de localización mediante GPS
     */
    private LocationManager lm;

    private static final String CSVHEADER = "latitude;longitude;altitude;RSRP";

    public StatusViewModel(TelephonyManager tm, LocationManager lm) {
        data = new LinkedHashMap<>();
        this.tm = tm;
        this.lm = lm;
        measuredData = new StringBuffer();
        measuredData.append(CSVHEADER);
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
        currView.setText(resources.getString(R.string.device_id) + data.get(0));
        currView = v.findViewById(R.id.phone_num);
        currView.setText(resources.getString(R.string.phone_num) + data.get(1));
        currView = v.findViewById(R.id.software_ver);
        currView.setText(resources.getString(R.string.software_ver) + data.get(2));
        currView = v.findViewById(R.id.op_name);
        currView.setText(resources.getString(R.string.op_name) + data.get(3));
        currView = v.findViewById(R.id.sim_op);
        currView.setText(resources.getString(R.string.sim_op) + data.get(4));
        currView = v.findViewById(R.id.sub_id);
        currView.setText(resources.getString(R.string.sub_id) + data.get(5));
        currView = v.findViewById(R.id.network_type);
        currView.setText(resources.getString(R.string.network_type) + data.get(6));
        currView = v.findViewById(R.id.voice_radio_type);
        currView.setText(resources.getString(R.string.voice_radio_type) + data.get(7));

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

    public void startListeners(final TextView callStateView, final TextView connectionStateView,
                               final TextView serviceStateView, final TextView cellLocationView,
                               final TextView phoneLocationView, final TextView signalView,
                               final ImageView signalImage, final TextView dataView) {
        int event = PhoneStateListener.LISTEN_DATA_ACTIVITY |
                PhoneStateListener.LISTEN_CALL_STATE |
                PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                PhoneStateListener.LISTEN_SERVICE_STATE;
        //|PhoneStateListener.LISTEN_CELL_LOCATION;

        PhoneStateListener listener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                String phoneState = "???";
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        phoneState = "Libre";
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        phoneState = "Sonando";
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        phoneState = "Llamada activa";
                        break;
                }
                callStateView.setText(phoneState);
                super.onCallStateChanged(state, phoneNumber);
            }

            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                String state = "???";
                switch (serviceState.getState()) {
                    case ServiceState.STATE_IN_SERVICE:
                        state = "En servicio";
                        break;
                    case ServiceState.STATE_OUT_OF_SERVICE:
                        state = "Fuera de servicio";
                        break;
                    case ServiceState.STATE_EMERGENCY_ONLY:
                        state = "Solo emergencias";
                        break;
                    case ServiceState.STATE_POWER_OFF:
                        state = "Apagado";
                        break;
                }
                serviceStateView.setText(state);
                super.onServiceStateChanged(serviceState);
            }

            @Override
            public void onCellLocationChanged(CellLocation location) {
                //TODO Cuando ya tengamos el mapa de calor
                //TODO Toca parsear a una de sus subclases
                super.onCellLocationChanged(location);
            }

            @Override
            public void onDataConnectionStateChanged(int state) {
                String dataState = "???";
                switch (state) {
                    case TelephonyManager.DATA_ACTIVITY_NONE:
                        dataState = "Ninguna";
                        break;
                    case TelephonyManager.DATA_ACTIVITY_IN:
                        dataState = "E";
                        break;
                    case TelephonyManager.DATA_ACTIVITY_OUT:
                        dataState = "S";
                        break;
                    case TelephonyManager.DATA_ACTIVITY_INOUT:
                        dataState = "E/S";
                        break;
                    case TelephonyManager.DATA_ACTIVITY_DORMANT:
                        dataState = "Latente";
                        break;
                }
                dataView.setText(dataState);
                super.onDataConnectionStateChanged(state);
            }

            @Override
            public void onCellInfoChanged(List<CellInfo> cellInfo) {
                switch (tm.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_GSM:
                        CellSignalStrengthGsm strGsm = ((CellInfoGsm) cellInfo.get(0)).
                                getCellSignalStrength();
                        Integer dbmGsm = strGsm.getDbm();
                        signalView.setText(dbmGsm.toString());
                        switch (strGsm.getLevel()) {
                            case 0:
                                //TODO
                                break;
                            case 1:
                                //TODO
                                break;
                            case 2:
                                //TODO
                                break;
                            case 3:
                                //TODO
                                break;
                            case 4:
                                //TODO
                                break;
                        }
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        CellSignalStrengthLte strLte = ((CellInfoLte) cellInfo.get(0)).
                                getCellSignalStrength();
                        Integer dbmLte = strLte.getDbm();
                        signalView.setText(dbmLte.toString());
                        switch (strLte.getLevel()) {
                            case 0:
                                //TODO
                                break;
                            case 1:
                                //TODO
                                break;
                            case 2:
                                //TODO
                                break;
                            case 3:
                                //TODO
                                break;
                            case 4:
                                //TODO
                                break;
                        }
                        break;
                }
                super.onCellInfoChanged(cellInfo);
            }
        };
        tm.listen(listener, event);
    }

    private void measure(int signal, Location location) {

    }
}
