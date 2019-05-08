package com.example.operator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class NewDetail extends AppCompatActivity {

    Button  btnFix;
    Button btnNoFix;
    Button btnPostpone;
    Button btnEnter;
    RadioGroup radioGroup;
    ArrayList fixID = new ArrayList();

    String otherStr;
    //global
    String ID;
    String idINWork;

    RadioButton newRadioButton;

    ProgressBar progressBar;
    EditText textAria;
    SharedPreferences sharedPreferencesToken;
    String tokenStr;
    final String SAVED_TEXT = "saved_text";
    String btnI;
    String status;

    protected static final String LOG_TAG = "detail activity =   ";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);



        sharedPreferencesToken = getSharedPreferences("MyPref", MODE_PRIVATE);
        String savedText = sharedPreferencesToken.getString(SAVED_TEXT, "");
        tokenStr = new String("Token "+ savedText);

        setTitle("Способ починки");

        //Наш id получим же его во славу Вестероса
        idINWork = getIntent().getExtras().getString("id");
        Log.d(LOG_TAG,idINWork + "Это наш id");

        status = getIntent().getExtras().getString("status");



        //initialization view
        btnFix = findViewById(R.id.btnFix);
        btnNoFix = findViewById(R.id.btnNoFix);
        btnPostpone = findViewById(R.id.btnPost);
        btnEnter = findViewById(R.id.ent);
        progressBar = findViewById(R.id.pbDetail);
        textAria = findViewById(R.id.textAreaDetail);
        radioGroup = findViewById(R.id.radioD);

        if (status.equals("another")){
            btnPostpone.setAlpha(0);
            btnPostpone.setEnabled(false);
        }else if (status.equals("in_work")){
            btnPostpone.setAlpha(1);
            btnPostpone.setEnabled(true);
        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        textAria.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                if(event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {

                    otherStr = textAria.getText().toString();

                    return true;
                }
                return false;
            }
        });



        btnFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d(LOG_TAG,"btnFix");

                btnI = "1";

                fixID.clear();
                TextEditBack();
                EnterInvisible();

                radioGroup.setAlpha(0);
                radioGroup.setEnabled(false);

                radioGroup.removeAllViews();

                otherStr = "";
                textAria.setText("");
                textAria.setHint("Опишите способ пчинки");


                progressBar.setVisibility(ProgressBar.VISIBLE);
                new BrokenStat().execute();


            }
        });


        btnNoFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnI = "2";


                TextEditFunc();
                EnterVisible();


                textAria.setText("");
                radioGroup.setAlpha(0);
                radioGroup.setEnabled(false);
                radioGroup.removeAllViews();
                  otherStr = "";

                textAria.setHint("Опишите причину");

                Log.d(LOG_TAG,"btnNoFix");




            }
        });

        btnPostpone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnI = "3";

                fixID.clear();
                TextEditBack();
                EnterInvisible();

                radioGroup.setAlpha(0);
                radioGroup.setEnabled(false);

                radioGroup.removeAllViews();
                textAria.setText("");
                otherStr = "";
                textAria.setHint("Опишите причину");

                progressBar.setVisibility(ProgressBar.VISIBLE);
                new PostPone().execute();

                Log.d(LOG_TAG,"btnPostpone");

            }
        });

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG,"btnEnter");


                String text;

                text = textAria.getText().toString();

                Log.d(LOG_TAG, "text aria  =  "  + textAria.getText().toString());

                if (btnI.equals("1")){


                    if (otherStr.equals("") || otherStr == null){
                        Toast.makeText(NewDetail.this, "Пустое текстовое поле" , Toast.LENGTH_SHORT).show();

                    }else{

                        new BrokenFix().execute();
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                    }

                }else if (btnI == "2"){


                    if (text.equals("")){
                        Toast.makeText(NewDetail.this, "Пустое текстовое поле" , Toast.LENGTH_SHORT).show();

                    }else{

                        new  BrocenCanFxi().execute();
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                    }

                }else if (btnI == "3"){


                    if (otherStr.equals("") || otherStr == null){
                        Toast.makeText(NewDetail.this, "Пустое текстовое поле" , Toast.LENGTH_SHORT).show();

                    }else{
                        new PostponeBroken().execute();
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                    }

                }


            }
        });


        // visible
        TextEditBack();
        EnterInvisible();

        radioGroup.setAlpha(0);
        radioGroup.setEnabled(false);

        progressBar.setVisibility(ProgressBar.INVISIBLE);


    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        otherStr = textAria.getText().toString();
        return true;
    }
        //api

    public  class BrokenStat extends AsyncTask<Void, Void, List<BrokenStatItem>> {


        @Override
        protected List<BrokenStatItem> doInBackground(Void... voids) {

            return new BrokenStatAPI().brokenStatItems(idINWork);

        }

        @Override
        protected void onPostExecute(List<BrokenStatItem> brokenStatItems) {
            super.onPostExecute(brokenStatItems);

            Log.d(LOG_TAG, "brokenStatItems = "  +  brokenStatItems.size());


            RadioFunc(brokenStatItems);

            radioGroup.setAlpha(1);
            radioGroup.setEnabled(true);

            progressBar.setVisibility(ProgressBar.INVISIBLE);


        }
    }


    public  class PostPone extends AsyncTask<Void, Void, List<PostponeItem>> {


        @Override
        protected List<PostponeItem> doInBackground(Void... voids) {

            return new PostponeReasonApi().postponeItems();

        }

        @Override
        protected void onPostExecute(List<PostponeItem> postponeItems) {
            super.onPostExecute(postponeItems);

            Log.d(LOG_TAG, "post pone  = "  +  postponeItems.size());

            RadioFunct(postponeItems);

            radioGroup.setAlpha(1);
            radioGroup.setEnabled(true);

            progressBar.setVisibility(ProgressBar.INVISIBLE);


        }
    }



    private void RadioFunct(List<PostponeItem> postponeItems) {


        if (postponeItems.size() == 0) {

            Log.d(LOG_TAG, "RadioFunc  =  ");

        } else {
            for (int i = 0; i < postponeItems.size(); i++) {
                newRadioButton = new RadioButton(this);
                Object str = postponeItems.get(i).getName();
                newRadioButton.setText(str.toString());
                radioGroup.addView(newRadioButton);
                fixID.add(postponeItems.get(i).getIdPostpone());
                Log.d(LOG_TAG, newRadioButton.getText().toString());
            }
        }


        newRadioButton = new RadioButton(this);
        newRadioButton.setText("другое");
        radioGroup.addView(newRadioButton);

        radioGroup.clearCheck();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {



                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (rb != null && checkedId > -1) {
                    // если такая кнопка есть и все нормально, то вызываем всплывающее окно
                    EnterVisible();

                    int index = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
                    String fg = Integer.toString(index);
                    String fs =  Integer.toString(checkedId);
                    Log.d(LOG_TAG, fg + " " + fs + " " + fixID.size());

                    if (index == fixID.size()) {
                        TextEditFunc();
                        ID = "0";
                        Log.d(LOG_TAG, ID);

                    } else  {
                        TextEditBack();
                        otherStr = "false";
                        ID = (String) fixID.get(index);
                        Log.d(LOG_TAG, ID);


                    }


                }
            }
        });


    }



    private void RadioFunc(List<BrokenStatItem> brokenStatItems) {



        if (brokenStatItems.size() == 0){

            Log.d(LOG_TAG, "RadioFunc  =  njkько другое");

        }else {
            for (int i = 0; i < brokenStatItems.size(); i++){
                newRadioButton = new RadioButton(this);
                Object str = brokenStatItems.get(i).getBrokenStatName();
                newRadioButton.setText(str.toString());
                radioGroup.addView(newRadioButton);
                fixID.add(brokenStatItems.get(i).brokenStatID);
                Log.d(LOG_TAG,newRadioButton.getText().toString());
            }
        }



        newRadioButton = new RadioButton(this);
        newRadioButton.setText("другое");
        radioGroup.addView(newRadioButton);

        radioGroup.clearCheck();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {



                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (rb != null && checkedId > -1) {
                    // если такая кнопка есть и все нормально, то вызываем всплывающее окно
                    EnterVisible();

                    int index = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
                    String fg = Integer.toString(index);
                    String fs =  Integer.toString(checkedId);
                    Log.d(LOG_TAG, fg + " " + fs + " " + fixID.size());

                    if (index == fixID.size()) {
                        TextEditFunc();
                        ID = "0";
                        Log.d(LOG_TAG, ID);

                    } else  {
                        TextEditBack();
                        otherStr = "false";
                        ID = (String) fixID.get(index);
                        Log.d(LOG_TAG, ID);


                    }


                }
            }
        });


    }


    public  class BrokenFix extends AsyncTask<Void, Void, List<BrokenFixItem>> {


        @Override
        protected List<BrokenFixItem> doInBackground(Void... voids) {


            return new BrokenFixAPI().brokenFixItems(tokenStr, idINWork, ID, otherStr);

        }

        @Override
        protected void onPostExecute(List<BrokenFixItem> brokenFixItems) {
            super.onPostExecute(brokenFixItems);

            Log.d(LOG_TAG, "brokenStatItems = "  +  brokenFixItems);
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            Intent intent = new Intent(NewDetail.this, TabActivity.class);

            startActivity(intent);



        }
    }



    public  class PostponeBroken extends AsyncTask<Void, Void, List<PostponeBrokenITEM>> {


        @Override
        protected List<PostponeBrokenITEM> doInBackground(Void... voids) {

            return new PostponeBrokenAPI().postponeBrokenITEMS(tokenStr, idINWork, ID, otherStr);


        }

        @Override
        protected void onPostExecute(List<PostponeBrokenITEM> postponeBrokenITEMS) {
            super.onPostExecute(postponeBrokenITEMS);

            Log.d(LOG_TAG, "postpone broken = "  +  postponeBrokenITEMS);
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            Intent intent = new Intent(NewDetail.this, TabActivity.class);

            startActivity(intent);



        }
    }



    public  class BrocenCanFxi extends AsyncTask<Void, Void, List<BrokenCantFixItem>> {


        @Override
        protected List<BrokenCantFixItem> doInBackground(Void... voids) {


            return new BrokenCantFix().brokenCantFixItems(tokenStr, idINWork, otherStr);

        }

        @Override
        protected void onPostExecute(List<BrokenCantFixItem> brokenCantFixItems) {
            super.onPostExecute(brokenCantFixItems);

            Log.d(LOG_TAG, "brokenStatItems = "  +  brokenCantFixItems);
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            Intent intent = new Intent(NewDetail.this, TabActivity.class);
//
            startActivity(intent);



        }
    }


    public void EnterVisible(){

        btnEnter.setAlpha(1);
        btnEnter.setEnabled(true);

    }

    public void EnterInvisible(){
        btnEnter.setAlpha(0);
        btnEnter.setEnabled(false);
    }


    public void TextEditBack() {

        textAria = (EditText) findViewById(R.id.textAreaDetail);
        textAria.setLines(0);
        textAria.setEnabled(false);
        textAria.setAlpha(0);
    }

    public void TextEditFunc() {


        textAria = (EditText) findViewById(R.id.textAreaDetail);
        textAria.setLines(3);
        textAria.setEnabled(true);
        textAria.setAlpha(1);
    }




    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed()
    {

        Intent intent = new Intent(NewDetail.this, TabActivity.class);
        startActivity(intent);
    }

}
