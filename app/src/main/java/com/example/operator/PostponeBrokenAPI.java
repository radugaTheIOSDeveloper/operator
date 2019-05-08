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

public class PostponeBrokenAPI {

    public static final String LOG_TAG = "WASH_SAM_API ";
    private OkHttpClient client = new OkHttpClient();

    String mtoken;
    String commenFix;
    String idBrokenStat;
    String idFixMethod;

    public String getJSONString (String UrlSpec) throws IOException {

        FormBody body;

        if (idFixMethod.equals("0")){

            body = new FormBody.Builder()
                    .add("broken_stat_id",idBrokenStat)
                    .add("postpone_reason_id",idFixMethod)
                    .add("postpone_comment",commenFix)
                    .build();

        }else {

            body = new FormBody.Builder()
                    .add("broken_stat_id",idBrokenStat)
                    .add("postpone_reason_id",idFixMethod)
                    .build();

        }




        Request request = new Request.Builder()
                .url(UrlSpec)
                .post(body)
                .addHeader("Authorization", mtoken)
                .build();

        Response response = client.newCall(request).execute();

        String result = response.body().string();
        return result;

    }

    public List<PostponeBrokenITEM> postponeBrokenITEMS (String token, String _idBrokenStat, String _idFixMethod, String comment){

        idBrokenStat = _idBrokenStat;
        idFixMethod = _idFixMethod;
        mtoken = token;
        commenFix = comment;

        List<PostponeBrokenITEM> postponeBrokenITEMS = new ArrayList<>();


        try {
            String url = Uri.parse("https://app.pomoysam.ru/api/v0/PostponeBroken/")
                    .buildUpon()
                    //        .appendQueryParameter("format", "json")
                    .build().toString();

            String jsonString = getJSONString(url);
            JSONObject jsonBody = new JSONObject(jsonString);

            parseItems(postponeBrokenITEMS, jsonBody);

        } catch (IOException ioe){
            Log.e(LOG_TAG, "Ошибка загрузки данных", ioe);
        }catch (JSONException joe){
            Log.e(LOG_TAG, "Ошибка парсинга JSON", joe);
        }

        return postponeBrokenITEMS;


    }

    private void parseItems (List<PostponeBrokenITEM> postponeBrokenITEMS , JSONObject jsonBody) throws IOException, JSONException {

        Log.d(LOG_TAG, "broken = " +  jsonBody);
    }
}
