package com.modelingbrain.home.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.modelingbrain.home.main.GlobalFunction;
import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelID;

import java.util.ArrayList;
import java.util.List;

public class DBUpdater00 implements IDBUpdater {

    private static final String prefixTable = "TABLE" + "OF" + "MODEL";
    private final List<Model> logModels = new ArrayList<>();

    //TODO remove strange logs

    @Override
    public List<Model> update(Context context, SQLiteDatabase db) {
        List<ModelID> tables = getTablesWithPrefix(db, prefixTable);
        if (tables.size() == 0) {
            return new ArrayList<>();
        }
        List<Model> models = new ArrayList<>();
        for (int i = 0; i < tables.size(); i++) {
            List<Model> tableModels = analyzeTable(context, db, tables.get(i));
            if (tableModels != null) {
                models.addAll(tableModels);
            }
        }
        {
            //drop tables
            for (int i = 0; i < tables.size(); i++) {
                db.execSQL("DROP TABLE IF EXISTS " + prefixTable + tables.get(i).toString());
            }
        }
        models.addAll(logModels);
        return models;
    }

    private List<ModelID> getTablesWithPrefix(SQLiteDatabase db, @SuppressWarnings("SameParameterValue") String prefix) {

        Model logModel = new Model(ModelID.ID_NOTE);
        logModel.setName("Log : getTablesWithPrefix");
        StringBuilder textLog = new StringBuilder();

        List<ModelID> tables = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String str = c.getString(c.getColumnIndex("name"));
                textLog.append("\n\nstr = ").append(str);
                if (str.startsWith(prefix)) {
                    textLog.append("with prefix: ").append(prefix);
                    String modelIdName = str.substring(prefix.length(), str.length());
                    textLog.append("with modelId name: ").append(prefix);
                    for (int i = 0; i < ModelID.values().length; i++) {
                        if (modelIdName.compareTo(ModelID.values()[i].toString()) == 0) {
                            tables.add(ModelID.values()[i]);
                            textLog.append("found");
                        }
                    }
                }
                c.moveToNext();
            }
        }
        c.close();

        logModel.setAnswer(0, textLog.toString());
        logModel.setMillisecond_Date(GlobalFunction.getTime());
        logModels.add(logModel);

        return tables;
    }

    private List<Model> analyzeTable(Context context, SQLiteDatabase db, ModelID tableId) {
        List<Model> models = new ArrayList<>();

        Model logModel = new Model(ModelID.ID_NOTE);
        logModel.setName("Log : analyzeTable : " + tableId);
        StringBuilder textLog = new StringBuilder();

        if (!ContentManagerModel.isIgnore(context, tableId)) {
            String tableName = prefixTable + tableId.toString();
            textLog.append("\n\n\n\nanalyzeTable table = ").append(tableName).append("\n");
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
                            textLog.append("position = ").append(i).append("\n");
                            String line = c.getString(c.getColumnIndex("Str" + i));
                            textLog.append("line = ").append(line).append("\n");
                            listRight.add(i, line);
                            i++;
                        } while (i < 200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    textLog.append("\ncreateNewRecord" + "name = ").append(name).append("time = ").append(time).append("listRight = ").append(listRight).append("\n");
                    if (listRight.size() != tableId.getSize()) {
                        Model model = new Model(ModelID.ID_NOTE);
                        model.setName(name);
                        model.setMillisecond_Date(time);
                        String out = "";//new String();
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
        } else {
            textLog.append("ignored");
        }

        logModel.setAnswer(0, textLog.toString());
        logModel.setMillisecond_Date(GlobalFunction.getTime());
        logModels.add(logModel);

        return models;
    }
}
