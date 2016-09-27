package com.modelingbrain.home.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelID;

import java.util.ArrayList;
import java.util.List;

public class DBUpdater00 implements IDBUpdater {

    private String log = new String();

    private static final String prefixTable = "TABLE" + "OF" + "MODEL";

    @Override
    public List<Model> update(SQLiteDatabase db) {
        List<ModelID> tables = getTablesWithPrefix(db, prefixTable);
        if (tables.size() == 0) {
            return new ArrayList<>();
        }
        List<Model> models = new ArrayList<>();
        for (int i = 0; i < tables.size(); i++) {
            List<Model> tableModels = analyzeTable(db, tables.get(i));
            if (tableModels != null) {
                models.addAll(tableModels);
            }
        }
//        {
//            Model model = new Model(ModelID.ID_NOTE);
//            model.setName("Tables");
//            String out = new String();
//            for (int i = 0; i < tables.size(); i++) {
//                out += tables.get(i).toString() + "\n";
//            }
//            model.setAnswer(0, out);
//            models.add(model);
//        }
        {
            //drop tables
            for (int i = 0; i < tables.size(); i++) {
                db.execSQL("DROP TABLE IF EXISTS " + prefixTable + tables.get(i).toString());
            }
        }
//        {
//            // add logging
//            Model logResult = new Model(ModelID.ID_NOTE);
//            logResult.setName("log");
//            logResult.setAnswer(0, log);
//            models.add(logResult);
//        }
        return models;
    }

    private List<ModelID> getTablesWithPrefix(SQLiteDatabase db, String prefix) {
        log += "found tables\n";
        List<ModelID> tables = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String str = c.getString(c.getColumnIndex("name"));
                log += "tableName = " + str + "\n";
                if (str.startsWith(prefix)) {
                    String temp = str.substring(prefix.length(), str.length());
                    log += "add table -- " + temp + "\n";
                    for (int i = 0; i < ModelID.values().length; i++) {
                        if (temp.compareTo(ModelID.values()[i].toString()) == 0) {
                            tables.add(ModelID.values()[i]);
                            log += "ModelID " + ModelID.values()[i].toString() + "\n";
                        }
                    }
                }
                c.moveToNext();
            }
        }
        c.close();
        //
        log += "\ngetTablesWithPrefix size = " + tables.size() + "\n";
        for (int i = 0; i < tables.size(); i++) {
            log += "getTablesWithPrefix i = " + i + "\nName = " + tables.get(i) + "\n";
        }
        //
        return tables;
    }

    private List<Model> analyzeTable(SQLiteDatabase db, ModelID tableId) {
        List<Model> models = new ArrayList<>();
        String tableName = prefixTable + tableId.toString();
        log += "\n\n\n\nanalyzeTable table = " + tableName + "\n";
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int NameColIndex = c.getColumnIndex("name");
            int DateColIndex = c.getColumnIndex("date");
            do {
                String name = c.getString(NameColIndex);
                long time = c.getLong(DateColIndex);
                List<String> listRight = new ArrayList<>();

                try {
                    int i = 0;
                    do {
                        log += "position = " + i + "\n";
                        String line = c.getString(c.getColumnIndex("Str" + i));
                        log += "line = " + line + "\n";
                        listRight.add(i, line);
                        i++;
                    } while (i < 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // TODO not state = delete
                log += "\ncreateNewRecord"
                        + "name = " + name
                        + "time = " + time
                        + "listRight = " + listRight
                        + "\n";
                if (listRight.size() != tableId.getSize()) {
                    Model model = new Model(ModelID.ID_NOTE);
                    model.setName(name);
                    model.setMillisecond_Date(time);
                    String out = new String();
                    for (int i = 0; i < listRight.size(); i++) {
                        out += listRight.get(i);
                    }
                    model.setAnswer(0, out);
                    models.add(model);
                } else {
                    Model model = new Model(tableId);
                    model.setName(name);
                    model.setMillisecond_Date(time);
                    for (int i = 0; i < listRight.size(); i++) {
                        model.setAnswer(i, listRight.get(i));
                    }
                    models.add(model);
                }
            } while (c.moveToNext());
        }
        c.close();
        return models;
    }
}
