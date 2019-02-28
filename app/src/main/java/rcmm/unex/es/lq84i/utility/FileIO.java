package rcmm.unex.es.lq84i.utility;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileIO {
    private String filename;
    private FileOutputStream writer;
    private boolean ready;

    public FileIO(String filename, Context context) {
        this.filename = filename;
        try {
            writer = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), filename));
            ready = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ready = false;
        }
    }

    public boolean saveData(String str) {
        if (ready) {
            try {
                writer.write(str.getBytes());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                ready = false;
                return false;
            }
        } else {
            return false;
        }
    }

    public File getOutputFile(Context context) {
        return new File(context.getFilesDir() + "/" + filename);
    }

    public void end() {
        if (ready) {
            try {
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                ready = false;
            }
        }
    }
}
