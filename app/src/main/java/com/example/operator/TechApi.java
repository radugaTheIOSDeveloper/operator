package com.example.operator;

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



public class TechApi {

    public static final String LOG_TAG = "WASH_SAM_API ";
    String jsonStrings;


    public String getJSONString (String UrlSpec) throws IOException{

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(UrlSpec)
                .build();


        Response response = client.newCall(request).execute();
        String result = response.body().string();
        return result;

    }


    public List<TechItem> techItems(){

        List<TechItem> techItems = new ArrayList<>();


        try {
            String url = Uri.parse("https://app.pomoysam.ru/api/v0/getTehs/")
                    .buildUpon()
                    .appendQueryParameter("format", "json")
                    .build().toString();


            String jsonString = getJSONString(url);


            parseItems(techItems, jsonString);


        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);
        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);
        }
        return techItems;


    }

    private void parseItems (List<TechItem> techItems , String jsonstring) throws IOException, JSONException {


        Log.d(LOG_TAG, jsonstring);

        JSONArray array = new JSONArray(jsonstring);


        for (int i = 0; i < array.length(); i++) {
            JSONObject nameObject = array.getJSONObject(i);
            TechItem item = new TechItem();
            item.setName(nameObject.getString("name"));
            JSONObject phones = array.getJSONObject(i);
            JSONObject phone = phones.getJSONObject("system_user");
            item.setPhone(phone.getString("phone"));

            techItems.add(item);

        }







    }


}
