package com.example.operator;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewFaultApi {

    public static final String LOG_TAG = "WASH_SAM_API ";
    private OkHttpClient client = new OkHttpClient();

    String mtoken;
    String idFragment;


    public String getJSONString (String UrlSpec) throws IOException {




        FormBody body = new FormBody.Builder()
                .build();


        Request request = new Request.Builder()
                .url(UrlSpec)
                .post(body)
                .addHeader("Authorization", mtoken)
                .build();

        Response response = client.newCall(request).execute();

        String result = response.body().string();
        return result;

    }

    public List<NewFaultItem> newFaultItems(String idFragments, String token){


        mtoken = token;

        List<NewFaultItem> newFaultItems = new ArrayList<>();

        idFragment = idFragments;

        try {
            String url = Uri.parse("https://app.pomoysam.ru/api/v0/getBrokensByTeh/")
                    .buildUpon()
                    //        .appendQueryParameter("format", "json")
                    .build().toString();

            String jsonString = getJSONString(url);
            JSONObject jsonBody = new JSONObject(jsonString);

            parseItems(newFaultItems, jsonBody);

        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);
        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);
        }

        return newFaultItems;


    }

    private void parseItems (List<NewFaultItem> items , JSONObject jsonBody) throws IOException, JSONException {

        Log.d(LOG_TAG , "123" + jsonBody.length());


            JSONArray jsonArr = null;
         NewFaultItem newFaultItem = new NewFaultItem();

//            if (jsonBody.getJSONArray("not_accepted").length() != 0 && jsonBody.getJSONArray("in_work").length() != 0){
//                newFaultItem.setSizeOne(jsonBody.getJSONArray("not_accepted").length());
//                newFaultItem.setSizeTwo(jsonBody.getJSONArray("in_work").length());
//                items.add(newFaultItem);
//
//            } else  if (jsonBody.getJSONArray("not_accepted").length() != 0 && jsonBody.getJSONArray("in_work").length() == 0){
//                newFaultItem.setSizeOne(jsonBody.getJSONArray("not_accepted").length());
//                newFaultItem.setSizeTwo(0);
//                items.add(newFaultItem);
//
//
//            }else  if (jsonBody.getJSONArray("not_accepted").length() == 0 && jsonBody.getJSONArray("in_work").length() != 0){
//                newFaultItem.setSizeOne(0);
//                newFaultItem.setSizeTwo(jsonBody.getJSONArray("in_work").length());
//                items.add(newFaultItem);
//
//            } else {
//                newFaultItem.setSizeOne(0);
//                newFaultItem.setSizeTwo(0);
//                items.add(newFaultItem);
//
//            }


        newFaultItem.setSizeOne(jsonBody.getJSONArray("not_accepted").length());
        newFaultItem.setSizeTwo(jsonBody.getJSONArray("in_work").length());
        newFaultItem.setSizeTree(jsonBody.getJSONArray("postponed").length());



        items.add(newFaultItem);


        if (idFragment == "new_fault"){


            jsonArr = jsonBody.getJSONArray("not_accepted");


        } else if (idFragment == "in_work"){
            jsonArr = jsonBody.getJSONArray("in_work");

        }else if (idFragment == "another"){
            jsonArr = jsonBody.getJSONArray("postponed");

        }

//        JSONObject inactive = jsonBody.getJSONObject("inactive");
//        JSONArray jsonArr = inactive.getJSONArray("inactive");




        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject nameObject = jsonArr.getJSONObject(i);
            newFaultItem = new NewFaultItem();

            newFaultItem.setId(nameObject.getString("id"));
            if (nameObject.getString("post_num") == "null") {

                if (nameObject.getString("description").equals("")) {
                    newFaultItem.setStrNewFault(nameObject.getString("name") +
                            "\nАдрес: " + nameObject.getString("addr")+
                            "\nВремя возникновения поломки: " + nameObject.getString("date"));
                } else {
                    newFaultItem.setStrNewFault(nameObject.getString("name")
                            + "\nАдрес: " + nameObject.getString("addr")
                            + "\nВремя возникновения поломки: " + nameObject.getString("date")
                            + "\nКомментарий: " + nameObject.getString("description")
                            );
                }


            } else {

                if (nameObject.getString("post_num") == null) {

                    newFaultItem.setStrNewFault(nameObject.getString("name")
                            + "\nАдрес: " + nameObject.getString("addr")+""
                            + "\nНомер поста: " + nameObject.getString("post_num") + ""+
                            "\nВремя возникновения поломки: " + nameObject.getString("date")
                            );

                } else {

                    newFaultItem.setStrNewFault(nameObject.getString("name")
                            + "\nАдрес: " + nameObject.getString("addr") +""
                            + "\nНомер поста: " + nameObject.getString("post_num") +
                            "\nВремя возникновения поломки: " + nameObject.getString("date")+
                             "\nКомментарий: " + nameObject.getString("description"));

                }


            }


            items.add(newFaultItem);

            }

        }







}
