package com.itla.appblog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.itla.appblog.api.Funciones;
import com.itla.appblog.api.ManejadorSesion;
import com.itla.appblog.api.ServicioApi;
import com.itla.appblog.api.adapter.PostAdapter;
import com.itla.appblog.api.interfaces.PostInterface;
import com.itla.appblog.api.interfaces.UserInterface;
import com.itla.appblog.api.modelos.Post;
import com.itla.appblog.api.modelos.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerfilActivity extends AppCompatActivity {
    private ArrayList<Post> postArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Retrofit retrofit;

    TextView tvnombre, tvemail, tvcantidadpost, tvfecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        retrofit = ServicioApi.getServicoApi();
        recyclerView = findViewById(R.id.rvpost_perfil);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        tvnombre = findViewById(R.id.tvnombre_perfil);
        tvemail = findViewById(R.id.tvemail_perfil);
        tvcantidadpost = findViewById(R.id.tvcantidadpost_perfil);
        tvfecha = findViewById(R.id.tvfecha_creacion_perfil);
        cargarPerfil();
    }

    private void cargarPerfil() {
        UserInterface userInterface = retrofit.create(UserInterface.class);
        Call<User> call = userInterface.buscarUsuarioId(ManejadorSesion.getToken(), ManejadorSesion.getUserID());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    tvnombre.setText(response.body().getName());
                    tvemail.setText(response.body().getEmail());
                    tvcantidadpost.setText(String.valueOf(response.body().getPosts())+" Publicaciones");
                    tvfecha.setText("Creado en " + Funciones.FormatoFechaTexto(new Date(response.body().getCreatedAt())));
                    cargarPost();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No se han podido obtener la informacion del usuario", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarPost() {
        PostInterface postService = retrofit.create(PostInterface.class);
        Call<List<Post>> call = postService.buscarPostPorUsuario(ManejadorSesion.getToken(), ManejadorSesion.getUserID());

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                postArrayList = new ArrayList<Post>(response.body());
                mAdapter = new PostAdapter(postArrayList);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No se han podido obtener los post del usuario", Toast.LENGTH_LONG).show();
            }
        });
    }
}
