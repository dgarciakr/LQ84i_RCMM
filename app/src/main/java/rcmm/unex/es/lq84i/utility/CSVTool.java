package rcmm.unex.es.lq84i.utility;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

public class CSVTool {
    private static boolean isStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean saveAsCSV(String filename, Map<String, String> content) {
        if (!isStorageAvailable()) {
            return false;
        }
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), filename);
            if (file.exists()) {
                Log.i("CSVTool", "Archivo encontrado, borrando...");
                if (!file.delete()) {
                    throw new IOException("Archivo encontrado y no borrado");
                }
                if (!file.createNewFile()) {
                    throw new IOException("No se pudo recrear el archivo");
                }
            }
            OutputStream outputStream = new FileOutputStream(file);
            Iterator<Map.Entry<String, String>> it = content.entrySet().iterator();
            for (Map.Entry<String, String> curr = it.next(); it.hasNext(); ) {
                String cont = curr.getKey() + ";" + curr.getValue();
                outputStream.write(cont.getBytes());
            }
            outputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.e("CSVTool", "Archivo no encontrado");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            Log.e("CSVTool", "IOException");
            e.printStackTrace();
            return false;
        }
    }
}
