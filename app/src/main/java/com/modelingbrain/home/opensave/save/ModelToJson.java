package com.modelingbrain.home.opensave.save;

import android.os.Environment;

import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.opensave.ValuesIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ModelToJson {

    public static JSONObject convertModelToJson(Model model) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(ValuesIO.JsonElemenets.TYPE, model.getModelID().toString());
            obj.put(ValuesIO.JsonElemenets.NAME, model.getName());
            obj.put(ValuesIO.JsonElemenets.TIME, Long.valueOf(model.getMillisecond_Date()).toString());
            JSONArray list = new JSONArray();
            for (int nn = 0; nn < model.getModelID().getSize(); nn++)
                list.put(model.getAnswer(nn));
            obj.put(ValuesIO.JsonElemenets.RIGHT, list);
        } catch (final JSONException ignored) {
            ignored.printStackTrace();
            return null;
        }
        return obj;
    }

    public static void saveJsonStringInFile(String string) {
        FileWriter file = null;
        try {
            File sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String full_path = sdPath.getAbsolutePath() + File.separator + ValuesIO.OUTPUT_FILENAME_JSON;
            if (!sdPath.exists())
                if(!sdPath.mkdirs())
                    return;
            file = new FileWriter(full_path);
            file.write(string);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            if (file != null)
                try {
                    file.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
    }
}
