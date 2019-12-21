package com.pl.deepbisdk.network;

import com.pl.deepbisdk.DeepBiManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    private static final String BASE_URL = "https://api.deep.bi/v1/streams/DATASET_KEY/events/";
    private static String URL;

    public static Retrofit getClient() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + DeepBiManager.INGESTION_KEY)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        })
        .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static void setBaseUrl(String datasetKey) {
        URL = BASE_URL.replace("DATASET_KEY", datasetKey);
    }
}
