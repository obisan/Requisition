package com.dubinets.requisition.databasehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dubinets.requisition.R;

import org.greenrobot.greendao.database.Database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by dubinets on 22.08.2016.
 */
class DatabasePopulator {
    private Context context;

    public DatabasePopulator(Context context) {
        this.context = context;
    }

    public void populateDatabase(Database db) {
        execResource(db, context.getResources().openRawResource(R.raw.veles_fill));
    }

    private void execResource(Database db, InputStream stream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            while (reader.ready()) {
                String sql = reader.readLine();

                if(sql.isEmpty())           continue;
                if(!sql.split(" ")[0]
                        .toLowerCase()
                        .equals("insert")) continue;
                db.execSQL(sql);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
