package com.itla.appblog.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioApi {
    private static final String BASE_URL = "http://itla.hectorvent.com/api/";
    private static Retrofit retrofit;

    private ServicioApi() {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }

    public static Retrofit getServicoApi() {
        if (retrofit == null)
             new ServicioApi();

        return retrofit;
    }
}
