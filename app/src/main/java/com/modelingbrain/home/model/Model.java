package com.modelingbrain.home.model;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;

public class Model {

    private static final String TAG = "myLogs";

    // DataBase id
    private int dbId;
    public int getDbId() {
        return dbId;
    }
    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    // Model id
    private ModelID modelID;
    public ModelID getModelID(){
        return modelID;
    }
    public void setModelID(ModelID iD){
        this.modelID = iD;
        createEmptyAnswer();
        name = new String();
        dbId = -1;
        millisecond_Date = 0;
    }

    // name of model
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // time of last changes
    private long millisecond_Date;
    public long getMillisecond_Date() {
        return millisecond_Date;
    }
    public void setMillisecond_Date(long millisecond_Date) {
        this.millisecond_Date = millisecond_Date;
    }

    // model state
    private ModelState state;
    public ModelState getState(){
        return state;
    }
    public void setState(ModelState state){
        this.state = state;
    }

    //right side - answers
    private String answer[];
    public String[] getAnswer(){
        return answer;
    }
    public String getAnswer(int i){
        return answer[i];
    }
    public void setAnswer(int i, String message){
        if(answer == null)
            answer = new String[getModelID().getSize()];
        answer[i] = new String();
        answer[i] = message;
    }
    public void setAnswer(JSONArray jsonArray){
        if(answer == null)
            answer = new String[getModelID().getSize()];
        try{
            for(int i=0;i<jsonArray.length();i++){
                answer[i] = jsonArray.getString(i);
            }
        }catch (final JSONException ignored) {
        }
    }

    private void createEmptyAnswer(){
        answer = new String[getModelID().getSize()];
        state = ModelState.NORMAL;
        for(int i=0;i< answer.length;i++)
        {
            //answer[i] = new String();
            answer[i] = "";
        }
    }

    public Model(ModelID iD){
        state = ModelState.NORMAL;
        setModelID(iD);
    }

    public ModelType getModelType(){
        return modelID.getModelType();
    }


// TODO: 7/30/16 remove comments
//    public boolean isOldModel() {
//        return modelID.isOldModel();
//    }
//
//    public int getResourceIcon() {
//        return modelID.getResourceIcon();
//    }
//
//    public int getResourceQuestion() {
//        return modelID.getResourceQuestion();
//    }
//
//    public int getSize() {
//        return modelID.getSize();
//    }

    // TODO: 7/30/16 rename to correct name
    public boolean compareTo(Model model) {
        if(this.modelID != model.modelID)
            return false;
        if(this.getName().compareTo(model.getName()) != 0)
            return false;
        // Never add time
        //if(this.getMillisecond_Date() != mj.getMillisecond_Date())
        //	return false;
        // Never add state
        //if(this.getState() != model.getState())
        //    return false;
        if(this.getModelID().getSize() != model.getModelID().getSize())
            return false;
        for(int i=0;i<this.getModelID().getSize();i++)
            if(this.getAnswer(i).length() != model.getAnswer(i).length())
                return false;
        for(int i=0;i<this.getModelID().getSize();i++)
            if(this.getAnswer(i).compareTo(model.getAnswer(i)) != 0)
                return false;
        return true;
    }

    @Override
    public String toString() {
        return "Model{" +
                "dbId=" + dbId +
                ", modelID=" + modelID +
                ", name='" + name + '\'' +
                ", millisecond_Date=" + millisecond_Date +
                ", state=" + state +
                ", answer=" + Arrays.toString(answer) +
                '}';
    }
}