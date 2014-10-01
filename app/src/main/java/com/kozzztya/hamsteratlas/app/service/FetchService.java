package com.kozzztya.hamsteratlas.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.kozzztya.hamsteratlas.app.Constants;
import com.kozzztya.hamsteratlas.app.model.Hamster;
import com.kozzztya.hamsteratlas.app.model.HamsterContent;
import com.kozzztya.hamsteratlas.app.utils.HttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class FetchService extends IntentService {

    private static final String TAG = "my" + FetchService.class.getSimpleName();

    private static final String URL = "https://dl.dropboxusercontent.com/s/9ksdptjr81fjb97/hamsters.json";

    public FetchService() {
        super(FetchService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        parseHamsters();
        sendBroadcast(new Intent(Constants.ACTION_FETCH));
    }

    private void parseHamsters() {
        HttpHelper httpHelper = new HttpHelper();

        // Making a request to URL and getting response
        String jsonStr = httpHelper.makeRequest(URL, HttpHelper.GET);

        // Create empty list for new data
        ArrayList<Hamster> hamsters = new ArrayList<>();

        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);

                int size = jsonArray.length();
                for (int i = 0; i < size; i++) {
                    JSONObject o = jsonArray.getJSONObject(i);

                    Hamster hamster = new Hamster(
                            o.getString(HamsterContent.TITLE),
                            o.getString(HamsterContent.DESCRIPTION),
                            o.has(HamsterContent.IMAGE) ?
                                    o.getString(HamsterContent.IMAGE) :
                                    null);

                    hamsters.add(hamster);
                }

                Collections.sort(hamsters);

                HamsterContent.ITEMS.clear();
                HamsterContent.ITEMS.addAll(hamsters);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(getClass().getSimpleName(), "Couldn't get any data from the url");
        }
    }
}
