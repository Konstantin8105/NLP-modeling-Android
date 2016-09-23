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
    private final int amount;
    private int position;

    public ReaderModels(AsyncTask<Void, String, Void> task, SaveOpenActivity activity, int amount) {
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
        if (amount == 0)
            return 0;
        return (int) ((float) position / (float) amount * 100f);
    }

    private boolean analyze(Model model) throws IOException {
        if (model == null)
            return false;
        // TODO: 2/6/16 add reading version of output file
        ModelID modelID = model.getModelID();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case ValuesIO.TYPE: {
                    String str = reader.nextString();
                    modelID = ModelID.valueOf(str);
                    model.setModelID(modelID);
                    break;
                }
                case ValuesIO.NAME: {
                    String str = reader.nextString();
                    model.setName(str);
                    break;
                }
                case ValuesIO.TIME: {
                    long millisecond = reader.nextLong();
                    model.setMillisecond_Date(millisecond);
                    break;
                }
                case ValuesIO.RIGHT: {
                    ArrayList<String> temp_str = new ArrayList<>();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        String str = reader.nextString();
                        temp_str.add(str);
                    }
                    reader.endArray();
                    if (modelID.getSize() == temp_str.size()) {
                        for (int i = 0; i < temp_str.size(); i++) {
                            model.setAnswer(i, temp_str.get(i));
                        }
                    }
                    break;
                }
                default:
            }
        }
        return true;
    }

    public ArrayList<Model> getModels() {
        return models;
    }
}
