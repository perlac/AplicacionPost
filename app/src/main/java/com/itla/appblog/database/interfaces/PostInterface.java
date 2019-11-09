package com.itla.appblog.database.interfaces;


import com.itla.appblog.database.modelos.Comment;
import com.itla.appblog.database.modelos.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostInterface {

    @GET("post")
    Call<List<Post>> buscarPosts(@Header("Authorization") String auth);

    @POST("post")
    Call< Post> guardarPost(@Header("Authorization") String auth, @Body Post post);

    @GET("post/{id}")
    Call< Post> buscarPostId(@Header("Authorization") String auth, @Path("id") int id);

    @GET("post/{id}/comment")
    Call<List<Comment>> buscarComentarios(@Header("Authorization") String auth, @Path("id") int id);

    @POST("post/{id}/comment")
    Call<Comment> guardarComentario(@Header("Authorization") String auth, @Path("id") int id, @Body Comment comment);

    @PUT("post/{id}/like")
    Call darLike(@Header("Authorization") String auth);

    @DELETE("post/{id}/like")
    Call quitarLike(@Header("Authorization") String auth);

}
