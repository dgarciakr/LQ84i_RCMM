package rcmm.unex.es.lq84i.utility;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;

import rcmm.unex.es.lq84i.interfaces.LinkMeasurementPresenter;

/**
 * Para usarse con AsyncTask. Mide la velocidad de descarga de un servidor FTP
 */
public class DownlinkMeasurement extends ViewModel {
    private Activity mHost;
    private TrafficStats stats;
    private String server;
    private static final String USERNAME = "rcmm";
    private static final String PASSWORD = "rcmm";
    private static final String FILENAME = "documento.txt";
    private static final String UPLOADFILENAME = "documento";
    private String downlinkSpeed;
    private String uplinkSpeed;
    private String downlinkThroughput;
    private String uplinkThroughput;
    private String downlinkPackageLost;
    private String uplinkPackageLost;
    private String downlinkTotalPackages;
    private String uplinkTotalPackages;
    private String downlinkRtt;
    private String uplinkRtt;
    private String downlinkDelay;
    private String uplinkDelay;
    private String downlinkJitter;
    private String uplinkJitter;
    private String cellidentifier;
    private long timeStartDownlink;
    private long lastTimeDownlink;
    private long firstPackageDownlink;
    private long lastPackageDownlink;
    private long fileLengthDownlink;
    private long timeStartUplink;
    private long lastTimeUplink;
    private long firstPackageUplink;
    private long lastPackageUplink;
    private long fileLengthUplink;
    private AsyncMeassure asyncmeasure;
    private TelephonyManager tm;

    public DownlinkMeasurement(String server, Activity context, LinkMeasurementPresenter presenter) {
        mHost = context;
        stats = new TrafficStats();
        this.server = server;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        asyncmeasure = new AsyncMeassure(mHost, presenter);
        asyncmeasure.execute();
    }

    public String getDownlinkSpeed() {
        return downlinkSpeed;
    }

    public String getUplinkSpeed() {
        return uplinkSpeed;
    }

    public String getDownlinkThroughput() {
        return downlinkThroughput;
    }

    public String getUplinkThroughput() {
        return uplinkThroughput;
    }

    public String getDownlinkPackageLost() {
        return downlinkPackageLost;
    }

    public String getUplinkPackageLost() {
        return uplinkPackageLost;
    }

    public String getDownlinkTotalPackages() {
        return downlinkTotalPackages;
    }

    public String getUplinkTotalPackages() {
        return uplinkTotalPackages;
    }

    public String getUplinkRtt() {
        return uplinkRtt;
    }

    public String getDownlinkDelay() {
        return downlinkDelay;
    }

    public String getUplinkDelay() {
        return uplinkDelay;
    }

    public String getDownlinkJitter() {
        return downlinkJitter;
    }

    public String getUplinkJitter() {
        return uplinkJitter;
    }

    public String getCellid() {
        return cellidentifier;
    }

    /**
     * Calcula la velocidad descargando un archivo del servidor FTP
     *
     * @return Velocidad de descarga media en bits por segundo
     */
    private void downloadSpeed() {
        long speed = -1;
        speed = (fileLengthDownlink * 1000 * 8) / ((lastTimeDownlink - timeStartDownlink));
        Log.i("datosquequierover", "la velocidad de descarga es: " + speed);
        downlinkSpeed = Long.toString(speed);
    }

    /**
     * Calcula la velocidad de subida de un archivo a un servidor FTP
     *
     * @return devuelve un long con la cantidad de tiempo empleado en bits por segundo
     */
    private void uploadSpeed() {
        long speed;
        speed = (fileLengthUplink * 1000 * 8) / ((lastTimeUplink - timeStartUplink));
        Log.i("datosquequierover", "la velocidad de subida es: " + speed);
        uplinkSpeed = Long.toString(speed);
    }

    private void downlinkThroughtput() {
        double throughput;
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        throughput = fileLengthDownlink / (lastTimeDownlink - timeStartDownlink);
        downlinkThroughput = df.format(throughput);
    }

    private void uplinkThroughtput() {
        double throughtput;
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        throughtput = fileLengthUplink / (lastTimeUplink - timeStartUplink);
        uplinkThroughput = df.format(throughtput);
    }

    private void downlink_packagelost() {
        long percent;
        double packetsSupposed = Math.ceil(fileLengthDownlink / 1460); //Tamaño del archivo / Tamaño de un paquete - cabecera TCP - cabecera IP
        percent = (long) (((lastPackageDownlink - firstPackageDownlink) - (Math.ceil(packetsSupposed))) * 100 / lastPackageDownlink);
        downlinkPackageLost = Long.toString(percent);
    }

    private void uplink_packagelost() {
        long percent;
        double packetsSupposed = Math.ceil(fileLengthUplink / 1460); //Tamaño del archivo / Tamaño de un paquete - cabecera TCP - cabecera IP
        percent = (long) (((lastPackageUplink - firstPackageUplink) - (Math.ceil(packetsSupposed))) * 100 / lastPackageUplink);
        uplinkPackageLost = Long.toString(percent);
    }

    private void downlink_total_packages() {
        long packages;
        packages = lastPackageDownlink - firstPackageDownlink;
        downlinkTotalPackages = Long.toString(packages);
    }

    private void uplink_total_packages() {
        long packages;
        packages = lastPackageUplink - firstPackageUplink;
        uplinkTotalPackages = Long.toString(packages);
    }

    private long downlink_rtt() {
        long rtt;
        rtt = (lastTimeDownlink - timeStartDownlink) / (lastPackageDownlink - firstPackageDownlink);
        downlinkRtt = Long.toString(rtt);
        return rtt;
    }

    private long uplink_rtt() {
        long rtt;
        rtt = (lastTimeUplink - timeStartUplink) / (lastPackageUplink - firstPackageUplink);
        uplinkRtt = Long.toString(rtt);
        return rtt;
    }

    private long downlink_delay() {
        downlinkDelay = Long.toString(downlink_rtt() / 2);
        return downlink_rtt() / 2;
    }

    private long uplink_delay() {
        uplinkDelay = Long.toString(uplink_rtt() / 2);
        return uplink_rtt() / 2;
    }

    private void downlink_jitter() {
        ArrayList<Long> firstPackageArray = new ArrayList<Long>();
        ArrayList<Long> firstTimeArray = new ArrayList<Long>();
        ArrayList<Long> lastTimeArray = new ArrayList<Long>();
        ArrayList<Long> lastPackageArray = new ArrayList<Long>();
        long jitter = 0;
        for (int i = 0; i < 10; i++) {
            jitterDownload();
            firstPackageArray.add(firstPackageDownlink);
            firstTimeArray.add(timeStartDownlink);
            lastTimeArray.add(lastTimeDownlink);
            lastPackageArray.add(lastPackageDownlink);
        }

        for (int i = 0; i < 10; i++) {
            jitter += ((lastTimeArray.get(i) - firstTimeArray.get(i)) / (lastPackageArray.get(i) - firstPackageArray.get(i))) / 2;
        }
        downlinkJitter = Long.toString(jitter / 10);
    }

    private void uplink_jitter() {
        ArrayList<Long> firstPackageArray = new ArrayList<Long>();
        ArrayList<Long> firstTimeArray = new ArrayList<Long>();
        ArrayList<Long> lastTimeArray = new ArrayList<Long>();
        ArrayList<Long> lastPackageArray = new ArrayList<Long>();
        long jitter = 0;
        for (int i = 0; i < 10; i++) {
            jitterUpload();
            firstPackageArray.add(firstPackageUplink);
            firstTimeArray.add(timeStartUplink);
            lastTimeArray.add(lastTimeUplink);
            lastPackageArray.add(lastPackageUplink);
        }

        for (int i = 0; i < 10; i++) {
            jitter += ((lastTimeArray.get(i) - firstTimeArray.get(i)) / (lastPackageArray.get(i) - firstPackageArray.get(i))) / 2;
        }
        uplinkJitter = Long.toString(jitter / 10);
    }

    private void jitterUpload() {
        String FILENAME = "jitterTest";
        FTPClient clientUplink = new FTPClient();
        try {
            clientUplink.connect(InetAddress.getByName(server));
            clientUplink.enterLocalPassiveMode();
            clientUplink.login(USERNAME, PASSWORD);
            InputStream updateFile = mHost.getResources().openRawResource(mHost.getResources().getIdentifier(UPLOADFILENAME, "raw", mHost.getPackageName()));
            firstPackageUplink = lastPackageUplink;
            timeStartUplink = System.currentTimeMillis();
            clientUplink.storeFile(FILENAME, updateFile);
            lastTimeUplink = System.currentTimeMillis();
            lastPackageUplink = TrafficStats.getUidRxPackets(mHost.getPackageManager().getApplicationInfo(mHost.getPackageName(), PackageManager.GET_META_DATA).uid);
            updateFile.close();
            updateFile = mHost.getResources().openRawResource(mHost.getResources().getIdentifier(UPLOADFILENAME, "raw", mHost.getPackageName()));
            fileLengthUplink = updateFile.available();
            updateFile.close();
            lastPackageDownlink = lastPackageUplink;
        } catch (IOException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void jitterDownload() {
        String FILENAMEDOWLOAD = "jitterTest.txt";
        FTPClient clientDownlink = new FTPClient();
        try {
            clientDownlink.connect(InetAddress.getByName(server));
            clientDownlink.enterLocalPassiveMode();
            clientDownlink.login(USERNAME, PASSWORD);
            ByteArrayOutputStream received = new ByteArrayOutputStream();
            firstPackageDownlink = lastPackageDownlink;
            timeStartDownlink = System.currentTimeMillis();
            clientDownlink.retrieveFile(FILENAMEDOWLOAD, received);
            received.close();
            lastTimeDownlink = System.currentTimeMillis();
            lastPackageDownlink = TrafficStats.getUidRxPackets(mHost.getPackageManager().getApplicationInfo(mHost.getPackageName(), PackageManager.GET_META_DATA).uid);
            fileLengthDownlink = received.toByteArray().length;
        } catch (IOException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getCellIdentifier() {
        int cellid = -1;
        try {
            final TelephonyManager telephony = (TelephonyManager) mHost.getSystemService(mHost.TELEPHONY_SERVICE);
            if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                final GsmCellLocation location = (GsmCellLocation) telephony.getCellLocation();
                if (location != null) {
                    cellid = location.getCid();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();

        }
        cellidentifier = Integer.toString(cellid);
    }

    private class AsyncMeassure extends AsyncTask<Void, Void, String> {

        private Activity mHost;
        private LinkMeasurementPresenter presenter;

        public AsyncMeassure(Activity context, LinkMeasurementPresenter presenter) {
            mHost = context;
            this.presenter = presenter;
        }

        @Override
        protected String doInBackground(Void... voids) {

            FTPClient clientDownlink = new FTPClient();
            try {
                clientDownlink.connect(InetAddress.getByName(server));
                clientDownlink.enterLocalPassiveMode();
                clientDownlink.login(USERNAME, PASSWORD);
                ByteArrayOutputStream received = new ByteArrayOutputStream();
                if (lastPackageDownlink == 0) {
                    firstPackageDownlink = TrafficStats.getUidRxPackets(mHost.getPackageManager().getApplicationInfo(mHost.getPackageName(), PackageManager.GET_META_DATA).uid);
                } else {
                    firstPackageDownlink = lastPackageDownlink;
                }
                timeStartDownlink = System.currentTimeMillis();
                clientDownlink.retrieveFile(FILENAME, received);
                received.close();
                lastTimeDownlink = System.currentTimeMillis();
                lastPackageDownlink = TrafficStats.getUidRxPackets(mHost.getPackageManager().getApplicationInfo(mHost.getPackageName(), PackageManager.GET_META_DATA).uid);
                fileLengthDownlink = received.toByteArray().length;
            } catch (IOException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            FTPClient clientUplink = new FTPClient();
            try {
                clientUplink.connect(InetAddress.getByName(server));
                clientUplink.enterLocalPassiveMode();
                clientUplink.login(USERNAME, PASSWORD);
                InputStream updateFile = mHost.getResources().openRawResource(mHost.getResources().getIdentifier(UPLOADFILENAME, "raw", mHost.getPackageName()));
                firstPackageUplink = lastPackageDownlink;
                timeStartUplink = System.currentTimeMillis();
                clientUplink.storeFile(UPLOADFILENAME, updateFile);
                lastTimeUplink = System.currentTimeMillis();
                lastPackageUplink = TrafficStats.getUidRxPackets(mHost.getPackageManager().getApplicationInfo(mHost.getPackageName(), PackageManager.GET_META_DATA).uid);
                updateFile.close();
                updateFile = mHost.getResources().openRawResource(mHost.getResources().getIdentifier(UPLOADFILENAME, "raw", mHost.getPackageName()));
                fileLengthUplink = updateFile.available();
                updateFile.close();
            } catch (IOException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            downloadSpeed();
            uploadSpeed();
            downlinkThroughtput();
            uplinkThroughtput();
            downlink_packagelost();
            uplink_packagelost();
            uplink_total_packages();
            downlink_total_packages();
            uplink_rtt();
            downlink_rtt();
            uplink_delay();
            downlink_delay();
            downlink_jitter();
            uplink_jitter();
            getCellIdentifier();

            Log.i("datosquequierover", "Datos descarga: Paquetes antes de la descarga: " + firstPackageDownlink + " tiempo antes de la descarga " + timeStartDownlink + " \n tiempo despues de la descarga " + lastTimeDownlink + " paquetes después de la descarga " + lastPackageDownlink + " tamaño del archivo " + fileLengthDownlink);
            Log.i("datosquequierover", "Datos subida: Paquetes antes de la subida: " + firstPackageUplink + " tiempo antes de la subida " + timeStartUplink + " \n tiempo despues de la subida " + lastTimeUplink + " paquetes después de la subida " + lastPackageUplink + " tamaño del archivo " + fileLengthUplink);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            presenter.updateUI();
        }
    }
}
