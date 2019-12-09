package com.itla.appblog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.itla.appblog.api.ManejadorSesion;
import com.itla.appblog.api.ServicioApi;
import com.itla.appblog.api.interfaces.PostInterface;
import com.itla.appblog.api.modelos.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostActivity extends AppCompatActivity {
    private EditText ettitulo, etpost;
    private Retrofit retrofit;
    private NachoTextView ettags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button btguardarpost = findViewById(R.id.btguardarpost);
        ettitulo = findViewById(R.id.ettitulo);
        etpost = findViewById(R.id.etpost);
        ettags = findViewById(R.id.ettags_post);
        ettags.addChipTerminator(',', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN);
        ettags.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_CURRENT_TOKEN);

        btguardarpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicacionDePost();
            }
        });

    }

    private void publicacionDePost() {
        Post post = new Post();
        post.setTitle(ettitulo.getText().toString());
        post.setBody(etpost.getText().toString());
        post.setUserId(ManejadorSesion.getUserID());
        List<String> listatags = ettags.getChipValues();
        post.setTags(listatags.toArray(new String[listatags.size()]));

        if (post.getTitle().isEmpty() || post.getBody().isEmpty() || listatags.size()==0) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
            return;
        }

        retrofit = ServicioApi.getServicoApi();
        PostInterface postInterface = retrofit.create(PostInterface.class);
        Call<Post> call = postInterface.guardarPost(ManejadorSesion.getToken(), post);


        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.code() == 201) {
                    Toast.makeText(getApplicationContext(), "Post creado!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No se han podido publicar el post", Toast.LENGTH_LONG).show();
            }
        });

    }
}
