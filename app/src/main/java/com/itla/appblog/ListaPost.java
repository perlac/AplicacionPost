package com.itla.appblog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.itla.appblog.api.ManejadorSesion;
import com.itla.appblog.api.ServicioApi;
import com.itla.appblog.api.adapter.PostAdapter;
import com.itla.appblog.api.interfaces.PostInterface;
import com.itla.appblog.api.interfaces.SecurityInterface;
import com.itla.appblog.api.modelos.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListaPost extends AppCompatActivity {
    private ArrayList<Post> postArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_post);

        retrofit = ServicioApi.getServicoApi();
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton btnpost = findViewById(R.id.btnnuevopost);
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        ImageButton btncerrarsesion = findViewById(R.id.btncerrarsesion_top);
        btncerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Cerrar Sesion")
                        .setMessage("Seguro que desea cerrar sesion?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cerrarSesion();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        ImageButton btnperfil = findViewById(R.id.btnaccion_top);
        btnperfil.setImageResource(R.drawable.account);
        btnperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PerfilActivity.class);
                startActivity(intent);
            }
        });

        cargarPosts();
    }

    private void cargarPosts() {
        PostInterface postService = retrofit.create(PostInterface.class);
        Call<List<Post>> call = postService.buscarPosts(ManejadorSesion.getToken());

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                postArrayList = new ArrayList<Post>(response.body());
                Collections.sort(postArrayList, new Comparator<Post>() {
                    @Override
                    public int compare(Post lhs, Post rhs) {
                        return rhs.getId() - lhs.getId();
                    }
                });
                mAdapter = new PostAdapter(postArrayList);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No se han podido obtener los post", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cerrarSesion() {
        SecurityInterface securityInterface = retrofit.create(SecurityInterface.class);
        Call<ResponseBody> call = securityInterface.cerrarSesion(ManejadorSesion.getToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    ManejadorSesion manejadorSesion = new ManejadorSesion(getApplicationContext());
                    manejadorSesion.removerSession("tkn");

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Error al cerrar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cargarPosts();
    }
}
