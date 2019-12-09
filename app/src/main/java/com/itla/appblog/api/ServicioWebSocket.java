package com.itla.appblog.api;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itla.appblog.ComentariosActivity;
import com.itla.appblog.ListaPost;
import com.itla.appblog.api.modelos.Comment;
import com.itla.appblog.api.modelos.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ServicioWebSocket {
    private Context context;
    private static final String BASE_URL = "http://itla.hectorvent.com/api/";
    public static ArrayList<Post> postArrayList;
    public static ArrayList<Object> commentsArrayList;
    public static RecyclerView.Adapter adapter;


    public ServicioWebSocket(Context context) {
        this.context = context;
        runWebSocktListener();
    }

    public void runWebSocktListener() {
        OkHttpClient clientCoinPrice = new OkHttpClient();
        Request requestCoinPrice = new Request.Builder().url(BASE_URL + "?token=" + ManejadorSesion.getToken_normal()).build();

        WebSocketListener webSocketListenerCoinPrice = new WebSocketListener() {

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                //Log.e(ConstValues.LOG_TAG, "MESSAGE: " + text);
                messageInterpreter(text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                //Log.e("BlogApp", "MESSAGE: " + bytes.hex());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
                webSocket.cancel();
                Log.e("BlogApp", "CLOSE: " + code + " " + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                //TODO: stuff
            }

        };

        clientCoinPrice.newWebSocket(requestCoinPrice, webSocketListenerCoinPrice);
        clientCoinPrice.dispatcher().executorService().shutdown();
    }

    private void messageInterpreter(String message) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(message);
        JsonObject obj = jsonElement.getAsJsonObject();

        String option = obj.get("type").getAsString();
        switch (option) {
            case "logged":
                break;
            case "user-connected":
                break;
            case "likes":
                notifyLikes(obj);
                break;
            case "view-post":
                notifyViews(obj);
                break;
            case "new-comment":
                notifyNewComment(obj);
                break;
            case "new-post":
                notifyNewPost(obj);
                break;
        }
    }

    private void runOnUiThread(String adapterType) {

        if (adapterType == "P") {
            ((ListaPost) context).runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("BlogApp","Entro en el hilo #1");
                    adapter.notifyDataSetChanged();
                    Log.d("BlogApp","Entro en el hilo #2");
                }
            });
        } else {
            ((ComentariosActivity) context).runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, "Hola caracola", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    private Post getPost(JsonObject jsonObject) {
        final int id = Integer.parseInt(jsonObject.get("postId").toString());
        Optional<Post> existPost = postArrayList
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst();
        return existPost.get();
    }

    private void notifyLikes(JsonObject jsonObject) {
        final int likes = Integer.parseInt(jsonObject.get("likes").toString());
        getPost(jsonObject).setLikes(likes);
        runOnUiThread("P");
    }

    private void notifyViews(JsonObject jsonObject) {
        final int views = Integer.parseInt(jsonObject.get("views").toString());
        getPost(jsonObject).setViews(views);
        runOnUiThread("P");
    }

    private void notifyNewPost(JsonObject jsonObject) {
        Post post = new Post();
        post.setUserId(jsonObject.get("userId").getAsInt());
        post.setUserName(jsonObject.get("userName").getAsString());
        post.setUserEmail(jsonObject.get("userEmail").getAsString());
        post.setComments("0");
        post.setLikes(0);
        post.setLiked(false);

        JsonObject object = jsonObject.get("post").getAsJsonObject();
        post.setId(object.get("id").getAsInt());
        post.setTitle(object.get("title").getAsString());
        post.setBody(object.get("body").getAsString());
        post.setCreatedAt(Long.parseLong(object.get("createdAt").toString()));

        postArrayList.add(0, post);

        runOnUiThread("P");
    }

    private void notifyNewComment(JsonObject jsonObject) {
        final String comments = jsonObject.get("comments").getAsString();
        getPost(jsonObject).setComments(comments);
        runOnUiThread("P");

        if (commentsArrayList != null) {
            Comment comment = new Comment();
            comment.setId(jsonObject.get("commendId").getAsInt());
            comment.setPostId(jsonObject.get("postId").getAsInt());
            comment.setUserId(jsonObject.get("userId").getAsInt());
            comment.setUserName(jsonObject.get("userName").getAsString());
            comment.setUserEmail(jsonObject.get("userEmail").getAsString());
            comment.setBody(jsonObject.get("commentBody").getAsString());
            comment.setCreatedAt(Calendar.getInstance().getTimeInMillis());

            commentsArrayList.add(comment);
            runOnUiThread("C");
        }

    }
}
