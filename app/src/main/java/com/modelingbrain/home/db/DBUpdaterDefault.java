package com.modelingbrain.home.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelID;

import java.util.ArrayList;
import java.util.List;

import static com.modelingbrain.home.db.DBHelperModel.DB_DATE;

public class DBUpdaterDefault implements IDBUpdater {

    @Override
    public List<Model> update(Context context, SQLiteDatabase db) {
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
            List<Model> temp = convertTableToNote(db, tableName);
            if (temp != null)
                models.addAll(temp);
        }
        return models;
    }

    private List<Model> convertTableToNote(SQLiteDatabase db, String tableName) {
        List<Model> models = new ArrayList<>();
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        String[] columns = c.getColumnNames();
        int[] columnsIndex = new int[columns.length];
        for (int i = 0; i < columns.length; i++) {
            columnsIndex[i] = c.getColumnIndex(columns[i]);
        }
        int position = 0;
        if (c.moveToFirst()) {
            do {
                Model model = new Model(ModelID.ID_NOTE);
                model.setName(tableName + " : " + (position++));
                StringBuilder note = new StringBuilder();
                for (int i = 0; i < columnsIndex.length; i++) {
                    note.append(columns[i]).append(" ").append(c.getString(columnsIndex[i])).append(";");
                }
                model.setAnswer(0, note.toString());
                int datePosition = c.getColumnIndex(DB_DATE);
                if (datePosition >= 0) {
                    model.setMillisecond_Date(c.getLong(datePosition));
                }
                models.add(model);
            } while (c.moveToNext());
        }
        c.close();
        return models;
    }
}
