package org.icddrb.dhis.android.eregistry.export;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ShareCompat.IntentBuilder;
import android.support.v4.content.FileProvider;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.icddrb.dhis.android.eregistry.C0773R;

public class ExportData {
    private final String DATABASE_FOLDER = "databases/";
    private final String EXPORT_DATA_FILE = "compressedData.zip";
    private final String EXPORT_DATA_FOLDER = "exportdata/";
    private final String EXTRA_INFO = "extrainfo.txt";
    private final String SHAREDPREFERENCES_FOLDER = "shared_prefs/";
    private final String TAG = ".ExportData";
    private Context mContext;

    public static String getCommitHash(Context context) {
        int layoutId = context.getResources().getIdentifier("lastcommit", "raw", context.getPackageName());
        if (layoutId == 0) {
            return "";
        }
        return convertFromInputStreamToString(context.getResources().openRawResource(layoutId)).toString();
    }

    public static StringBuilder convertFromInputStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while (true) {
                String line = r.readLine();
                if (line == null) {
                    break;
                }
                stringBuilder.append(line + StringUtils.LF);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder;
    }

    public Intent dumpAndSendToAIntent(Activity activity) throws IOException {
        this.mContext = activity.getBaseContext();
        removeDumpIfExist(activity);
        File tempFolder = new File(getCacheDir() + "/" + "exportdata/");
        tempFolder.mkdir();
        dumpDatabase("Dhis2.db", tempFolder);
        dumpSharedPreferences(tempFolder);
        dumpMetadata(new File(tempFolder + "/" + "extrainfo.txt"), activity);
        File compressedFile = compressFolder(tempFolder);
        if (compressedFile == null) {
            return null;
        }
        return createEmailIntent(activity, compressedFile);
    }

    private void dumpMetadata(File customInformation, Activity activity) throws IOException {
        customInformation.createNewFile();
        FileWriter fw = new FileWriter(customInformation.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("Flavour: \n");
        bw.write("Version code: 4\n");
        bw.write("Version name: 1.2.1\n");
        bw.write("Application Id: org.icddrb.dhis.client.sdk.ui\n");
        bw.write("Build type: release\n");
        bw.write("Hash: " + getCommitHash(activity));
        bw.close();
        fw.close();
    }

    private File compressFolder(File tempFolder) throws IOException {
        if (tempFolder.listFiles() == null) {
            Log.d(".ExportData", "Error, nothing to convert");
            return null;
        }
        zipFolder(tempFolder.getAbsolutePath(), getCacheDir() + "/" + "compressedData.zip");
        return new File(getCacheDir() + "/" + "compressedData.zip");
    }

    private void zipFolder(String inputFolderPath, String outputFilePath) throws IOException {
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputFilePath));
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();
            Log.d("", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("", "Adding file: " + files[i].getName());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                while (true) {
                    int length = fis.read(buffer);
                    if (length <= 0) {
                        break;
                    }
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    private void dumpDatabase(String dbName, File tempFolder) throws IOException {
        if (tempFolder.canWrite()) {
            copyFile(new File(getDatabasesFolder(), dbName), new File(tempFolder, dbName));
        }
    }

    private void dumpSharedPreferences(File tempFolder) throws IOException {
        File[] files = getSharedPreferencesFolder().listFiles();
        Log.d("Files", "Size: " + files.length);
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getName());
            copyFile(files[i], new File(tempFolder, files[i].getName()));
        }
    }

    private void copyFile(File current, File backup) throws IOException {
        if (current.exists()) {
            FileChannel src = new FileInputStream(current).getChannel();
            FileChannel dst = new FileOutputStream(backup).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
        }
    }

    private File getCacheDir() {
        return this.mContext.getCacheDir();
    }

    private String getAppPath() {
        return "/data/data/" + this.mContext.getPackageName() + "/";
    }

    private File getSharedPreferencesFolder() {
        return new File(getAppPath() + "shared_prefs/");
    }

    private File getDatabasesFolder() {
        return new File(getAppPath() + "databases/");
    }

    private Intent createEmailIntent(Activity activity, File data) {
        Log.d(".ExportData", data.toURI() + "");
        data.setReadable(true, false);
        return IntentBuilder.from(activity).setType("application/zip").setSubject(this.mContext.getString(C0773R.string.app_name) + " db " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime())).setStream(FileProvider.getUriForFile(activity, "org.icddrb.dhis.android.eregistry.export.ExportData", data)).setChooserTitle(activity.getResources().getString(C0773R.string.export_data_name)).createChooserIntent().addFlags(1);
    }

    public void removeDumpIfExist(Activity activity) {
        new File(activity.getCacheDir() + "/" + "compressedData.zip").delete();
        File tempFolder = new File(activity.getCacheDir() + "/" + "exportdata/");
        File[] files = tempFolder.listFiles();
        if (files != null) {
            for (File delete : files) {
                delete.delete();
            }
            tempFolder.delete();
        }
    }
}
