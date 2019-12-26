package com.pl.deepbisdk.network;

import android.util.Log;
import com.pl.deepbisdk.DeepBiManager;
import com.pl.deepbisdk.queuemanager.HitEvent;

import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NetworkManager {

    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    private NetworkManager() {

    }

    public void postHit(final ArrayList<HitEvent> jsonBody, final PostHitListener onPostHitListener) {
        Retrofit retrofit = ApiClient.getClient();
        UploadHitApi uploadAPIs = retrofit.create(UploadHitApi.class);
        Call<ResponseBody> call = uploadAPIs.uploadHit(
                "" + Calendar.getInstance().getTimeInMillis(),
                DeepBiManager.DEVICE_ID,
                jsonBody
        );
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("Testlog", "DeepBi SDK TestLog Response code " + response.code());
                onPostHitListener.onPostHitFinish(response.code() == 204 || response.code() == 200, jsonBody);
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Testlog", "DeepBi SDK TestLog Response fail " + t.toString());
                onPostHitListener.onPostHitFinish(false, jsonBody);
            }
        });

    }

    public interface PostHitListener {
        void onPostHitFinish(boolean isSucess, ArrayList<HitEvent> listSent);
    }
}
