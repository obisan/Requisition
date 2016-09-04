package com.dubinets.requisition.databasehelper;

import android.app.Application;

import com.dubinets.requisition.databasehelper.DatabaseHelper;

/**
 * Created by dubinets on 22.08.2016.
 */
public class DaoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.setHelper(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DatabaseHelper.releaseHelper();
    }
}
