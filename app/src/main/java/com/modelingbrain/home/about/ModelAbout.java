package com.modelingbrain.home.about;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class ModelAbout {

    @SuppressWarnings("unused")
    private static final String TAG = "ModelAbout";

//    private ModelAbout(){
////        title = new String();
////        link  = new String();
//    }

    public String title;
    public String link;

    static public ArrayList<ModelAbout> convert (String[] strings){
        Log.d(TAG, "convert - start");
        ArrayList<ModelAbout> modelAbouts = new ArrayList<>();
        for(int i=0;i<strings.length/2;i++){
            ModelAbout element = new ModelAbout();
            element.title = strings[i*2];
            element.link  = strings[i*2+1];
            modelAbouts.add(element);
        }
        for (int i = 0; i < modelAbouts.size(); i++) {
            Log.d(TAG, "i = " + i + " model = "+modelAbouts.get(i).title);
        }
        modelAbouts = sort(modelAbouts);
        for (int i = 0; i < modelAbouts.size(); i++) {
            Log.d(TAG, "i = " + i + " model = "+modelAbouts.get(i).title);
        }
        Log.d(TAG, "convert - finish");
        return  modelAbouts;
    }

    private static ArrayList<ModelAbout> sort(ArrayList<ModelAbout> result) {
        Log.d(TAG, "sort - start");
        if(result.size()<2)
            return result;
        Collections.sort(result, new Comparator<ModelAbout>() {
            @Override
            public int compare(ModelAbout lhs, ModelAbout rhs) {
                return lhs.title.compareTo(rhs.title);
            }
        });
        Log.d(TAG, "sort - finish");
        return result;
    }
}
