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

import java.util.ArrayList;
import java.util.List;

public class InWorkFragment extends Fragment {


    public InWorkFragment() {
    }

    public static InWorkFragment newInstance() {
        return new InWorkFragment();
    }

    ArrayList<Item> items = new ArrayList<Item>();
    NewAdapter newAdapter;
    Context thiscontext;
    ListView lv;
    ArrayList<ItemEmpty> itemEmpties  = new ArrayList<ItemEmpty>();
    EmptyAdapter emptyAdapter;


    SharedPreferences sharedPreferencesToken;
    String tokenStr;
    final String SAVED_TEXT = "saved_text";

    public static final String LOG_TAG = "new  = ";
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_inwork_fault, container, false);
        thiscontext = container.getContext();



        sharedPreferencesToken = thiscontext.getSharedPreferences("MyPref", thiscontext.MODE_PRIVATE);
        String savedText = sharedPreferencesToken.getString(SAVED_TEXT, "");
        tokenStr = new String("Token "+ savedText);

        progressBar = view.findViewById(R.id.inworkPB);
        lv = view.findViewById(R.id.inworkLV);


        new ItemTask().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                Intent intent = new Intent(thiscontext, NewDetail.class);
                intent.putExtra("id",items.get(position).id);
                intent.putExtra("status", "in_work");

                startActivity(intent);

            }
        });


        return view;
    }



    public  class ItemTask extends AsyncTask<Void, Void, List<NewFaultItem>> {


        @Override
        protected List<NewFaultItem> doInBackground(Void... voids) {

            return  new NewFaultApi().newFaultItems("in_work",tokenStr);

        }

        @Override
        protected void onPostExecute(List<NewFaultItem> newFaultItems) {
            super.onPostExecute(newFaultItems);

            Log.d(LOG_TAG," oleg = " + newFaultItems);


            if (newFaultItems.get(0).getSizeTwo() == 0){
                emptyAdapter = new EmptyAdapter(thiscontext, itemEmpties);
                itemEmpties.add(new ItemEmpty("Поломки в работе отсутствуют"));
                lv.setEnabled(false);
                lv.setAdapter(emptyAdapter);



            } else {
                newAdapter = new NewAdapter(thiscontext, items);

                lv.setEnabled(true);


                for (int i = 0; i <= newFaultItems.size() -1; i++) {
                    items.add(new Item(newFaultItems.get(i).getStrNewFault(), newFaultItems.get(i).getId()));
                    Log.d(LOG_TAG , "id ="  +  newFaultItems.get(i).getId());
                }



                lv.setAdapter(newAdapter);
            }




            progressBar.setVisibility(ProgressBar.INVISIBLE);



        }
    }


}
