package kr.co.kjworld.viewsearch.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kr.co.kjworld.viewsearch.data.response.data.KakaoData;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCreator {
    private static final String BASE_URL = "https://dapi.kakao.com";

    private static Retrofit retrofit;

    public static Retrofit getInstance()
    {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(KakaoData.class, new KakaoDataDeserializer())
                    .create();


            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))

                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                    .build();

        }
        return retrofit;
    }
}
