package app.com.bookdb.controller;

import android.util.Log;

/**
 * Created by Ori Syun on 1/25/2018.
 */

public class SysLog {
    private static SysLog instance;

    public SysLog() {
    }

    public static SysLog getInstance() {
        if (instance == null) {
            instance = new SysLog();
        }

        return instance;
    }

    public void sendLog(String tag, String log) {
        short maxLogSize = 1000;
        if (log.length() >= maxLogSize) {
            for (int i = 0; i <= log.length() / maxLogSize; ++i) {
                int start = i * maxLogSize;
                int end = (i + 1) * maxLogSize;
                end = end > log.length() ? log.length() : end;
                Log.e(tag, log.substring(start, end));
            }
        } else {
            Log.e(tag, log);
        }


    }
}
