package com.dubinets.requisition.databasehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.dubinets.requisition.db.Client;
import com.dubinets.requisition.db.ClientDao;
import com.dubinets.requisition.db.Count;
import com.dubinets.requisition.db.CountDao;
import com.dubinets.requisition.db.CrossItineraryClient;
import com.dubinets.requisition.db.CrossItineraryClientDao;
import com.dubinets.requisition.db.DaoMaster;
import com.dubinets.requisition.db.DaoSession;
import com.dubinets.requisition.db.DayOfWeekDao;
import com.dubinets.requisition.db.Item;
import com.dubinets.requisition.db.ItemDao;
import com.dubinets.requisition.db.ItineraryDao;
import com.dubinets.requisition.db.PhotoDao;
import com.dubinets.requisition.db.Shelftime;
import com.dubinets.requisition.db.ShelftimeDao;
import com.dubinets.requisition.db.Spot;
import com.dubinets.requisition.db.SpotDao;
import com.dubinets.requisition.db.TelephoneDao;
import com.dubinets.requisition.db.TypeDao;
import com.dubinets.requisition.db.Week;
import com.dubinets.requisition.db.WeekDao;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dubinets on 22.08.2016.
 */
public class DatabaseHelper {
    private final String DATABASE_PATH = "/data/data/com.dubinets.requisition/databases/";
    private final String DATABASE_NAME = "veles.db";

    private static ClientDao                    clientDao;
    private static CountDao                     countDao;
    private static ItemDao                      itemDao;
    private static PhotoDao                     photoDao;
    private static ShelftimeDao                 shelftimeDao;
    private static SpotDao                      spotDao;
    private static TelephoneDao                 telephoneDao;
    private static ItineraryDao                 itineraryDao;
    private static CrossItineraryClientDao      crossItineraryClientDao;
    private static WeekDao                      weekDao;
    private static DayOfWeekDao                 dayOfWeekDao;
    private static TypeDao                      typeDao;

    private static DatabaseHelper               instance;

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

                new DatabasePopulator(context).populateDatabase(db);
                //isEmpty = false;
            }

            @Override
            public void onUpgrade(Database db, int oldVersion, int newVersion) {
                super.onUpgrade(db, oldVersion, newVersion);
                //if(!isEmpty) new DatabasePopulator(context).populateDatabase(database);
            }
        } ;

        database    = helper.getWritableDatabase();

        daoMaster   = new DaoMaster(database);
        daoSession  = daoMaster.newSession();

        clientDao                   = daoSession.getClientDao();
        countDao                    = daoSession.getCountDao();
        itemDao                     = daoSession.getItemDao();
        photoDao                    = daoSession.getPhotoDao();
        shelftimeDao                = daoSession.getShelftimeDao();
        spotDao                     = daoSession.getSpotDao();
        telephoneDao                = daoSession.getTelephoneDao();
        itineraryDao                = daoSession.getItineraryDao();
        crossItineraryClientDao     = daoSession.getCrossItineraryClientDao();
        weekDao                     = daoSession.getWeekDao();
        dayOfWeekDao                = daoSession.getDayOfWeekDao();
        typeDao                     = daoSession.getTypeDao();
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

    public static ItineraryDao getItineraryDao() { return itineraryDao; }

    public static WeekDao getWeekDao() { return weekDao; }

    public static DayOfWeekDao getDayOfWeekDao() { return dayOfWeekDao; }

    public static TypeDao getTypeDao() { return typeDao; }

    public static CrossItineraryClientDao getCrossItineraryClientDao() { return crossItineraryClientDao; }

    public static List<Week> loadAllWeeksSorted() {
        Query<Week> weekSortedListQuery = null;
        synchronized (DatabaseHelper.class) {
            if (weekSortedListQuery == null) {
                QueryBuilder<Week> queryBuilder = DatabaseHelper.getWeekDao().queryBuilder();
                queryBuilder.orderDesc(com.dubinets.requisition.db.WeekDao.Properties.Date_start_week);
                weekSortedListQuery = queryBuilder.build();
            }
        }
        Query<Week> query = weekSortedListQuery.forCurrentThread();
        return query.list();
    }

    public static List<Spot> _queryClientItinerary_SpotList(long itinerary_id, long client_id) {
        Query<Spot> clientItinerary_SpotListQuery = null;
        synchronized (DatabaseHelper.class) {
            if (clientItinerary_SpotListQuery == null) {
                QueryBuilder<Spot> queryBuilder = DatabaseHelper.getSpotDao().queryBuilder();
                queryBuilder.where(
                        com.dubinets.requisition.db.SpotDao.Properties.Itinerary_id.eq(null),
                        com.dubinets.requisition.db.SpotDao.Properties.Client_id.eq(null));
                clientItinerary_SpotListQuery = queryBuilder.build();
            }
        }
        Query<Spot> query = clientItinerary_SpotListQuery.forCurrentThread();
        query.setParameter(0, itinerary_id);
        query.setParameter(1, client_id);
        return query.list();
    }

    public static List<Item> itemListFiltered(long itinerary_id, long client_id) {
        List<Item> items = itemDao.loadAll();
        List<Spot> spots = _queryClientItinerary_SpotList(itinerary_id, client_id);
        List<Item> itemsUsed = new ArrayList<>();

        for(Spot spot : spots) {
            Item item = DatabaseHelper.getItemDao().load(spot.getItem_id());
            itemsUsed.add(item);
        }

        items.removeAll(itemsUsed);
        return items;
    }

    public static List<Client> clientListFiltered(long itinerary_id) {
        List<Client> clients = DatabaseHelper.clientDao.loadAll();
        List<Client> clientsUsed = new ArrayList<>();

        for(CrossItineraryClient crossItineraryClient : DatabaseHelper.getItineraryDao().load(itinerary_id).getCrossItineraryClientList()) {
            Client client = DatabaseHelper.getClientDao().load(crossItineraryClient.getClient_id());
            clientsUsed.add(client);
        }
        clients.removeAll(clientsUsed);
        return clients;
    }

    public static Long getCountIdByValue(Integer value) {
        List<Count> counts = getCountDao().loadAll();

        for(Count count : counts) {
            if(count.getCount().equals(value))
                return count.getId();
        }

        Count newCount = new Count(null, value);
        getCountDao().insert(newCount);

        return newCount.getId();
    }

    public static Long getShelftimeByValue(Integer value) {
        List<Shelftime> shelftimes = getShelftimeDao().loadAll();

        for(Shelftime shelftime : shelftimes) {
            if(shelftime.getShelftime().equals(value))
                return shelftime.getId();
        }

        Shelftime newShelftime = new Shelftime(null, value);
        getShelftimeDao().insert(newShelftime);

        return newShelftime.getId();
    }
}
