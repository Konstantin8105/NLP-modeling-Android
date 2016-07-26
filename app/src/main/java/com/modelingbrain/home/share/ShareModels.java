package com.modelingbrain.home.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.modelingbrain.home.opensave.ValuesIO;
import com.modelingbrain.home.R;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.model.Model;

import java.io.File;
import java.util.ArrayList;

public class ShareModels {
    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    private ArrayList<Model> models;
    private Context context;

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
            String text;
            if(count == 1)
                text = String.format(context.getResources().getString(R.string.share_model),count);
            else
                text = String.format(context.getResources().getString(R.string.share_models),count);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    // TODO: 1/28/16  share createHTML
    // TODO: 1/28/16  share createPoster

    public void createEmail() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        email.putExtra(Intent.EXTRA_SUBJECT, R.string.result_of_modeling);//subject
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

        Log.d(TAG, "CreateEMAIL: Message -> " + message);
        Log.d(TAG, "CreateEMAIL: Add file -> " + ValuesIO.FILENAME);


        File sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.d(TAG, "CreateEMAIL: Add file -> " + sdPath);
        sdPath = new File(sdPath.getAbsolutePath());
        Log.d(TAG, "CreateEMAIL: Add file -> " + sdPath);
        try {
            email.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + sdPath.getAbsolutePath() + "/" + ValuesIO.FILENAME));
        } catch (NullPointerException e) {
            Log.d(TAG, "CreateEMAIL: Uri exception:" + e);
        }

        email.setType("text/xml");//text/plain");//
        email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(Intent.createChooser(email, "Send email"));//email);//
        Log.d(TAG, "CreateEMAIL: OUT");
    }
}
