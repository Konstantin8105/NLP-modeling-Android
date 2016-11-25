package com.modelingbrain.home.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelID;
import com.modelingbrain.home.model.ModelState;

import java.util.ArrayList;
import java.util.List;

public class DBUpdater00 implements IDBUpdater {

    private static final String prefixTable = "TABLE" + "OF" + "MODEL";
    //LOG: private final List<Model> logModels = new ArrayList<>();

    @Override
    public List<Model> update(Context context, SQLiteDatabase db) {
        List<ModelID> tables = getTablesWithPrefix(db, prefixTable);
        if (tables.size() == 0) {
            return new ArrayList<>();
        }
        List<Model> models = new ArrayList<>();
        for (ModelID table : tables) {
            List<Model> tableModels = analyzeTable(context, db, table);
            if (tableModels != null) {
                models.addAll(tableModels);
            }
        }
        {
            //drop tables
            for (ModelID table : tables) {
                db.execSQL("DROP TABLE IF EXISTS " + prefixTable + table.toString());
            }
        }
        //LOG: models.addAll(logModels);
        return models;
    }

    private List<ModelID> getTablesWithPrefix(SQLiteDatabase db, @SuppressWarnings("SameParameterValue") String prefix) {

        //LOG: Model logModel = new Model(ModelID.ID_NOTE);
        //LOG: logModel.setName("Log : getTablesWithPrefix");
        //LOG: StringBuilder textLog = new StringBuilder();

        List<ModelID> tables = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String str = c.getString(c.getColumnIndex("name"));
                //LOG: textLog.append("\n\n str = ").append(str);
                if (str.startsWith(prefix)) {
                    //LOG: textLog.append("with prefix: ").append(prefix);
                    String modelIdName = str.substring(prefix.length(), str.length());
                    //LOG: textLog.append("with modelId name: ").append(prefix);
                    for (int i = 0; i < ModelID.values().length; i++) {
                        if (modelIdName.compareTo(ModelID.values()[i].toString()) == 0) {
                            tables.add(ModelID.values()[i]);
                            //LOG: textLog.append("found");
                        }
                    }
                }
                c.moveToNext();
            }
        }
        c.close();

        //LOG: logModel.setAnswer(0, textLog.toString());
        //LOG: logModel.setMillisecond_Date(GlobalFunction.getTime());
        //LOG: logModels.add(logModel);

        return tables;
    }

    private List<Model> analyzeTable(Context context, SQLiteDatabase db, ModelID tableId) {
        List<Model> models = new ArrayList<>();

        //LOG: Model logModel = new Model(ModelID.ID_NOTE);
        //LOG: logModel.setName("Log : analyzeTable : " + tableId);
        //LOG: StringBuilder textLog = new StringBuilder();

        String tableName = prefixTable + tableId.toString();
        //LOG: textLog.append("\n\n\n\n analyzeTable table = ").append(tableName).append("\n");
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int NameColIndex = c.getColumnIndex("name");
            int DateColIndex = c.getColumnIndex("date");
            int StateColIndex = c.getColumnIndex("state");
            do {
                String name = c.getString(NameColIndex);
                long time = c.getLong(DateColIndex);
                List<String> listRight = new ArrayList<>();

                try {
                    int i = 0;
                    do {
                        //LOG: textLog.append("position = ").append(i).append("\n");
                        String line = c.getString(c.getColumnIndex("Str" + i));
                        //LOG: textLog.append("line = ").append(line).append("\n");
                        listRight.add(i, line);
                        i++;
                    } while (i < 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //LOG: textLog.append("\n createNewRecord" + "name = ").append(name).append("time = ").append(time).append("listRight = ").append(listRight).append("\n");
                if (listRight.size() != tableId.getSize() || ContentManagerModel.isIgnore(context, tableId)) {
                    Model model = new Model(ModelID.ID_NOTE);
                    model.setName(name);
                    model.setMillisecond_Date(time);
                    StringBuilder out = new StringBuilder();
                    for (String right : listRight) {
                        out.append(right);
                        out.append("\n");
                    }
                    model.setAnswer(0, out.toString());
                    models.add(model);
                } else {
                    Model model = new Model(tableId);
                    model.setName(name);
                    model.setMillisecond_Date(time);
                    for (int i = 0; i < listRight.size(); i++) {
                        model.setAnswer(i, listRight.get(i));
                    }
                    model.setState(ModelState.convert(c.getInt(StateColIndex)));
                    models.add(model);
                }
            } while (c.moveToNext());
        }
        c.close();

        //LOG: logModel.setAnswer(0, textLog.toString());
        //LOG: logModel.setMillisecond_Date(GlobalFunction.getTime());
        //LOG: logModels.add(logModel);

        return models;
    }
}
