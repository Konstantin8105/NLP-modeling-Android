package com.modelingbrain.home.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.modelingbrain.home.main.GlobalFunction;
import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelID;
import com.modelingbrain.home.model.ModelState;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public class DBHelperModel extends SQLiteOpenHelper {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().getSimpleName();

    static final String DATABASE_MODEL = "DataBaseOfModels";
    private static final int DB_Version = 20160716;

    private Context context;

    private final static String DB_ID = "id";
    private final static String DB_MODEL_ID = "modelID";
    private final static String DB_NAME = "name";
    final static String DB_DATE = "date";
    private final static String DB_STATE = "state";
    private final static String DB_JSON_ARRAY = "json_array";

    public void addModelNormal(List<Model> models) {
        Log.i(TAG, "DBHelperModel : addModelNormal - start");
        for (Model model : models) {
            model.setState(ModelState.NORMAL);
            addModel(model);
        }
        Log.i(TAG, "DBHelperModel : addModelNormal - finish");
    }

    private void addModelNormal(SQLiteDatabase sqLiteDatabase, List<Model> models) {
        Log.i(TAG, "DBHelperModel : addModelNormal - start");
        for (Model model : models) {
            model.setState(ModelState.NORMAL);
            addModel(sqLiteDatabase, model);
        }
        Log.i(TAG, "DBHelperModel : addModelNormal - finish");
    }

    public int addModel(Model model) {
        Log.i(TAG, "DBHelperModel : addModel - start");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int id = addModel(sqLiteDatabase, model);
        sqLiteDatabase.close();
        Log.i(TAG, "DBHelperModel : addModel - finish");
        return id;
    }

    private int addModel(SQLiteDatabase sqLiteDatabase, Model model) {
        Log.i(TAG, "DBHelperModel : addModelWithoutClose - start");
        Log.i(TAG, "DBHelperModel : addModel : modelName = " + model.getName());
        Log.i(TAG, "DBHelperModel : addModel : model = " + model.toString());
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_MODEL_ID, model.getModelID().toString());
        contentValues.put(DB_NAME, model.getName());
        long time = model.getMillisecond_Date();
        if (time == 0)
            contentValues.put(DB_DATE, GlobalFunction.getTime());
        else
            contentValues.put(DB_DATE, time);

        contentValues.put(DB_STATE, model.getState().getValue());
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < model.getModelID().getSize(); i++) {
            jsonArray.put(model.getAnswer(i));
        }
        contentValues.put(DB_JSON_ARRAY, jsonArray.toString());
        long rowID = sqLiteDatabase.insert(DATABASE_MODEL, null, contentValues);
        Log.i(TAG, "DBHelperModel : addModelWithoutClose - end");
        return (int) rowID;
    }

    private Model convert(Cursor cursor) {
        int IdColIndex = cursor.getColumnIndex(DB_ID);
        int ColModelID = cursor.getColumnIndex(DB_MODEL_ID);
        int NameColIndex = cursor.getColumnIndex(DB_NAME);
        int DateColIndex = cursor.getColumnIndex(DB_DATE);
        int StateColIndex = cursor.getColumnIndex(DB_STATE);
        int ColJsonArray = cursor.getColumnIndex(DB_JSON_ARRAY);

        ModelID modelID = ModelID.valueOf(cursor.getString(ColModelID));
        if (ContentManagerModel.isIgnore(context, modelID))
            return null;

        Model element = new Model(modelID);
        element.setName(cursor.getString(NameColIndex));
        element.setDbId(cursor.getInt(IdColIndex));
        element.setMillisecond_Date(cursor.getLong(DateColIndex));
        element.setState(ModelState.convert(cursor.getInt(StateColIndex)));

        try {
            JSONArray jsonArray = new JSONArray(cursor.getString(ColJsonArray));
            element.setAnswer(jsonArray);
        } catch (final JSONException ignored) {
            ignored.printStackTrace();
            return null;
        }
        return element;
    }

    public Model openModel(int inputID) {
        Model element = null;
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + DATABASE_MODEL + " WHERE "
                + DB_ID + " = " + inputID;

        Log.i(TAG, selectQuery);

        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Model temp = convert(cursor);
                if (temp != null) {
                    if (temp.getDbId() == inputID) {
                        element = temp;
                        break;
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return element;
    }


    public ArrayList<Model> openHeader(ModelState modelState) {
        Log.i(TAG, "openHeader - start");
        if (modelState == null) {
            throw new NullPointerException("modelState cannot be null");
        }
        ArrayList<Model> outputModel = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + DATABASE_MODEL + " WHERE "
                + DB_STATE + " = " + modelState.getValue();
        Log.i(TAG, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Model temp = convert(cursor);
                if (temp != null) {
                    outputModel.add(temp);
                }
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        Log.i(TAG, "openHeader - amount models = " + outputModel.size());
        Log.i(TAG, "openHeader - finish");
        return outputModel;
    }

    public void archiveModel(int idDB) {
        Log.i(TAG, "archiveModel - start");
        if (idDB < 0) return;
        Model model = this.openModel(idDB);
        if (model == null) return;
        model.setState(ModelState.ARCHIVE);
        updateModel(model);
        Log.i(TAG, "archiveModel - finish");
    }

    public void normalModel(int idDB) {
        Log.i(TAG, "normalModel - start");
        if (idDB < 0) return;
        Model model = this.openModel(idDB);
        if (model == null) return;
        model.setState(ModelState.NORMAL);
        updateModel(model);
        Log.i(TAG, "normalModel - finish");
    }

    public void deleteModel(int idDB) {
        Log.i(TAG, "deleteModel - start");
        if (idDB < 0) return;
        Model model = this.openModel(idDB);
        if (model == null) return;
        model.setState(ModelState.DELETE);
        updateModel(model);
        Log.i(TAG, "deleteModel - finish");
    }

    public void updateModel(Model model) {
        Log.i(TAG, "updateModel - start");
        //noinspection ConstantConditions
        if (model == null) return;
        Model modelDB = this.openModel(model.getDbId());
        if (modelDB == null) return;
        Log.i(TAG, "updateModel - " + model.toString());

        //If changed just time, then no changes
        if (modelDB.compareTo(model) && modelDB.getState() == model.getState()) {
            Log.i(TAG, "updateModel - finish");
            return;
        }
        //If changed text of model, then update time
        long time = modelDB.getMillisecond_Date();
        if (!modelDB.compareTo(model)) {
            time = GlobalFunction.getTime();
        }
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        contentValues.put(DB_MODEL_ID, model.getModelID().toString());
        contentValues.put(DB_NAME, model.getName());
        contentValues.put(DB_DATE, time);
        contentValues.put(DB_STATE, model.getState().getValue());
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < model.getModelID().getSize(); i++) {
            jsonArray.put(model.getAnswer(i));
        }
        contentValues.put(DB_JSON_ARRAY, jsonArray.toString());
        sqLiteDatabase.update(DATABASE_MODEL, contentValues, "id = ? ", new String[]{Integer.toString(model.getDbId())});
        Log.i(TAG, "updateModel - model.ID    - " + model.getModelID());
        Log.i(TAG, "updateModel - model.state - " + model.getState());
        Log.i(TAG, "updateModel - finish");
    }

    public DBHelperModel(Context context) {
        super(context, DATABASE_MODEL, null, DB_Version);
        if (context == null)
            throw new RuntimeException("Context == null in DB");
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate - start");
        createDB(db);
        Log.i(TAG, "onCreate - finish");
    }

    private void createDB(SQLiteDatabase db) {
        Log.i(TAG, "createDB - start");
        String str = "create table  IF NOT EXISTS " + DATABASE_MODEL
                + " ( " + DB_ID + " integer primary key autoincrement "
                + " , " + DB_MODEL_ID + " text "
                + " , " + DB_NAME + " text "
                + " , " + DB_DATE + " long "
                + " , " + DB_STATE + " int  "
                + " , " + DB_JSON_ARRAY + " text "
                + " );";
        db.execSQL(str);
        Log.i(TAG, "createDB - finish");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade - start");
        createDB(db);
        List<Model> models = DBUpdaterManager.update(context, db, oldVersion, newVersion);
        addModelNormal(db, models);
        Log.i(TAG, "onUpgrade - finish");
    }
}

