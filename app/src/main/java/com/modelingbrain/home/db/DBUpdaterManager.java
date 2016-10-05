package com.modelingbrain.home.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.modelingbrain.home.model.Model;

import java.util.ArrayList;
import java.util.List;

class DBUpdaterManager {

    @SuppressWarnings("unused")
    private static final String TAG = "DBUpdaterManager";

    public static List<Model> update(Context context, SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "DBUpdaterManager:update()");
        Log.d(TAG, "DBUpdaterManager:onUpgrade() {" + oldVersion + ";" + newVersion + "}" + "current version ->" + db.getVersion());
        List<Model> models = new ArrayList<>();
        viewTableInLog(db);
        // TODO тут какой то пиздец - 3 файла ru_RU | name TABLEOFTEST | от модели приблежения - избегания нихуя не осталось
        IDBUpdater[] updaters = new IDBUpdater[]
                {
                        new DBUpdater00(),
                        new DBUpdaterDefault()
                };
        for (IDBUpdater updater : updaters) {
            List<Model> result = updater.update(context, db);
            if (result != null) {
                if (result.size() > 0)
                    models.addAll(result);
            }
        }
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
