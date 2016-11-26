package com.modelingbrain.home.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.modelingbrain.home.R;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.opensave.ValuesIO;
import com.modelingbrain.home.opensave.save.ModelToJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * License: LGPL ver.3
 *
 * @author Izyumov Konstantin
 */

public class ShareModels {
    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    private ArrayList<Model> models;
    private final Context context;

    public ShareModels(int[] modelId, Context context) {
        if(models == null)
            models = new ArrayList<>();
        DBHelperModel dbHelperModel = new DBHelperModel(context);
        for (int aModelId : modelId) {
            models.add(dbHelperModel.openModel(aModelId));
        }
        this.context = context;
    }



    public void showToast() {
        int count = models.size();
        if(count > 0){
            String text = context.getResources().getQuantityString(R.plurals.share_models,count,count);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    public void createEmail() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        email.putExtra(Intent.EXTRA_SUBJECT, R.string.result_of_modeling);
        String message;
        message = "";
        for (Model model : models) {
            message += context.getString(R.string.name_of_model_) + model.getName();
            message += "\n";
            for (int k = 0; k < model.getModelID().getSize(); k++) {
                message += context.getResources().getStringArray(model.getModelID().getResourceQuestion())[k+1];
                message += " :\n";
                message += model.getAnswer()[k];
                message += "\n";
            }
            message += "\n\n\n";
        }
        email.putExtra(Intent.EXTRA_TEXT, message);

        Log.i(TAG, "CreateEMAIL: Message -> " + message);
        Log.i(TAG, "CreateEMAIL: Add file -> " + ValuesIO.OUTPUT_FILENAME_JSON);

        // add file in folder Downloads
        JSONArray jsonArrayGlobal = createJson();
        ModelToJson.saveJsonStringInFile(jsonArrayGlobal.toString());

        File sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.i(TAG, "CreateEMAIL: Add file -> " + sdPath);
        sdPath = new File(sdPath.getAbsolutePath());
        Log.i(TAG, "CreateEMAIL: Add file -> " + sdPath);
        try {
            email.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + sdPath.getAbsolutePath() + "/" + ValuesIO.OUTPUT_FILENAME_JSON));
        } catch (NullPointerException e) {
            Log.i(TAG, "CreateEMAIL: Uri exception:" + e);
        }

        email.setType("text/xml");//text/plain");//
        email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(Intent.createChooser(email, "Send email"));//email);//
        Log.i(TAG, "CreateEMAIL: OUT");
    }

    private JSONArray createJson() {
        JSONArray jsonArrayGlobal = new JSONArray();
        for (Model model : models) {
            if (ContentManagerModel.isIgnore(context, model.getModelID()))
                continue;
            JSONObject obj = ModelToJson.convertModelToJson(model);
            if (obj != null) {
                jsonArrayGlobal.put(obj);
            }
        }
        return jsonArrayGlobal;
    }
}
