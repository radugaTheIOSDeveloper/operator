package com.example.operator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;

public class StartActivity extends AppCompatActivity {

    ImageView imageLogo;
    SharedPreferences sharedPreferencesToken;
    final String SAVED_TEXT = "saved_text";
    public static String LOG_TAG = "my_log";
    String token;
    String pushToken;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        sharedPreferencesToken = getSharedPreferences("MyPref", MODE_PRIVATE);
        token  = sharedPreferencesToken.getString(SAVED_TEXT, "");

        imageLogo = findViewById(R.id.startLogo);

        progressBar = findViewById(R.id.startPB);

        progressBar.setVisibility(ProgressBar.INVISIBLE);


        final Animation animationAlpha = AnimationUtils.loadAnimation(this, R.anim.logo);

        setTitle("Мойка САМ");

        imageLogo.startAnimation(animationAlpha);

        animationAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {


                if (token.length() <= 2) {
                    Intent intent = new Intent(StartActivity.this, EnterActivity.class);
                    startActivity(intent);

                } else  {

                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    GetPush();

//                    Intent intent = new Intent(StartActivity.this, TabActivity.class);
//                    startActivity(intent);
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });
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

                            new SaveTehToken().execute();


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

            return new SaveTehTokenAPI().tokenItems(token, pushToken);

        }


        @Override
        protected void onPostExecute(List<TokenItem> tokenItems) {
            super.onPostExecute(tokenItems);


            Log.d(LOG_TAG, " status = " + tokenItems);

            progressBar.setVisibility(ProgressBar.INVISIBLE);

            Intent intent = new Intent(StartActivity.this, TabActivity.class);
            startActivity(intent);


        }


    }

}
