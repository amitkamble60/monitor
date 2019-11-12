package com.amit.monitorlib;

import android.app.ActivityManager;
import android.content.Context;
import android.net.TrafficStats;
import android.os.Environment;
import android.os.StatFs;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class MonitorUtil {

    private static final MonitorUtil ourInstance = new MonitorUtil();

    public static MonitorUtil getInstance() {
        return ourInstance;
    }

    private MonitorUtil() {
    }

    public String getMemoryPercent(Context context) {

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (activityManager != null)
            activityManager.getMemoryInfo(memoryInfo);

        Runtime runtime = Runtime.getRuntime();

        long used = runtime.totalMemory() - runtime.freeMemory();

        float percent = ((float) used * 100) / (float) runtime.maxMemory();

        return new DecimalFormat("##.##").format(percent);
    }

    public String getNetworkDataUsed(Context context) {

        long mStartRX = 0;
        long mStartTX = 0;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        if (activityManager == null)
            return null;

        activityManager.getMemoryInfo(memoryInfo);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {

            if (runningAppProcessInfo.processName.equals(context.getPackageName())) {

                mStartRX = TrafficStats.getUidRxBytes(runningAppProcessInfo.uid);
                mStartTX = TrafficStats.getUidTxBytes(runningAppProcessInfo.uid);

                if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Uh Oh!");
                    alert.setMessage("Your device does not support traffic stat monitoring.");
                    alert.show();
                }

                break;
            }

        }

        return humanReadableByteCount(mStartRX + mStartTX, true);
    }

    private static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static float getTotalStorageAvailable() {
        return getStorageAvailable(Environment.getDataDirectory());
    }

    private static float getStorageAvailable(File f) {
        StatFs stat = new StatFs(f.getPath());
        long bytesAvailable = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
            bytesAvailable = (long) stat.getBlockSizeLong() * (long) stat.getAvailableBlocksLong();
        else
            bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
        return bytesAvailable / (1024.f * 1024.f);
    }

}
