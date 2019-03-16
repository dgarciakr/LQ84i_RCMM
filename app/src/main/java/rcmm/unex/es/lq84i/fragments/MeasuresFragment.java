package rcmm.unex.es.lq84i.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import rcmm.unex.es.lq84i.R;
import rcmm.unex.es.lq84i.interfaces.LinkMeasurementPresenter;
import rcmm.unex.es.lq84i.utility.DownlinkMeasurement;

public class MeasuresFragment extends Fragment implements LinkMeasurementPresenter {

    private Activity mHost;
    private DownlinkMeasurement measurement;
    private View v;
    private TelephonyManager tm;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost = getActivity();
        measurement = new DownlinkMeasurement("boquique.unex.es", mHost, this);
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
    }
}
