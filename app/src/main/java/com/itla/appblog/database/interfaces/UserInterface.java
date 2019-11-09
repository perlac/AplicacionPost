package com.itla.appblog.database.interfaces;

import com.itla.appblog.database.modelos.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserInterface {
    @POST("users")
    Call<List<User>> buscarUsuarios(@Header("Authorization") String auth);

    @GET("users/{id}")
    Call<User> buscarUsuarioId(@Path("id") int id, @Header("Authorization") String auth);

    @GET("/users/me")
    Call<User> buscarMiInformacion(@Header("Authorization") String auth);

}