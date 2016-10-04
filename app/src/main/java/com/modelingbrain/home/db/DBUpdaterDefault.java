package com.modelingbrain.home.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelID;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBUpdaterDefault implements IDBUpdater {

    private static final String TAG = DBUpdaterDefault.class.toString();

    @Override
    public List<Model> update(Context context, SQLiteDatabase db) {
        Log.d(TAG, "\nupdate");
        List<String> tableNames = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                tableNames.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        c.close();
        tableNames.removeAll(DB.listDb);
        List<Model> models = new ArrayList<>();
        for (String tableName : tableNames) {
            Model temp = convertTableToNote(db, tableName);
            if(temp != null)
                models.add(temp);
        }
        Log.d(TAG, "\nend of update");
        return models;
    }

    private Model convertTableToNote(SQLiteDatabase db, String tableName) {
        Log.d(TAG, "\nconvertTableToNote");
        Log.d(TAG, "\ntableName = " + tableName);
        Model model = null;
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        String[] columns = c.getColumnNames();
        int[] columnsIndex = new int[columns.length];
        for (int i = 0; i < columns.length; i++) {
            columnsIndex[i] = c.getColumnIndex(columns[i]);
        }
        if (c.moveToFirst()) {
            do {
                model = new Model(ModelID.ID_NOTE);
                model.setName((new Date()).getTime() + "");
                String note = "";//new String();
                for (int i = 0; i < columnsIndex.length; i++) {
                    note += columns[i] + " " + c.getString(columnsIndex[i]) + ";";
                }
                model.setAnswer(0, note);
                Log.d(TAG,note);
            } while (c.moveToNext());
        }
        c.close();
        Log.d(TAG, "\nend of convertTableToNote");
        return model;
    }
}
