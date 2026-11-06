package com.student.snhu.nutriscan360;

import okhttp3.*;

import java.io.IOException;

public class ProductApiClient {

    public interface Callback {
        void onSuccess(String json);
        void onFailure(Exception e);
    }

    private static final OkHttpClient client = new OkHttpClient();

    public static void fetchProductByUPC(String upc, Callback callback) {
        String url = "https://world.openfoodfacts.org/api/v0/product/" + upc + ".json";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure(new IOException("Unexpected code " + response));
                    return;
                }
                callback.onSuccess(response.body().string());
            }
        });
    }
}
