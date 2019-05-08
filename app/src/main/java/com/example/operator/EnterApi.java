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

public class EnterApi {

    public static final String LOG_TAG = "WASH_SAM_API ";
    String jsonStrings;
    private OkHttpClient client = new OkHttpClient();

    String phones;
    String passs;

    public String getJSONString (String UrlSpec) throws IOException {




        FormBody body = new FormBody.Builder()
                .add("username", phones)
                .add("password", passs)
                .build();


        Request request = new Request.Builder()
                .url(UrlSpec)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        String result = response.body().string();
        return result;

    }


    public List<EnterItem> enterItems(String phone , String password){

        List<EnterItem> enterItems = new ArrayList<>();
        EnterItem item = new EnterItem();

        phones = phone;
        passs = password;

        try {
            String url = Uri.parse("https://app.pomoysam.ru/api/v0/login/")
                    .buildUpon()
                    //        .appendQueryParameter("format", "json")
                    .build().toString();

            String jsonString = getJSONString(url);
            JSONObject jsonBody = new JSONObject(jsonString);

            parseItems(enterItems, jsonBody);

        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);

        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);

            item.setStatus("no_good");
            enterItems.add(item);

        }

        return enterItems;


    }

    private void parseItems (List<EnterItem> items , JSONObject jsonBody) throws IOException, JSONException {

        items.clear();

        Log.d(LOG_TAG , "parsing arrays" + jsonBody);


        EnterItem item = new EnterItem();
        String srt = jsonBody.getString("token");
        Log.d(LOG_TAG, srt);

        item.setToken(srt);
        item.setStatus("good");
        items.add(item);
//
//        }


    }

}
