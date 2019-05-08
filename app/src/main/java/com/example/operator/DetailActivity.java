package com.example.operator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends AppCompatActivity {

    protected static final String LOG_TAG = "my_tag";

    String idINWork;
    RadioGroup radioGroup;
    Button buttonWork;
    RadioGroup radioPostpone;

    EditText textAria;

    ArrayList fixID = new ArrayList();
    ArrayList fixName = new ArrayList();

    String ID;

    String otherStr;

    SharedPreferences sharedPreferencesToken;
    String tokenStr;
    final String SAVED_TEXT = "saved_text";

    ProgressBar progressBar;

    String strCantFixed;

    Button btnCantFixed;
    Button sendBtn;

    Button btnSetDawn;

    String strComment;

    TextView textComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        setTitle("Способ починки");

        //Наш id получим же его во славу Вестероса
        idINWork = getIntent().getExtras().getString("id");
        Log.d(LOG_TAG,idINWork + "Это наш id");

        ID = "falseID";

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // токен
        sharedPreferencesToken = getSharedPreferences("MyPref", MODE_PRIVATE);
        String savedText = sharedPreferencesToken.getString(SAVED_TEXT, "");
        tokenStr = new String("Token "+ savedText);

        textAria = (EditText) findViewById(R.id.textArea);
        TextEditBack();

        Log.d(LOG_TAG,fixID.toString() + "udsufusduf");
        fixID.clear();
        fixName.clear();
        ID = "falseID";

        btnCantFixed = (Button)findViewById(R.id.btnCantFixed);
        sendBtn = (Button)findViewById(R.id.sendBtn);
        btnSetDawn = (Button) findViewById(R.id.btntopostpon);
        textComment = (TextView)findViewById(R.id.textComment);
        textComment.setAlpha(0);

        btnCantFixed.setAlpha(0);
        btnCantFixed.setEnabled(false);

        sendBtn.setAlpha(0);
        sendBtn.setEnabled(false);


        textAria.setInputType(InputType.TYPE_CLASS_TEXT);

        radioPostpone = (RadioGroup) findViewById(R.id.postponeRG);
        radioPostpone.setAlpha(0);

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

        buttonWork = (Button)findViewById(R.id.buttonWork);

        buttonWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(LOG_TAG, ID);

                if (ID == "falseID") {

                    Toast.makeText(DetailActivity.this, "Способ устранения поломки не выбран" , Toast.LENGTH_SHORT).show();

                } else if (ID == "0") {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(buttonWork.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    setTitle("Опишите способ починки");
                    otherStr = textAria.getText().toString();

                    Log.d(LOG_TAG, "other text = " + otherStr);

                    if (otherStr.equalsIgnoreCase("")){

                        Toast.makeText(DetailActivity.this, "Пустое текстовое поле" , Toast.LENGTH_SHORT).show();

                    } else {

                        progressBar = (ProgressBar) findViewById(R.id.detailProgressBar);
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        new FixBroken().execute();

                    }


                } else {

                    progressBar = (ProgressBar) findViewById(R.id.detailProgressBar);
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    new FixBroken().execute();

                }

            }
        });



        btnCantFixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                radioGroup.clearCheck();
                clickBtn();
                setTitle("Опишите причину");

            }
        });


        btnSetDawn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressBar.setVisibility(ProgressBar.INVISIBLE);
                new Postpone().execute();

            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(buttonWork.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                Log.d(LOG_TAG, "other text = " + otherStr);

                otherStr = textAria.getText().toString();

                if (otherStr.equalsIgnoreCase("") || otherStr == null){
                    Toast.makeText(DetailActivity.this, "Пустое текстовое поле" , Toast.LENGTH_SHORT).show();

                } else {

                    Log.d(LOG_TAG, "fixBroken");

                    progressBar = (ProgressBar) findViewById(R.id.detailProgressBar);
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    new  BrokenCantFix().execute();
                }

            }
        });




        new BrokenStat().execute();

        new IsBrokenCantFixed().execute();

    }



    public void  clickBtn(){

        TextEditFunc();
        sendBtn.setAlpha(1);
        sendBtn.setEnabled(true);
    }

    public void BtnBrokenCantFixed() {


        if (strCantFixed.equalsIgnoreCase("N")){


            btnCantFixed.setAlpha(1);
            btnCantFixed.setEnabled(true);

            textComment.setAlpha(0);


        } else {

            btnCantFixed.setAlpha(0);
            btnCantFixed.setEnabled(false);
            sendBtn.setAlpha(0);
            sendBtn.setEnabled(false);
            textComment.setAlpha(1);
            textComment.setText(strComment);

        }


    }

    public void inactPB() {
        progressBar = (ProgressBar) findViewById(R.id.detailProgressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }





    private void RadioFunc() {

        radioGroup = (RadioGroup) findViewById(R.id.radioGrupINWork);



        inactPB();

        for (int i = 0; i < fixName.size(); i++){
            RadioButton newRadioButton = new RadioButton(this);
            Object str = fixName.get(i);
            newRadioButton.setText(str.toString());
            radioGroup.addView(newRadioButton);
            Log.d(LOG_TAG,newRadioButton.getText().toString());
        }

        RadioButton newRadioButton = new RadioButton(this);
        newRadioButton.setText("другое");
        radioGroup.addView(newRadioButton);

        radioGroup.clearCheck();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {



                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (rb != null && checkedId > -1) {
                    // если такая кнопка есть и все нормально, то вызываем всплывающее окно

                    int index = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
                    String fg = Integer.toString(index);
                    String fs =  Integer.toString(checkedId);
                    Log.d(LOG_TAG, fg + " " + fs + " " + fixID.size());

                    if (index == fixID.size()) {
                        TextEditFunc();
                        ID = "0";
                        Log.d(LOG_TAG, ID);
                        sendBtn.setAlpha(0);
                        sendBtn.setEnabled(false);
                    } else  {
                        TextEditBack();
                        ID = (String) fixID.get(index);
                        Log.d(LOG_TAG, ID);
                        sendBtn.setAlpha(0);
                        sendBtn.setEnabled(false);

                    }


                }
            }
        });
    }


    public void TextEditBack() {

        textAria = (EditText) findViewById(R.id.textArea);
        textAria.setLines(0);
        textAria.setEnabled(false);
        textAria.setAlpha(0);
    }

    public void TextEditFunc() {

        textAria = (EditText) findViewById(R.id.textArea);
        textAria.setLines(3);
        textAria.setEnabled(true);
        textAria.setAlpha(1);
    }


    public class   IsBrokenCantFixed extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {

            String resultJson = "";


            try {
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler response = new BasicResponseHandler();
                HttpGet http = new HttpGet("https://app.pomoysam.ru/api/v0/isBrokenCantFixed/?broken_stat_id=" + idINWork);
                resultJson = (String) hc.execute(http, response);
                resultJson = new String(hc.execute(http, new BasicResponseHandler()).getBytes("ISO-8859-1"),"UTF-8");

                Log.d(LOG_TAG,"IsBrokenCantFixed = "  +  resultJson);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultJson;

        }

        protected void onPostExecute(String strJson) {

            super.onPostExecute(strJson);

            JSONObject dataJsonObj;


            try {

                dataJsonObj = new JSONObject(strJson);
                String broken_cant_fixed = dataJsonObj.getString("broken_cant_fixed");

                if (broken_cant_fixed.equalsIgnoreCase("N")){
                    strCantFixed = broken_cant_fixed;

                } else {
                    strCantFixed = broken_cant_fixed;
                    String comment = dataJsonObj.getString("comment");
                    strComment =  "Комментарий: " + comment;

                }

                BtnBrokenCantFixed();

            } catch (JSONException e) {

                Toast.makeText(DetailActivity.this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }

        }
    }



    //Broken

    public class BrokenStat extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String resultJson = "";

            try {
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler response = new BasicResponseHandler();
                HttpGet http = new HttpGet("https://app.pomoysam.ru/api/v0/fixMethods/?broken_stat_id=" + idINWork);
                // resultJson = (String) hc.execute(http, response);
                resultJson = new String(hc.execute(http, new BasicResponseHandler()).getBytes("ISO-8859-1"),"UTF-8");

                Log.d(LOG_TAG, resultJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        protected void onPostExecute(String strJson) {

            super.onPostExecute(strJson);

            try {

                JSONArray fixArray = new JSONArray(strJson);
                for (int i = 0; i < fixArray.length(); i++) {
                    JSONObject nameObject = fixArray.getJSONObject(i);
                    fixID.add(nameObject.getString("id"));
                    fixName.add(nameObject.get("name"));
                }

                RadioFunc();

            } catch (JSONException e) {
                inactPB();
                Toast.makeText(DetailActivity.this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }
        }

    }
    public class FixBroken extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String resultJson = "";

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost http = new HttpPost("https://app.pomoysam.ru/api/v0/fixBroken/");
                http.addHeader("Authorization", tokenStr);


                if (ID == "0") {

                    String str = new String (otherStr.getBytes(),"UTF-8");

                    List nameValuePairs = new ArrayList(3);
                    nameValuePairs.add(new BasicNameValuePair("broken_stat_id", idINWork));
                    nameValuePairs.add(new BasicNameValuePair("fix_method_id", ID));
                    nameValuePairs.add(new BasicNameValuePair("fix_comment", str));
                    http.setEntity(new UrlEncodedFormEntity(nameValuePairs , "UTF-8"));

                    Log.d(LOG_TAG, "other");

                } else {

                    List nameValuePairs = new ArrayList(2);
                    nameValuePairs.add(new BasicNameValuePair("broken_stat_id", idINWork));
                    nameValuePairs.add(new BasicNameValuePair("fix_method_id", ID));
                    http.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                }

                resultJson = (String) httpclient.execute(http, new BasicResponseHandler());
                Log.d(LOG_TAG, resultJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        protected void onPostExecute(String strJson) {

            super.onPostExecute(strJson);

            Intent intent = new Intent(DetailActivity.this, TabActivity.class);
            startActivity(intent);

        }


    }


    public class BrokenCantFix extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String resultJson = "";

            Log.d(LOG_TAG, "BrokenCantFix");

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost http = new HttpPost("https://app.pomoysam.ru/api/v0/fixBroken/");
                http.addHeader("Authorization", tokenStr);


                String str = new String (otherStr.getBytes(),"UTF-8");

                Log.d(LOG_TAG,"str" + str);
                List nameValuePairs = new ArrayList(3);
                nameValuePairs.add(new BasicNameValuePair("broken_stat_id", idINWork));
                nameValuePairs.add(new BasicNameValuePair("fix_comment", str));
                nameValuePairs.add(new BasicNameValuePair("broken_cant_fixed", "1"));
                http.setEntity(new UrlEncodedFormEntity(nameValuePairs , "UTF-8"));

                Log.d(LOG_TAG, "other");



                resultJson = (String) httpclient.execute(http, new BasicResponseHandler());
                Log.d(LOG_TAG, resultJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        protected void onPostExecute(String strJson) {

            super.onPostExecute(strJson);

            Intent intent = new Intent(DetailActivity.this, TabActivity.class);
            startActivity(intent);

        }


    }



    public  class Postpone extends AsyncTask<Void, Void, List<PostponeItem>> {


        @Override
        protected List<PostponeItem> doInBackground(Void... voids) {

            return  new PostponeReasonApi().postponeItems();

        }

        @Override
        protected void onPostExecute(List<PostponeItem> postponeItems) {
            super.onPostExecute(postponeItems);

            radioPostpone = (RadioGroup) findViewById(R.id.postponeRG);
            radioPostpone.setAlpha(1);

            for (int i = 0; i < postponeItems.size(); i++){
                RadioButton newRadioButton = new RadioButton(DetailActivity.this);
                Object str = postponeItems.get(i).getName();
                newRadioButton.setText(str.toString());
                radioPostpone.addView(newRadioButton);
                Log.d(LOG_TAG,newRadioButton.getText().toString());
            }

            RadioButton newRadioButton = new RadioButton(DetailActivity.this);
            newRadioButton.setText("другое");
            radioPostpone.addView(newRadioButton);

            radioPostpone.clearCheck();


            radioPostpone.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {



                    RadioButton rb = (RadioButton) group.findViewById(checkedId);
                    if (rb != null && checkedId > -1) {
                        // если такая кнопка есть и все нормально, то вызываем всплывающее окно

                        int index = radioGroup.indexOfChild(findViewById(radioPostpone.getCheckedRadioButtonId()));
                        String fg = Integer.toString(index);
                        String fs =  Integer.toString(checkedId);
                        Log.d(LOG_TAG, fg + " " + fs + " " + postponeItems.size());

                        if (index == postponeItems.size()) {
                            TextEditFunc();
                            ID = "0";
                            Log.d(LOG_TAG, ID);
                        } else  {
                            TextEditBack();
                            ID = (String) postponeItems.get(index).idPostpone;
                            Log.d(LOG_TAG, ID);


                        }


                    }
                }
            });
        }
    }


    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
