package com.itla.appblog.api.interfaces;



import com.itla.appblog.api.modelos.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SecurityInterface {

    @POST("register")
    Call<User> registrarUsuario(@Body User login);

    @POST("login")
    Call<User> iniciarSesion(@Body User login);

    @DELETE("logout")
    Call<ResponseBody> cerrarSesion(@Header("Authorization") String auth);
}
