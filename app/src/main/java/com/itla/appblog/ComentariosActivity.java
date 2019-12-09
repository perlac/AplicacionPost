package com.itla.appblog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.itla.appblog.api.ManejadorSesion;
import com.itla.appblog.api.ServicioApi;
import com.itla.appblog.api.ServicioWebSocket;
import com.itla.appblog.api.adapter.ComentarioAdapter;
import com.itla.appblog.api.interfaces.PostInterface;
import com.itla.appblog.api.modelos.Comment;
import com.itla.appblog.api.modelos.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ComentariosActivity extends AppCompatActivity {
    private ArrayList<Object> commentArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Retrofit retrofit;
    private int idPost;
    PostInterface postService;
    EditText etComment;
    Post post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        idPost = getIntent().getIntExtra("idPost",0);
        retrofit = ServicioApi.getServicoApi();
        postService = retrofit.create(PostInterface.class);

        recyclerView=findViewById(R.id.rvcomentarios);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final ImageButton btnenviar = findViewById(R.id.btnenviar_comentario);
        etComment = findViewById(R.id.etcomentario);

        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment(etComment.getText().toString());
            }
        });
        getPost();
        new ServicioWebSocket(this);
    }


    private void getPost() {
        Call<Post> call = postService.buscarPostId(ManejadorSesion.getToken(),idPost);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.code()==200){
                    post = response.body();

                    getComments();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No se han podido obtener los post", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void getComments() {
        Call<List<Comment>> call = postService.buscarComentarios(ManejadorSesion.getToken(),idPost);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                commentArrayList.clear();
                commentArrayList.add(post);
                commentArrayList.addAll(response.body());
                mAdapter = new ComentarioAdapter(commentArrayList);
                recyclerView.setAdapter(mAdapter);

                ServicioWebSocket.commentsArrayList=commentArrayList;
                ServicioWebSocket.adapter=mAdapter;
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No se han podido obtener los comentarios", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addComment(String comment){
        Comment objcomment = new Comment();
        objcomment.setBody(comment);
        Call<Comment> call = postService.guardarComentario(ManejadorSesion.getToken(), idPost,objcomment);

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(response.code()== 201){
                    //getComments();
                    etComment.setText("");
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No se han podido agregar el comentario", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }
}
