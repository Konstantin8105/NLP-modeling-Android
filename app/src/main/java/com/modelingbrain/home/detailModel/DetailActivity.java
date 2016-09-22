package com.modelingbrain.home.detailModel;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.modelingbrain.home.detailModel.fragments.StageEditFragment;
import com.modelingbrain.home.detailModel.fragments.StageViewFragment;
import com.modelingbrain.home.db.DBHelperModel;
import com.modelingbrain.home.model.Model;
import com.modelingbrain.home.R;

public class DetailActivity extends AppCompatActivity {

    @SuppressWarnings("unused")
    private final String TAG = this.getClass().toString();

    static public final String DATABASE_ID = "DATABASE_ID";
    static public final String STATE_DETAIL_ACTIVITY = "STATE_DETAIL_ACTIVITY";

    private enum StateView {
        STATE_VIEW_READ,
        STATE_VIEW_WRITE
        //TODO add exame mode for action - упражнения по-степенное
    }

    private Model model;

    private FloatingActionButton fab;
    private int generalModelColor;
    private StageDetailActivity stageDetailActivity;
    private StateView stateView;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate - start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int idDb = getIntent().getIntExtra(DATABASE_ID, 0);
        DBHelperModel dbHelperModel = new DBHelperModel(this.getBaseContext());
        model = dbHelperModel.openModel(idDb);

        initColors();

        stageDetailActivity = StageDetailActivity.valueOf(getIntent().getStringExtra(STATE_DETAIL_ACTIVITY));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add text to Snackbar
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                changeStageDetail();
                createView();
                //TODO switch icon in fab
            }
        });
        Log.d(TAG, "stageDetailActivity = " + stageDetailActivity.toString());
        if (stageDetailActivity == StageDetailActivity.STATE_READ_ONLY) {
            fab.hide();
        }

        switch (stageDetailActivity) {
            case STATE_NEW_FROM_WRITE:
                stateView = StateView.STATE_VIEW_WRITE;
                break;
            default:
                stateView = StateView.STATE_VIEW_READ;
        }


        stageViewFragment = new StageViewFragment();
        stageEditFragment = new StageEditFragment();

        createView();
        Log.d(TAG, "onCreate - finish");
    }

    private void initColors() {
        Log.d(TAG, "initColors - start");
        // TODO: 7/30/16 function getColor() is old - use new version
        generalModelColor = ContextCompat.getColor(getBaseContext(), model.getModelType().getGeneralColor());
//        generalModelTextColor   = this.getResources().getColor(model.getModelType().getTextColor());

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ctl.setTitle(getResources().getStringArray(model.getModelID().getResourceQuestion())[0]);

        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nested_detail_scroll);
        nestedScrollView.setBackgroundColor(generalModelColor);
        Log.d(TAG, "initColors - finish");
    }

    private void changeStageDetail() {
        if (stageDetailActivity == StageDetailActivity.STATE_READ_ONLY)
            return;
        switch (stateView) {
            case STATE_VIEW_READ:
                stateView = StateView.STATE_VIEW_WRITE;
                break;
            case STATE_VIEW_WRITE:
                stateView = StateView.STATE_VIEW_READ;
                break;
            default:
                throw new RuntimeException("Add new view");
        }
    }

    private StageViewFragment stageViewFragment;
    private StageEditFragment stageEditFragment;

    private void createView() {
        transaction = getFragmentManager().beginTransaction();
        switch (stateView) {
            case STATE_VIEW_READ: {
                transaction.remove(stageEditFragment);
                stageViewFragment.send(model);
                transaction.replace(R.id.detail_fragment, stageViewFragment);
                break;
            }
            case STATE_VIEW_WRITE: {
                transaction.remove(stageViewFragment);
                stageEditFragment.send(model);
                transaction.replace(R.id.detail_fragment, stageEditFragment);
                break;
            }
            default:
                throw new RuntimeException("Add new view");
        }
        //todo why I add that line?
        //transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    protected void onPause() {
        DBHelperModel dbHelperModel = new DBHelperModel(this.getBaseContext());
        dbHelperModel.updateModel(model);
        setResult(RESULT_OK);
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed - start");
        DBHelperModel dbHelperModel = new DBHelperModel(this.getBaseContext());
        dbHelperModel.updateModel(model);
        setResult(RESULT_OK);
        super.onBackPressed();
        Log.d(TAG, "onBackPressed - finish");
    }

}
// TODO add fragment for polar model
// TODO add fragment for rules
// TODO add fragment for eyes
// TODO add AutoComplete text

//TODO cleaning the code


//}
//
////
//    private static final String TAG = "myLogs";
//
//    //static public final String DETAIL_MODEL = "DETAIL_MODEL";
//
//
//fab.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View view) {
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//        .setAction("Action", null).show();
//        fab.setImageResource(R.drawable.ic_archive_24dp);
//        fab.invalidate();
//        }
//        });
//
//        //Views for toolbar name
//        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
//        ctl.setTitle(getResources().getStringArray(model.getModelLine(model.getModelID()).resourceQuestions)[0]);
//        ctl.setBackgroundColor(generalModelColor);
//        ctl.setStatusBarScrimColor(generalModelColor);
//        ctl.setContentScrimColor(generalModelColor);
//
//        // Views for names
//        View mCircle = findViewById(R.id.DETAILS_circle);
//        GradientDrawable bgShape = (GradientDrawable) mCircle.getBackground();
//        bgShape.setColor(generalModelColor);
//
//        TextView mName = (TextView) findViewById(R.id.title);
//        mName.setText(model.getName());
//
//        // Views for date
//        View mCircleDate = findViewById(R.id.DETAILS_circle_date);
//        GradientDrawable bgShapeDate = (GradientDrawable) mCircleDate.getBackground();
//        bgShapeDate.setColor(generalModelColor);
//
//        TextView mNameDate = (TextView) findViewById(R.id.title_date);
//        mNameDate.setText(GlobalFunction.ConvertMillisecondToDate(model.getMillisecond_Date()));
//
//        // TODO: FUTURE: Update DetailActivity
//
//        linLayout = (LinearLayout) findViewById(R.id.detail_listview);
//        linLayout.setBackgroundColor(generalModelColor);
//        for (int i = 0; i < model.getModelSize(); i++) {
//            createElement(getResources().getStringArray(model.getModelLine(model.getModelID()).resourceQuestions)[1 + i], QA.QUESTION);
//            createElement(model.getAnswer(i), QA.ANSWER);
//        }
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
//    }
/*

public class ActivityModel extends Activity implements View.OnClickListener {

    private static final String TAG = "myLogs";

    static final int ID_Edit_Box = 1000;
    RadioButton rb_RightHand;

    DBHelperModel  dbHelper;
    LinearLayout linLayout;
    Model WS;

    boolean ReadOnly;

    ////////////////////////////////////////////////////////
    boolean STATE_VIEW;
    void CreateLIEdit()
    {
        Log.d(TAG, "IN  ActivityModel: CreateLIEdit");
        LayoutInflater ltInflater = getLayoutInflater();
        View item = ltInflater.inflate(R.layout.listitem_edit, linLayout, false);
        ImageButton btn = (ImageButton) item.findViewById(R.id.imageButtonLIEdie);
        btn.setId(R.id.id_button_switcher_read_write);
        btn.setOnClickListener(this);
        if(STATE_VIEW)
        {
            btn.setImageResource(R.drawable.icon_edit);
            btn.setBackgroundResource(R.drawable.round_button);
        }
        else
        {
            btn.setImageResource(R.drawable.icon_ok);
            btn.setBackgroundResource(R.drawable.round_button_selected);
        }
        linLayout.addView(item);
        Log.d(TAG, "OUT ActivityModel: CreateLIEdit");
    }
    ////////////////////////////////////////////////////////

    void CreateEditModel()
    {
        Log.d(TAG, "IN  ActivityModel: CreateEditModel");
        CreateNameEdit();
        for(int i=0;i<WS.getSize();i++){
            CreateLeft(i);
            CreateRightEdit(i);
            if(WS.GetIDofModel() == Model.IDofModel.ID_Eye)
                CreateEyeInterface();
        }
        Log.d(TAG, "OUT ActivityModel: CreateEditModel");
    }

    void CreateViewModel()
    {
        Log.d(TAG, "IN  ActivityModel: CreateViewModel");
        CreateName();
        if(WS.GetIDofModel() == Model.IDofModel.ID_Polar)
        {
            CreatePolar();
        }
        else if(WS.GetIDofModel() == Model.IDofModel.ID_Rules)
        {
            CreateRules();
        }
        else for(int i=0;i<WS.getSize();i++){
            CreateLeft(i);
            CreateRight(i);
        }
        Log.d(TAG, "OUT ActivityModel: CreateViewModel");
    }

    void SaveDATA()
    {
        Log.d(TAG, "IN  ActivityModel: SaveDATA");
        WS.SetName(((EditText)findViewById(R.id.id_edit_box_name)).getText().toString());
        for(int i=0;i<WS.getSize();i++)
            WS.SetRight(i, ((MultiAutoCompleteTextView) findViewById(ID_Edit_Box+i)).getText().toString());
        Log.d(TAG, "OUT ActivityModel: SaveDATA");
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "IN  ActivityModel: onClick");
        if(v.getId() == R.id.id_button_switcher_read_write)
        {
            if(STATE_VIEW)
            {
                STATE_VIEW = false;
            }
            else
            {
                STATE_VIEW = true;
                SaveDATA();
            }
            linLayout.removeAllViews();
            CreateAllView();
        }
        else{
            if(WS.GetIDofModel() == Model.IDofModel.ID_Eye){
                boolean right_hand = rb_RightHand.isChecked();
                MultiAutoCompleteTextView MACTV = (MultiAutoCompleteTextView) findViewById(ID_Edit_Box+0);
                switch(v.getId()) {
                    case R.id.id_activity_model_button_01:
                        if(right_hand)  MACTV.setText(MACTV.getText()+ "Vk -> ");
                        else            MACTV.setText(MACTV.getText()+ "Vr -> ");
                        break;
                    case R.id.id_activity_model_button_02:
                                        MACTV.setText(MACTV.getText()+ "V -> ");
                        break;
                    case R.id.id_activity_model_button_03:
                        if(right_hand)  MACTV.setText(MACTV.getText()+ "Vr -> ");
                        else            MACTV.setText(MACTV.getText()+ "Vk -> ");
                        break;
                    case R.id.id_activity_model_button_04:
                        if(right_hand)  MACTV.setText(MACTV.getText()+ "Ak -> ");
                        else            MACTV.setText(MACTV.getText()+ "Ar -> ");
                        break;
                    case R.id.id_activity_model_button_05:
                                        MACTV.setText(MACTV.getText()+ "C -> ");
                        break;
                    case R.id.id_activity_model_button_06:
                        if(right_hand)  MACTV.setText(MACTV.getText()+ "Ar -> ");
                        else            MACTV.setText(MACTV.getText()+ "Ak -> ");
                        break;
                    case R.id.id_activity_model_button_07:
                        if(right_hand)  MACTV.setText(MACTV.getText()+ "K -> ");
                        else            MACTV.setText(MACTV.getText()+ "Ad -> ");
                        break;
                    case R.id.id_activity_model_button_08:
                                        MACTV.setText(MACTV.getText()+ "Ad -> ");
                        break;
                    case R.id.id_activity_model_button_09:
                        if(right_hand)  MACTV.setText(MACTV.getText()+ "Ad -> ");
                        else            MACTV.setText(MACTV.getText()+ "K -> ");
                        break;
                }
            }
        }
        Log.d(TAG, "OUT ActivityModel: onClick");
    }



    void CreateRightEdit(int position){
        LayoutInflater ltInflater = getLayoutInflater();
        View item_Right = ltInflater.inflate(R.layout.one_row_right_edit, linLayout, false);
        ImageView im_right = (ImageView) item_Right.findViewById(R.id.imageViewRowRight);
        im_right.setImageResource(WS.GetNavigationBack());

        MultiAutoCompleteTextView et_Right = (MultiAutoCompleteTextView) item_Right.findViewById(R.id.editText);
        et_Right.setText(WS.GetRight(position));
        et_Right.setTextColor(WS.GetGeneralColorText());
        et_Right.setId(ID_Edit_Box + position);

        if(WS.GetIDofModel() == Model.IDofModel.ID_PROFILE3)
        {
            if(position == 0) et_Right.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_REP_SYSTEM)));
            if(position == 1) et_Right.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_sub_REP_SYSTEM)));
            if(position == 3) et_Right.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_dishey)));
            if(position == 4) et_Right.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_MetaProgramm)));
            if(position == 5) et_Right.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_MetaProgramm_ubeditel)));
            if(position == 7) et_Right.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_TricksOfLanguage)));
            if(position == 8) et_Right.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.str_Position)));
            et_Right.setTokenizer( new MultiAutoCompleteTextView.CommaTokenizer());
        }

        item_Right.setBackgroundColor(WS.GetGeneralColor());
        linLayout.addView(item_Right);
    }

    private void CreatePolar() {
        LayoutInflater ltInflater = getLayoutInflater();
        View table = ltInflater.inflate(R.layout.one_row_table_view_polar, linLayout, false);
        ((TextView) table.findViewById(R.id.textView1)).setText("");
        ((TextView) table.findViewById(R.id.textView2)).setText("\n" + WS.GetLeft(1) + ":\n" + WS.GetRight(1) + "\n");
        ((TextView) table.findViewById(R.id.textView3)).setText("\n" + WS.GetLeft(0) + ":\n" + WS.GetRight(0) + "\n");
        ((TextView) table.findViewById(R.id.textView4)).setText(WS.GetLeft(2) + ":\n" + WS.GetRight(2) + "\n");
        ((TextView) table.findViewById(R.id.textView5)).setText(WS.GetRight(5) + "\n");
        ((TextView) table.findViewById(R.id.textView6)).setText(WS.GetRight(4) + "\n");
        ((TextView) table.findViewById(R.id.textView7)).setText(WS.GetLeft(3) + ":\n" + WS.GetRight(3) + "\n");
        ((TextView) table.findViewById(R.id.textView8)).setText(WS.GetRight(7) + "\n");
        ((TextView) table.findViewById(R.id.textView9)).setText(WS.GetRight(6) + "\n");
        linLayout.addView(table);
    }


    private void CreateRules() {
        for(int i=0;i<WS.getSize();i++) {
            int number = 1;
            int start = 0;
            int finish;
            for (int j = 0; j < WS.GetRight(i).length(); j++) {
                if (WS.GetRight(i).charAt(j) == '\n' || j == WS.GetRight(i).length() - 1) {
                    if (WS.GetRight(i).charAt(start) == '\n') start++;
                    if (j == WS.GetRight(i).length() - 1) finish = j + 1;
                    else finish = j;
                    if (finish > start) {
                        char str[] = new char[finish - start];
                        for (int g = 0; g < str.length; g++)
                            str[g] = WS.GetRight(i).charAt(g + start);
                        CreateRulesRight(number++, new String(str));
                    }
                    start = j;
                }
            }
        }
    }

    void CreateRulesRight(int number, String str)
    {
        if(str.length() == 0 ||
                (str.length() == 1 && str.charAt(0) == '\n') ||
                (str.length() == 1 && str.charAt(0) == ' ')
                ) return;
        LayoutInflater ltInflater = getLayoutInflater();
        View item_Right = ltInflater.inflate(R.layout.one_row_view_right_rule, linLayout, false);

        TextView txt_v1 = (TextView) item_Right.findViewById(R.id.textView1);
        if(number<10)txt_v1.setText(" 0"+number + " ");
        else txt_v1.setText(" "+number + " ");
        txt_v1.setTextColor(WS.GetGeneralColorText());

        TextView txt_v2 = (TextView) item_Right.findViewById(R.id.text);
        txt_v2.setText(str);
        txt_v2.setTextColor(WS.GetGeneralColorText());

        item_Right.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        item_Right.setBackgroundColor(WS.GetGeneralColor());
        linLayout.addView(item_Right);
    }

    private void CreateEyeInterface() {
        LayoutInflater ltInflater = getLayoutInflater();
        View item = ltInflater.inflate(R.layout.listitem_edit_model_eye, linLayout, false);
        {   ImageButton im = (ImageButton) item.findViewById(R.id.imageButton1);
            im.setId(R.id.id_activity_model_button_01);
            im.setOnClickListener(this);}
        {   ImageButton im = (ImageButton) item.findViewById(R.id.imageButton2);
            im.setId(R.id.id_activity_model_button_02);
            im.setOnClickListener(this);}
        {   ImageButton im = (ImageButton) item.findViewById(R.id.imageButton3);
            im.setId(R.id.id_activity_model_button_03);
            im.setOnClickListener(this);}
        {   ImageButton im = (ImageButton) item.findViewById(R.id.imageButton4);
            im.setId(R.id.id_activity_model_button_04);
            im.setOnClickListener(this);}
        {   ImageButton im = (ImageButton) item.findViewById(R.id.imageButton5);
            im.setId(R.id.id_activity_model_button_05);
            im.setOnClickListener(this);}
        {   ImageButton im = (ImageButton) item.findViewById(R.id.imageButton6);
            im.setId(R.id.id_activity_model_button_06);
            im.setOnClickListener(this);}
        {   ImageButton im = (ImageButton) item.findViewById(R.id.imageButton7);
            im.setId(R.id.id_activity_model_button_07);
            im.setOnClickListener(this);}
        {   ImageButton im = (ImageButton) item.findViewById(R.id.imageButton8);
            im.setId(R.id.id_activity_model_button_08);
            im.setOnClickListener(this);}
        {   ImageButton im = (ImageButton) item.findViewById(R.id.imageButton9);
            im.setId(R.id.id_activity_model_button_09);
            im.setOnClickListener(this);}
        rb_RightHand = (RadioButton) item.findViewById(R.id.radioRight);
        //rg.setId(R.id.id_activity_model_radio_right);
        //rg.setOnClickListener(this);
        //RadioButton rg2 = (RadioButton) item.findViewById(R.id.radioLeft);
        //rg2.setId(R.id.id_activity_model_radio_left_);
        //rg2.setOnClickListener(this);
        linLayout.addView(item);
    }
}
/*
		boolean b_Save = false;

		if(Name.getText().toString().compareTo(model.GetBaseName(model.GetIDofModel(),this.getBaseContext())) != 0 && Name.getText().toString().length() != 0)
				b_Save = true;
		// EMPTY MODEL
		if(Name.getText().toString().length() == 0 || Name.getText().toString().compareTo(model.GetBaseName(model.GetIDofModel(),this.getBaseContext())) == 0)
		{
			for(int i=0;i<model.GetLenght();i++)
				if(et_Right[i].getText().toString().length()>0)
					{
						b_Save = true;
						break;
					}
		}

		boolean b_Change = false;
		if(Name.getText().toString().compareTo(model.GetName()) == 0)
		{
			for(int i=0;i<model.GetLenght();i++)
				if(et_Right[i].getText().toString().compareTo(model.GetRight(i)) != 0)
				{
					b_Change = true;
					break;
				}
		}
		else b_Change = true;

		if(b_Save) Log.d(TAG, "ActivityEditModel:onPause. b_Save = true");
		else       Log.d(TAG, "ActivityEditModel:onPause. b_Save = false");

		if(b_Change) Log.d(TAG, "ActivityEditModel:onPause. b_Change = true");
		else         Log.d(TAG, "ActivityEditModel:onPause. b_Change = false");

		if(b_Save)
		{
			if(b_Change)

		}
		else
		{
			dbHelper.Delete(model, model.getDb_ID());
			Log.d(TAG, "ActivityEditModel:onPause. Delete DB model");
		}

 */