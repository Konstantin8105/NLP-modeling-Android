package com.modelingbrain.home.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.modelingbrain.home.GlobalFunction;
import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelID;
import com.modelingbrain.home.model.ModelState;

import org.json.JSONArray;
import org.json.JSONException;

public class DBHelperModel extends SQLiteOpenHelper {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    private static final String DATABASE_MODEL = "DataBaseOfModels";
    //	private static final String prefixTable = "TABLEOFMODEL";
    private static final int DB_Version = 20160716;

    private Context context;

    private final static String DB_ID = "id";
    private final static String DB_MODEL_ID = "modelID";
    private final static String DB_NAME = "name";
    private final static String DB_DATE = "date";
    private final static String DB_STATE = "state";
    private final static String DB_JSON_ARRAY = "json_array";

    public void addModelNormal(List<Model> models) {
        Log.d(TAG, "DBHelperModel : addModelNormal - start");
        for (Model model : models) {
            model.setState(ModelState.NORMAL);
            addModel(model);
        }
        Log.d(TAG, "DBHelperModel : addModelNormal - finish");
    }

    private void addModelNormal(SQLiteDatabase sqLiteDatabase, List<Model> models) {
        Log.d(TAG, "DBHelperModel : addModelNormal - start");
        for (Model model : models) {
            model.setState(ModelState.NORMAL);
            addModel(sqLiteDatabase,model);
        }
        Log.d(TAG, "DBHelperModel : addModelNormal - finish");
    }

    public int addModel(Model model) {
        Log.d(TAG, "DBHelperModel : addModel - start");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int id = addModel(sqLiteDatabase, model);
        sqLiteDatabase.close();
        Log.d(TAG, "DBHelperModel : addModel - finish");
        return id;
    }

    private int addModel(SQLiteDatabase sqLiteDatabase, Model model) {
        Log.d(TAG, "DBHelperModel : addModelWithoutClose - start");
        Log.d(TAG, "model = "+model.toString());
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
        long rowID = sqLiteDatabase.insert(DB.MODEL_TABLE, null, contentValues);
        Log.d(TAG, "DBHelperModel : addModelWithoutClose - end");
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
        if (ContentManagerModel.isIgnore(context,modelID))
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
//        Cursor cursor = db.query(MODEL_TABLE, null, null, null, null, null, null);

        String selectQuery = "SELECT  * FROM " + DB.MODEL_TABLE + " WHERE "
                + DB_ID + " = " + inputID;

        Log.d(TAG, selectQuery);

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
//        Log.d(TAG, "DBHelperModel : openModel - ID = " + element.getDbId());
        return element;
    }


    public ArrayList<Model> openHeader(ModelState modelState) {
        Log.d(TAG, "openHeader - start");
        if(modelState == null){
            throw new NullPointerException("modelState cannot be null");
        }
        ArrayList<Model> outputModel = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + DB.MODEL_TABLE + " WHERE "
                + DB_STATE + " = " + modelState.getValue();
        Log.d(TAG, selectQuery);
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
        Log.d(TAG, "openHeader - amount models = " + outputModel.size());
        Log.d(TAG, "openHeader - finish");
        return outputModel;
    }

    public void archiveModel(int idDB) {
        Log.d(TAG, "archiveModel - start");
        if (idDB < 0) return;
        Model model = this.openModel(idDB);
        if (model == null) return;
        model.setState(ModelState.ARCHIVE);
        updateModel(model);
        Log.d(TAG, "archiveModel - finish");
    }

    public void normalModel(int idDB) {
        Log.d(TAG, "normalModel - start");
        if (idDB < 0) return;
        Model model = this.openModel(idDB);
        if (model == null) return;
        model.setState(ModelState.NORMAL);
        updateModel(model);
        Log.d(TAG, "normalModel - finish");
    }

    public void deleteModel(int idDB) {
        Log.d(TAG, "deleteModel - start");
        if (idDB < 0) return;
        Model model = this.openModel(idDB);
        if (model == null) return;
        model.setState(ModelState.DELETE);
        updateModel(model);
        Log.d(TAG, "deleteModel - finish");
    }

    public void updateModel(Model model) {
        Log.d(TAG, "updateModel - start");
        Log.d(TAG, "updateModel - " + model.toString());
        //noinspection ConstantConditions
        if (model == null) return;
        Model modelDB = this.openModel(model.getDbId());
        if (modelDB == null) return;

        //If changed just time, then no changes
        if (modelDB.compareTo(model) && modelDB.getState() == model.getState()) {
            Log.d(TAG, "updateModel - finish");
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
        sqLiteDatabase.update(DB.MODEL_TABLE, contentValues, "id = ? ", new String[]{Integer.toString(model.getDbId())});
        Log.d(TAG, "updateModel - model.ID" + model.getDbId());
        Log.d(TAG, "updateModel - model.state" + model.getState());
        Log.d(TAG, "updateModel - finish");
    }

    public DBHelperModel(Context context) {
        super(context, DATABASE_MODEL, null, DB_Version);
        if (context == null)
            throw new RuntimeException("Context == null in DB");
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate - start");
        createDB(db);
        Log.d(TAG, "onCreate - finish");
    }

    private void createDB(SQLiteDatabase db) {
        Log.d(TAG, "createDB - start");
        String str = "create table  IF NOT EXISTS " + DB.MODEL_TABLE
                + " ( " + DB_ID + " integer primary key autoincrement "
                + " , " + DB_MODEL_ID + " text "
                + " , " + DB_NAME + " text "
                + " , " + DB_DATE + " long "
                + " , " + DB_STATE + " int  "
                + " , " + DB_JSON_ARRAY + " text "
                + " );";
        db.execSQL(str);
        Log.d(TAG, "createDB - finish");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade - start");
        createDB(db);
        List<Model> models = DBUpdaterManager.update(db, oldVersion, newVersion);
        addModelNormal(db, models);
        Log.d(TAG, "onUpgrade - finish");
    }
}

