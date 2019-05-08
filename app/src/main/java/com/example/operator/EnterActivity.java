package com.example.operator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

public class EnterActivity extends AppCompatActivity {


    public static String LOG_TAG = "my_log";
    ArrayList nameArray = new ArrayList();
    ArrayList phoneArray = new ArrayList();
    Boolean status = false;
    ProgressBar progressBar;
    String username;
    String passwordString = "";
    EditText passwordText;
    SharedPreferences sharedPreferencesToken;
    String savedToken;
    String pushToken;

    final String SAVED_TEXT = "saved_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);


        setTitle("Авторизация");


        Log.d(LOG_TAG, "123122");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        passwordText  = (EditText) findViewById(R.id.editText);
        passwordText.setEnabled(false);
        new GetTechs().execute();


    }


    public  class GetTechs extends AsyncTask<Void, Void , List<TechItem>> {


        @Override
        protected List<TechItem> doInBackground(Void... voids) {

            return new TechApi().techItems();


        }


        @Override
        protected void onPostExecute(List<TechItem> techItems) {
            super.onPostExecute(techItems);

            Log.d(LOG_TAG, "size = " + techItems.size());

            status = true;

            for (int i = 0; i < techItems.size(); i++) {

                nameArray.add(techItems.get(i).getName());
                phoneArray.add(techItems.get(i).getPhone());

            }
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            passwordText = (EditText) findViewById(R.id.editText);

            if (status == true) {

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EnterActivity.this, android.R.layout.simple_spinner_item, nameArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                spinner.setAdapter(adapter);
                spinner.setPrompt("Выберите пользователя из списка");

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {

                        username = (String) phoneArray.get(position);
                        Log.d(LOG_TAG, username);

                        final EditText editText = (EditText) findViewById(R.id.editText);
                        editText.setEnabled(true);
                        editText.setOnKeyListener(new View.OnKeyListener() {
                                                      public boolean onKey(View v, int keyCode, KeyEvent event) {
                                                          if (event.getAction() == KeyEvent.ACTION_DOWN &&
                                                                  (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                              passwordString = editText.getText().toString();
                                                              progressBar = (ProgressBar) findViewById(R.id.progressBar);
                                                              progressBar.setVisibility(ProgressBar.VISIBLE);

                                                              if (passwordString.length() < 4) {
                                                                  progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                                  Toast.makeText(getBaseContext(), "Пароль должен быть не менне 5 символов", Toast.LENGTH_SHORT).show();

                                                              } else {

                                                                  new Enter().execute();


                                                              }
                                                              return true;
                                                          }
                                                          return false;
                                                      }
                                                  }
                        );

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

            } else {
                Toast.makeText(getBaseContext(), "Что то пошло не так", Toast.LENGTH_SHORT).show();

            }


        }


        public class Enter extends AsyncTask<Void, Void, List<EnterItem>> {


            @Override
            protected List<EnterItem> doInBackground(Void... voids) {

                return new EnterApi().enterItems(username, passwordString);

            }


            @Override
            protected void onPostExecute(List<EnterItem> enterItems) {
                super.onPostExecute(enterItems);




                Log.d(LOG_TAG, " status = " + enterItems.get(0).getStatus());

                String status = enterItems.get(0).getStatus();

                if (status.equals("good")){


                    String token = enterItems.get(0).getToken();
                    sharedPreferencesToken = getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sharedPreferencesToken.edit();
                    ed.putString(SAVED_TEXT, token);
                    ed.commit();
                    savedToken = sharedPreferencesToken.getString(SAVED_TEXT, "");

                    GetPush();

                    Log.d(LOG_TAG, token + "token");



                }else {

                    Toast.makeText(getBaseContext(), "Неверный пароль", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(ProgressBar.INVISIBLE);

                }




            }


        }


        public void GetPush() {


            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(LOG_TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();


                            sharedPreferencesToken = getSharedPreferences("pushToken", MODE_PRIVATE);
                            SharedPreferences.Editor ed = sharedPreferencesToken.edit();
                            ed.putString(SAVED_TEXT, token);
                            ed.commit();
                            pushToken = sharedPreferencesToken.getString(SAVED_TEXT, "");

                            if (pushToken!=""){

                                new  SaveTehToken().execute();


                            }else {
                                Toast.makeText(getBaseContext(), "Что то пошло не так", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(ProgressBar.INVISIBLE);

                            }




                        }
                    });

        }


        public class SaveTehToken extends AsyncTask<Void, Void, List<TokenItem>> {


            @Override
            protected List<TokenItem> doInBackground(Void... voids) {

                return new SaveTehTokenAPI().tokenItems(savedToken, pushToken);

            }


            @Override
            protected void onPostExecute(List<TokenItem> tokenItems) {
                super.onPostExecute(tokenItems);


                Log.d(LOG_TAG, " status = " + tokenItems);

                progressBar.setVisibility(ProgressBar.INVISIBLE);

                Intent intent = new Intent(EnterActivity.this, TabActivity.class);
                startActivity(intent);


            }


        }

        }

}
