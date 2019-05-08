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

public class BrokenStatAPI {


    public static final String LOG_TAG = "WASH_SAM_API ";
    String jsonStrings;
    private OkHttpClient client = new OkHttpClient();

    String brokenid;
    String passs;

    public String getJSONString (String UrlSpec) throws IOException {



        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(UrlSpec)
                .build();


        Response response = client.newCall(request).execute();
        String result = response.body().string();
        return result;

    }


    public List<BrokenStatItem> brokenStatItems(String id ){

        List<BrokenStatItem> brokenStatItems = new ArrayList<>();

        brokenid = id;

        try {
            String url = Uri.parse("https://app.pomoysam.ru/api/v0/fixMethods/?broken_stat_id=" + brokenid)
                    .buildUpon()
                    //        .appendQueryParameter("format", "json")
                    .build().toString();

            String jsonString = getJSONString(url);

            parseItems(brokenStatItems, jsonString);

        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);

        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);

        }

        return brokenStatItems;


    }

    private void parseItems (List<BrokenStatItem> items , String jsonString) throws IOException, JSONException {




        JSONArray array = null;


        if (jsonString == null || jsonString.equals("[]")){
            Log.d(LOG_TAG , "parsing arrays" + jsonString);

        } else {
            array =  new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject nameObject = array.getJSONObject(i);
                BrokenStatItem item = new BrokenStatItem();
                item.setBrokenStatName(nameObject.getString("name"));
                item.setBrokenStatID(nameObject.getString("id"));

                items.add(item);

            }
        }


    }
}
