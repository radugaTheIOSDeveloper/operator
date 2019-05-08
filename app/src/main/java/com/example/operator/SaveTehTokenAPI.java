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

public class SaveTehTokenAPI {

    public static final String LOG_TAG = "WASH_SAM_API ";
    private OkHttpClient client = new OkHttpClient();

    String mtoken;
    String pushToken;

    String iD;

    public String getJSONString (String UrlSpec) throws IOException {


        FormBody body = new FormBody.Builder()
                .add("gcm_token",pushToken)
                .build();


        Request request = new Request.Builder()
                .url(UrlSpec)
                .post(body)
                .addHeader("Authorization", "Token " + mtoken)
                .build();

        Response response = client.newCall(request).execute();

        String result = response.body().string();
        return result;

    }

    public List<TokenItem> tokenItems (String token, String _pushToken){


        mtoken = token;
        pushToken = _pushToken;

        List<TokenItem> tokenItems = new ArrayList<>();


        try {
            String url = Uri.parse("https://app.pomoysam.ru/api/v0/saveTehToken/")
                    .buildUpon()
                    //        .appendQueryParameter("format", "json")
                    .build().toString();

            String jsonString = getJSONString(url);
            JSONObject jsonBody = new JSONObject(jsonString);

            parseItems(tokenItems, jsonBody);

        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);
        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);
        }

        return tokenItems;


    }

    private void parseItems (List<TokenItem>  tokenItems , JSONObject jsonBody) throws IOException, JSONException {

        Log.d(LOG_TAG, "broken = " +  jsonBody);
    }

}
