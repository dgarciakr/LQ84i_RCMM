package rcmm.unex.es.lq84i.utility;

import android.net.TrafficStats;

import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Para usarse con AsyncTask. Mide la velocidad de descarga de un servidor FTP
 */
public class DownlinkMeasurement {
    private TrafficStats stats;
    private String server;
    private static final String USERNAME = "MEASURINGUSER";
    private static final String PASSWORD = "MEASURINGPASSWORD";
    private static final String FILENAME = "MeasuringFile.txt";

    public DownlinkMeasurement(String server) {
        stats = new TrafficStats();
        this.server = server;
    }

    /**
     * Calcula la velocidad descargando un archivo del servidor FTP
     *
     * @return Velocidad de descarga media en bytes por segundo
     */
    public long downloadSpeed() {
        long speed = -1;
        FTPClient client = new FTPClient();
        try {
            client.connect(InetAddress.getByName(server));
            client.enterLocalPassiveMode();
            client.login(USERNAME, PASSWORD);
            ByteArrayOutputStream received = new ByteArrayOutputStream();
            long timeStart = System.currentTimeMillis();
            client.retrieveFile(FILENAME, received);
            received.close();
            long timeEnd = System.currentTimeMillis();
            speed = (received.toByteArray().length) * 1000 / (timeEnd - timeStart);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return speed;
    }

}
