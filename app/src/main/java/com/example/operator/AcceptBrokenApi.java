package com.example.operator;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AcceptBrokenApi {

    public static final String LOG_TAG = "WASH_SAM_API ";
    private OkHttpClient client = new OkHttpClient();

    String mtoken;

    String iD;

    public String getJSONString (String UrlSpec) throws IOException {


        FormBody body = new FormBody.Builder()
                .add("id",iD)
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

    public List<BrokenItem> brokenItems (String token, String id){


        mtoken = token;
        iD = id;

        List<BrokenItem> brokenItems = new ArrayList<>();


        try {
            String url = Uri.parse("https://app.pomoysam.ru/api/v0/acceptBroken/")
                    .buildUpon()
                    //        .appendQueryParameter("format", "json")
                    .build().toString();

            String jsonString = getJSONString(url);
            JSONObject jsonBody = new JSONObject(jsonString);

            parseItems(brokenItems, jsonBody);

        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);
        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);
        }

        return brokenItems;


    }

    private void parseItems (List<BrokenItem> brokenItems , JSONObject jsonBody) throws IOException, JSONException {

        Log.d(LOG_TAG, "broken = " +  jsonBody);
    }

}
