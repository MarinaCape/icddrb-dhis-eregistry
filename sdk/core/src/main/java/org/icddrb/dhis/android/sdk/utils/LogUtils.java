package org.icddrb.dhis.android.sdk.utils;

import android.os.Environment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogUtils {
    public static boolean writeErrorLogToSDCard(String path, String errorLog) {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.append(errorLog);
            writer.newLine();
            writer.append("-------------------------------");
            writer.newLine();
            writer.close();
            return true;
        } catch (IOException ioe2) {
            ioe2.printStackTrace();
            return false;
        }
    }
}
