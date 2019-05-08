package com.example.operator;

import android.app.LocalActivityManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class TabActivity extends AppCompatActivity {

    public static final String LOG_TAG = "myby = ";
    SharedPreferences sharedPreferencesToken;
    final String SAVED_TEXT = "saved_text";

    TabHost tabHost;
    TabHost.TabSpec tabSpec1;
    TabHost.TabSpec tabSpec2;
    TabHost.TabSpec tabSpec3;
    String tokenStr;
    public String act;
    private long backTapButton;
    private Toast backTosat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        setTitle("Список поломок");

        //Наш токен
        sharedPreferencesToken = getSharedPreferences("MyPref", MODE_PRIVATE);
        String savedText = sharedPreferencesToken.getString(SAVED_TEXT, "");
        tokenStr = new String("Token "+ savedText);

        Log.d(LOG_TAG, tokenStr + "   tabs");

        tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();


        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        tabSpec1 = tabHost.newTabSpec("tag1");
        tabSpec1.setIndicator("новые неисправности");
        tabSpec1.setContent(R.id.tab1);
        tabHost.addTab(tabSpec1);


        tabSpec2 = tabHost.newTabSpec("tag2");
        tabSpec2.setIndicator("в работе");
        tabSpec2.setContent(R.id.tab2);
        tabHost.addTab(tabSpec2);


        tabSpec3 = tabHost.newTabSpec("tag3");
        tabSpec3.setContent(R.id.tab3);
        tabSpec3.setIndicator("Хуйня");
        tabHost.addTab(tabSpec3);


        tabHost.setCurrentTab(0);



        if (tabHost.getCurrentTab()== 0){
            loadFragment(NewFragment.newInstance());

        }
        //обработчик переключения табов
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {

                if (tabId == "tag1") {

                    Log.d(LOG_TAG, "tag1");
                    loadFragment(NewFragment.newInstance());


                } else if (tabId == "tag2"){
                    loadFragment2(InWorkFragment.newInstance());

                    Log.d(LOG_TAG, "tag2");

                }else if (tabId == "tag3") {
                    loadFragment3(AnotherFragment.newInstance());

                    Log.d(LOG_TAG, "tag3");

                }

            }
        });

        Tabs();

    }


    public void Tabs(){

        TabWidget tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        View tabView = tw.getChildTabViewAt(0);
        TextView tv = (TextView)tabView.findViewById(android.R.id.title);
        tv.setText("новые неисправности: ");
        tv.setTextSize(8);


        View tabView2 = tw.getChildTabViewAt(1);
        TextView tv2 = (TextView)tabView2.findViewById(android.R.id.title);
        tv2.setText("в работе: ");
        tv2.setTextSize(8);


        View tabView3 = tw.getChildTabViewAt(2);
        TextView tv3 = (TextView)tabView3.findViewById(android.R.id.title);
        tv3.setText("отложенные: ");
        tv3.setTextSize(8);


    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tab1, fragment);
        ft.commit();
    }



    private void loadFragment2(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tab2, fragment);
        ft.commit();
    }

    private void loadFragment3(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tab3, fragment);
        ft.commit();
    }


    @Override
    public void onBackPressed() {


        if(backTapButton + 2000 > System.currentTimeMillis()){

            backTosat.cancel();
            // super.onBackPressed();

            finish();
            System.exit(0);
            return;

        }else {

            backTosat = Toast.makeText(getBaseContext(),"Для закрытия приложения нажмити кнопку назад еще раз", Toast.LENGTH_SHORT);
            backTosat.show();
        }

        backTapButton = System.currentTimeMillis();

    }

}
