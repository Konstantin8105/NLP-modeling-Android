package com.modelingbrain.home.opensave.open;

import android.os.AsyncTask;

import com.modelingbrain.home.main.GlobalFunction;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.model.ModelID;
import com.modelingbrain.home.opensave.SaveOpenActivity;
import com.modelingbrain.home.opensave.ValuesIO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReaderModels extends Reader {

    private ArrayList<Model> models;
    private final int amount;
    private int position;

    public ReaderModels(AsyncTask<Void, String, Void> task, SaveOpenActivity activity, String filename, int amount, ValuesIO.formats format) {
        super(task, activity, filename, format);
        this.amount = amount;
    }

    @Override
    void init() {
        models = new ArrayList<>();
        position = 0;
    }

    @Override
    void action() throws IOException {
        Model model = new Model(ModelID.ID_NOTE);
        switch (format) {
            case JSON: {
                readerJson.beginObject();
                boolean addModel = analyzeJson(model);
                if (addModel)
                    models.add(model);
                readerJson.endObject();
                break;
            }
            case XML: {
                boolean addModel = analyzeXml(model);
                if (addModel)
                    models.add(model);
                break;
            }
            default:
                throw new FileNotFoundException("Cannot open that format: " + format);
        }
        position++;
        GlobalFunction.pause();
    }

    private boolean analyzeXml(Model model) {
        if (model == null)
            return false;
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            if (i == 0)
                model.setModelID(ModelID.valueOf(xpp.getAttributeValue(i)));
            else if (i == 1) model.setName(xpp.getAttributeValue(i));
            else if (i == 2)
                model.setMillisecond_Date(Long.valueOf(xpp.getAttributeValue(i)));
            else model.setAnswer(i - 3, xpp.getAttributeValue(i));
        }
        return true;
    }

    @Override
    int getPositionProgress() {
        if (amount <= 0)
            return 0;
        return (int) ((float) position / (float) amount * 100f);
    }

    private boolean analyzeJson(Model model) throws IOException {
        if (model == null)
            return false;

        ModelID modelID = model.getModelID();
        while (readerJson.hasNext()) {
            String name = readerJson.nextName();
            switch (name) {
                case ValuesIO.JsonElemenets.TYPE: {
                    String str = readerJson.nextString();
                    modelID = ModelID.valueOf(str);
                    model.setModelID(modelID);
                    break;
                }
                case ValuesIO.JsonElemenets.NAME: {
                    String str = readerJson.nextString();
                    model.setName(str);
                    break;
                }
                case ValuesIO.JsonElemenets.TIME: {
                    long millisecond = readerJson.nextLong();
                    model.setMillisecond_Date(millisecond);
                    break;
                }
                case ValuesIO.JsonElemenets.RIGHT: {
                    ArrayList<String> temp_str = new ArrayList<>();
                    readerJson.beginArray();
                    while (readerJson.hasNext()) {
                        String str = readerJson.nextString();
                        temp_str.add(str);
                    }
                    readerJson.endArray();
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

    public List<Model> getModels() {
        return models;
    }
}
