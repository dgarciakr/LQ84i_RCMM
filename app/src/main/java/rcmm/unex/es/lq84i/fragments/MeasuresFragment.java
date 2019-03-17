package rcmm.unex.es.lq84i.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import rcmm.unex.es.lq84i.R;
import rcmm.unex.es.lq84i.interfaces.LinkMeasurementPresenter;
import rcmm.unex.es.lq84i.utility.DownlinkMeasurement;
import rcmm.unex.es.lq84i.utility.FileIO;

public class MeasuresFragment extends Fragment implements LinkMeasurementPresenter {

    private Activity mHost;
    private DownlinkMeasurement measurement;
    private View v;
    private TelephonyManager tm;
    private LocationManager lm;
    private Integer testN;
    private FileIO csv;
    private static final String FILENAME = "QoSTests.csv";
    private static final String CSVHEADER = "test;latitude;longitude;RSRP;CID;dlspeed;ulspeed;dlthroughput;ulthroughput;dlpacketloss;ulpacketloss;dlpackets;ulpackets;ulRTT;dldelay;uldelay;dljitter;uljitter";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testN = 0;
        mHost = getActivity();
        csv = new FileIO(FILENAME, mHost);
        measurement = new DownlinkMeasurement("boquique.unex.es", mHost, this);
        tm = (TelephonyManager)
                Objects.requireNonNull(mHost).getSystemService(Context.TELEPHONY_SERVICE);
        lm = (LocationManager)
                Objects.requireNonNull(mHost.getSystemService(Context.LOCATION_SERVICE));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.conection_measurement, container, false);
        return res;
    }

    @Override
    public void onViewCreated(@NonNull final View res, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(res, savedInstanceState);
        v = res;
        Button b = v.findViewById(R.id.refresh_values);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recalculate();
                Snackbar.make(v, mHost.getResources().getString(R.string.snackbarmessage), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void recalculate() {
        measurement = new DownlinkMeasurement("boquique.unex.es", mHost, this);
        testN++;
    }

    public String parseSpeed(boolean download) {
        String text = null;
        Long speed = null;
        if (download) {
            speed = Math.round(Double.parseDouble(measurement.getDownlinkSpeed()));
        } else {
            speed = Math.round(Double.parseDouble(measurement.getUplinkSpeed()));
        }
        int speedLength = speed.toString().length();
        if (speedLength > 3 && speedLength <= 6) {
            text = " " + Math.round(speed / 1024) + " Kb/s";
        } else if (speedLength > 6 && speedLength <= 9) {
            text = " " + Math.round(speed / 1048576) + " Mb/s";
        } else if (speedLength > 9) {
            text = " " + Math.round(speed / 1073741824) + " Gb/s";
        } else {
            text = " " + Math.round(speed) + " b/s";
        }
        return text;
    }

    public void realice(View res) {
        TextView cellid = res.findViewById(R.id.measurement_cellid);
        cellid.setText(getResources().getString(R.string.cell_location) + " " + measurement.getCellid());
        TextView downlink_speed = res.findViewById(R.id.downlink_speed);
        downlink_speed.setText(getResources().getString(R.string.downloadSpeed) + " " + parseSpeed(true));
        TextView downlink_throughput = res.findViewById(R.id.downlink_throughtput);
        downlink_throughput.setText(getResources().getString(R.string.time_downlink) + " " + measurement.getDownlinkThroughput() + " ms");
        TextView downlink_packetlost = res.findViewById(R.id.downlink_packetloss);
        downlink_packetlost.setText(getResources().getString(R.string.packet_downlink) + " " + measurement.getDownlinkPackageLost() + " %");
        TextView downlink_packets = res.findViewById(R.id.downlink_totalpacket);
        downlink_packets.setText(getResources().getString(R.string.totalpackets_downlink) + " " + measurement.getDownlinkTotalPackages() + " paquetes");
        TextView downlink_delay = res.findViewById(R.id.downlink_delay);
        downlink_delay.setText(getResources().getString(R.string.downlink_delay) + " " + measurement.getDownlinkDelay() + " ms");
        TextView downlink_jitter = res.findViewById(R.id.downlink_jitter);
        downlink_jitter.setText(getResources().getString(R.string.jitter_downlink) + " " + measurement.getDownlinkJitter() + " ms");
        TextView uplink_speed = res.findViewById(R.id.uplink_speed);
        uplink_speed.setText(getResources().getString(R.string.uploadSpeed) + " " + parseSpeed(false));
        TextView uplink_throughput = res.findViewById(R.id.throughtput_uplink);
        uplink_throughput.setText(getResources().getString(R.string.time_uplink) + " " + measurement.getUplinkThroughput() + " ms");
        TextView uplink_packetlost = res.findViewById(R.id.uplink_packetloss);
        uplink_packetlost.setText(getResources().getString(R.string.packet_uplink) + " " + measurement.getUplinkPackageLost() + " %");
        TextView uplink_packets = res.findViewById(R.id.uplink_totalpackage);
        uplink_packets.setText(getResources().getString(R.string.totalpackets_uplink) + " " + measurement.getUplinkTotalPackages() + " paquetes");
        TextView uplink_rtt = res.findViewById(R.id.uplink_rtt);
        uplink_rtt.setText(getResources().getString(R.string.rtt_uplink) + " " + measurement.getUplinkRtt() + " ms");
        TextView uplink_delay = res.findViewById(R.id.uplink_delay);
        uplink_delay.setText(getResources().getString(R.string.uplink_delay) + " " + measurement.getUplinkDelay() + " ms");
        TextView uplink_jitter = res.findViewById(R.id.uplink_jitter);
        uplink_jitter.setText(getResources().getString(R.string.jitter_uplink) + " " + measurement.getUplinkJitter() + " ms");
    }

    @Override
    public void updateUI() {
        realice(v);
        saveTest();
    }

    @Override
    public void onDestroy() {
        csv.end();
        super.onDestroy();
    }

    private void saveTest() {
        try {
            Location currLoc = lm.getLastKnownLocation(lm.getAllProviders().get(0));
            long bestTime = currLoc.getElapsedRealtimeNanos();
            for (String provider : lm.getAllProviders()) {
                @SuppressLint("MissingPermission") Location loc = lm.getLastKnownLocation(provider);
                if (loc.getElapsedRealtimeNanos() > bestTime) {
                    bestTime = loc.getElapsedRealtimeNanos();
                    currLoc = loc;
                }
            }
            Integer dbm = 0;
            List<CellInfo> cellInfoList = tm.getAllCellInfo();
            for (CellInfo cellInfo : cellInfoList) {
                if (cellInfo instanceof CellInfoLte) {
                    dbm = ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();
                    break;
                } else if (cellInfo instanceof CellInfoGsm) {
                    dbm = ((CellInfoGsm) cellInfo).getCellSignalStrength().getDbm();
                    break;
                }
            }
            GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
            String cid = Integer.toHexString(cellLocation.getCid());
            String format = testN + ";" + currLoc.getLatitude() + ";" + currLoc.getLongitude() +
                    ";" + dbm + ";" + cid + ";" + measurement.getDownlinkSpeed() + ";" +
                    measurement.getUplinkSpeed() + ";" + measurement.getDownlinkThroughput() + ";"
                    + measurement.getUplinkThroughput() + ";" + measurement.getDownlinkPackageLost()
                    + ";" + measurement.getUplinkPackageLost() + ";" +
                    measurement.getDownlinkTotalPackages() + ";" +
                    measurement.getUplinkTotalPackages() + ";" + measurement.getUplinkRtt() + ";" +
                    measurement.getDownlinkDelay() + ";" + measurement.getUplinkDelay() + ";" +
                    measurement.getDownlinkJitter() + ";" + measurement.getUplinkJitter();
            if (!csv.saveData(format)) {
                Log.e("Measure", "No se pudieron guardar los datos");
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }
}
