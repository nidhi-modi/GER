package com.tandg.qualitysheet.helper;

import android.util.Log;

/**
 * Created by root on 23/12/17.
 */

public class ApplicationHelper {

    private static final String TAG = ApplicationHelper.class.getSimpleName();
    private static ApplicationHelper _instance;

    public synchronized static ApplicationHelper getInstance() {
        if (_instance == null) {
            _instance = new ApplicationHelper();
            Log.i(TAG, "instance of ActivityHelper created.");
        }
        return _instance;
    }
}
