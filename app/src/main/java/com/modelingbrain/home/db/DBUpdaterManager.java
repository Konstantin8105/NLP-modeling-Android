package com.modelingbrain.home.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.modelingbrain.home.model.Model;

import java.util.ArrayList;
import java.util.List;

public class DBUpdaterManager {

    @SuppressWarnings("unused")
    private static final String TAG = "DBUpdaterManager";

    public static List<Model> update(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "DBUpdaterManager:update()");
        Log.d(TAG, "DBUpdaterManager:onUpgrade() {" + oldVersion + ";" + newVersion + "}" + "current version ->" + db.getVersion());
        List<Model> models = new ArrayList<>();
        viewTableInLog(db);
        //if (oldVersion <= -1000000 && newVersion <= 20160716) {
            DBUpdater00 updater = new DBUpdater00();
            models.addAll(updater.update(db));
        //}
        DBUpdaterDefault updaterDefault = new DBUpdaterDefault();
        models.addAll(updaterDefault.update(db));
        return models;
    }

    private static void viewTableInLog(SQLiteDatabase db) {
        Log.d(TAG, "\nviewTableInLog");
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String str = c.getString(c.getColumnIndex("name"));
                Log.d(TAG, "\t|\tListTables - " + str);
                c.moveToNext();
            }
        }
        c.close();
        Log.d(TAG, "\n");
    }
}
