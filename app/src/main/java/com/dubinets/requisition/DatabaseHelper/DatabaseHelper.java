package com.dubinets.requisition.databasehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dubinets.requisition.db.ClientDao;
import com.dubinets.requisition.db.CountDao;
import com.dubinets.requisition.db.DaoMaster;
import com.dubinets.requisition.db.DaoSession;
import com.dubinets.requisition.db.ItemDao;
import com.dubinets.requisition.db.OrderDao;
import com.dubinets.requisition.db.PhotoDao;
import com.dubinets.requisition.db.ShelftimeDao;
import com.dubinets.requisition.db.SpotDao;
import com.dubinets.requisition.db.TelephoneDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by dubinets on 22.08.2016.
 */
public class DatabaseHelper {
    private final String DATABASE_NAME = "veles.db";

    private static ClientDao    clientDao;
    private static CountDao     countDao;
    private static ItemDao      itemDao;
    private static OrderDao     orderDao;
    private static PhotoDao     photoDao;
    private static ShelftimeDao shelftimeDao;
    private static SpotDao      spotDao;
    private static TelephoneDao telephoneDao;

    private static DatabaseHelper       instance;

    private static DaoMaster.OpenHelper    helper;
    private static SQLiteDatabase          database;
    private static DaoMaster               daoMaster;
    private static DaoSession              daoSession;

    private DatabaseHelper(final Context context) {
        helper      = new DaoMaster.DevOpenHelper(context, DATABASE_NAME, null) {
            boolean isEmpty = true;

            @Override
            public void onCreate(Database db) {
                super.onCreate(db);
                new DatabasePopulator(context).populateDatabase(database);
                isEmpty = false;
            }

            @Override
            public void onUpgrade(Database db, int oldVersion, int newVersion) {
                super.onUpgrade(db, oldVersion, newVersion);
                if(!isEmpty) new DatabasePopulator(context).populateDatabase(database);
            }
        } ;
        database    = helper.getWritableDatabase();

        daoMaster   = new DaoMaster(database);
        daoSession  = daoMaster.newSession();

        clientDao      = daoSession.getClientDao();
        countDao       = daoSession.getCountDao();
        itemDao        = daoSession.getItemDao();
        orderDao       = daoSession.getOrderDao();
        photoDao       = daoSession.getPhotoDao();
        shelftimeDao   = daoSession.getShelftimeDao();
        spotDao        = daoSession.getSpotDao();
        telephoneDao   = daoSession.getTelephoneDao();
    }

    public static void setHelper(Context context) {
        instance = new DatabaseHelper(context);
    }

    public static DatabaseHelper getHelper() {
        return instance;
    }

    public static void releaseHelper() {
        database.close();
        helper.close();
        daoMaster = null;
        daoSession = null;
        instance = null;
    }

    public static ClientDao getClientDao() {
        return clientDao;
    }

    public static CountDao getCountDao() {
        return countDao;
    }

    public static ItemDao getItemDao() {
        return itemDao;
    }

    public static OrderDao getOrderDao() {
        return orderDao;
    }

    public static PhotoDao getPhotoDao() {
        return photoDao;
    }

    public static ShelftimeDao getShelftimeDao() {
        return shelftimeDao;
    }

    public static SpotDao getSpotDao() {
        return spotDao;
    }

    public static TelephoneDao getTelephoneDao() {
        return telephoneDao;
    }
}
