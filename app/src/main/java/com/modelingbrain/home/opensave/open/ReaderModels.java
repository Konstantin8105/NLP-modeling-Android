package com.modelingbrain.home.opensave.open;

import android.os.AsyncTask;

import com.modelingbrain.home.GlobalFunction;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelID;
import com.modelingbrain.home.opensave.SaveOpenActivity;
import com.modelingbrain.home.opensave.ValuesIO;

import java.io.IOException;
import java.util.ArrayList;

public class ReaderModels extends Reader {

    private ArrayList<Model> models;
    private int amount;
    private int position;

    public ReaderModels(AsyncTask<Void, String, Void> task, SaveOpenActivity activity, int amount) throws IOException {
        super(task, activity);
        this.amount = amount;
    }

    @Override
    void init() {
        models = new ArrayList<>();
        position = 0;
    }

    @Override
    void action() throws IOException {
        reader.beginObject();
        Model model = new Model(ModelID.ID_NOTE);
        boolean addModel = analyze(model);
        if (addModel)
            models.add(model);
        reader.endObject();
        position++;
        GlobalFunction.pause();
    }

    @Override
    int getPositionProgress() {
//        Log.d(TAG, "getPositionProgress: start");
//        Log.d(TAG, "getPositionProgress: position = "+position+" amount="+amount);
        if (amount == 0)
            return 0;
//        Log.d(TAG, "getPositionProgress: finish");
        return (int) ((float) position / (float) amount * 100f);
    }

    private boolean analyze(Model model) throws IOException {
        if(model == null)
            return false;
        // TODO: 2/6/16 add reading version of output file
        ModelID modelID = model.getModelID();
//        Log.d(TAG, "readingFile: start");
        while (reader.hasNext()) {
            String name = reader.nextName();
//            Log.d(TAG, "readingFile: name = " + name);
            switch (name) {
                case ValuesIO.TYPE: {
//                Log.d(TAG, "readingFile: type = " + ValuesIO.TYPE);
                    String str = reader.nextString();
//                Log.d(TAG, "readingFile: str = " + str);
                    modelID = ModelID.valueOf(str);
                    model.setModelID(modelID);
                    break;
                }
                case ValuesIO.NAME: {
//                Log.d(TAG, "readingFile: type = " + ValuesIO.NAME);
                    String str = reader.nextString();
//                Log.d(TAG, "readingFile: str = " + str);
                    model.setName(str);
                    break;
                }
                case ValuesIO.TIME: {
//                Log.d(TAG, "readingFile: type = " + ValuesIO.TIME);
                    long millisecond = reader.nextLong();
//                Log.d(TAG, "readingFile: str = " + lstr);
                    model.setMillisecond_Date(millisecond);
                    break;
                }
                case ValuesIO.RIGHT: {
//                Log.d(TAG, "readingFile: type = " + ValuesIO.RIGHT);
                    ArrayList<String> temp_str = new ArrayList<>();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        String str = reader.nextString();
//                    Log.d(TAG, "readingFile: str = " + str);
                        temp_str.add(str);
                    }
                    reader.endArray();
                    if (modelID.getSize() == temp_str.size()) {
                        for (int i = 0; i < temp_str.size(); i++) {
//                        Log.d(TAG, "readingFile: i = " + i + " size = " + temp_str.size());
//                        Log.d(TAG, "readingFile: str = " + temp_str.get(i));
                            model.setAnswer(i, temp_str.get(i));
                        }
                    }
                    break;
                }
                default:
            }
        }
//        Log.d(TAG, "readingFile: finish");
        return true;
    }

    public ArrayList<Model> getModels() {
        return models;
    }
}
