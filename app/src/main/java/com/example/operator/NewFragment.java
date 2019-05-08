package com.example.operator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewFragment extends Fragment {

    public NewFragment() {
    }

    public static NewFragment newInstance() {
        return new NewFragment();
    }

    ArrayList<Item> items = new ArrayList<Item>();
    NewAdapter newAdapter;
    ArrayList<ItemEmpty> itemEmpties  = new ArrayList<ItemEmpty>();
    EmptyAdapter emptyAdapter;
    Context thiscontext;
    ListView lv;
    String mid;

    Boolean status;
    public static final String LOG_TAG = "new  = ";
    ProgressBar progressBar;

    SharedPreferences sharedPreferencesToken;
    String tokenStr;
    final String SAVED_TEXT = "saved_text";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_new_fault, container, false);
        thiscontext = container.getContext();

        status = false;



        sharedPreferencesToken = thiscontext.getSharedPreferences("MyPref", thiscontext.MODE_PRIVATE);
        String savedText = sharedPreferencesToken.getString(SAVED_TEXT, "");
        tokenStr = new String("Token "+ savedText);


        progressBar = view.findViewById(R.id.newPB);
        lv = view.findViewById(R.id.newLV);
        progressBar.setVisibility(ProgressBar.VISIBLE);


        new ItemTask().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                        + id ) ;



                    Log.d(LOG_TAG, items.get(position).id);
                    mid = items.get(position).id;

                    items.clear();
                    progressBar.setVisibility(ProgressBar.VISIBLE);

                    new Broken().execute();




            }
        });
        return view;
    }


    public  class ItemTask extends AsyncTask<Void, Void, List<NewFaultItem>> {


        @Override
        protected List<NewFaultItem> doInBackground(Void... voids) {

            return  new NewFaultApi().newFaultItems("new_fault",tokenStr);

        }

        @Override
        protected void onPostExecute(List<NewFaultItem> newFaultItems) {
            super.onPostExecute(newFaultItems);

                Log.d(LOG_TAG, " oleg = " + newFaultItems.get(0).getSizeOne());

                Tabs(newFaultItems.get(0).getSizeOne(), newFaultItems.get(0).getSizeTwo(), newFaultItems.get(0).getSizeTree());


            if (newFaultItems.get(0).getSizeOne() == 0){
                emptyAdapter = new EmptyAdapter(thiscontext, itemEmpties);
                itemEmpties.add(new ItemEmpty("Новые неисправности отсутствуют"));
                lv.setEnabled(false);
                lv.setAdapter(emptyAdapter);


            } else {
                newAdapter = new NewAdapter(thiscontext, items);

                lv.setEnabled(true);


                for (int i = 0; i <= newFaultItems.size() -1; i++) {
                    items.add(new Item(newFaultItems.get(i).getStrNewFault(), newFaultItems.get(i).getId()));
                   // Log.d(LOG_TAG , "id ="  +  newFaultItems.get(i).getId());
                }



                lv.setAdapter(newAdapter);
            }




            progressBar.setVisibility(ProgressBar.INVISIBLE);


        }
    }



    public void Tabs(Integer one, Integer two, Integer tree){

        TabHost tb = (TabHost)getActivity().findViewById(R.id.tabHost);


        TabWidget tw = (TabWidget)tb.findViewById(android.R.id.tabs);
        View tabView = tw.getChildTabViewAt(0);
        TextView tv = (TextView)tabView.findViewById(android.R.id.title);
        tv.setText("новые неисправности: " + one);
        tv.setTextSize(8);


        View tabView2 = tw.getChildTabViewAt(1);
        TextView tv2 = (TextView)tabView2.findViewById(android.R.id.title);
        tv2.setText("в работе: " + two);
        tv2.setTextSize(8);


        View tabView3 = tw.getChildTabViewAt(2);
        TextView tv3 = (TextView)tabView3.findViewById(android.R.id.title);
        tv3.setText("отлженные: " + tree);
        tv3.setTextSize(8);


    }

    public  class Broken extends AsyncTask<Void, Void, List<BrokenItem>> {


        @Override
        protected List<BrokenItem> doInBackground(Void... voids) {

            return  new AcceptBrokenApi().brokenItems(tokenStr ,mid);

        }

        @Override
        protected void onPostExecute(List<BrokenItem> brokenItems) {
            super.onPostExecute(brokenItems);


                Log.d(LOG_TAG," oleg null= " + brokenItems);


            new ItemTask().execute();


        }
    }


}
