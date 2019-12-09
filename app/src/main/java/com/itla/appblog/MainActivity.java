package com.itla.appblog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itla.appblog.api.ManejadorSesion;
import com.itla.appblog.api.ServicioApi;
import com.itla.appblog.api.interfaces.SecurityInterface;
import com.itla.appblog.api.modelos.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    User login;
    private String token;
    private Retrofit retrofit;
    ManejadorSesion manejadorSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manejadorSesion = new ManejadorSesion(getApplicationContext());
        if (!manejadorSesion.getSesion("tkn").isEmpty()) {
            manejadorSesion.setUserID(Integer.parseInt(manejadorSesion.getSesion("usr")));
            manejadorSesion.setToken(manejadorSesion.getSesion("tkn"));
            ManejadorSesion.setToken_normal(manejadorSesion.getSesion("tkn").replace("Bearer ", ""));
            Intent intento = new Intent(getApplicationContext(), ListaPost.class);
            startActivity(intento);
            finish();
        }

        Button btregistrar = findViewById(R.id.btregistrarse);
        btregistrar.requestFocus();
        btregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Registrarse.class);
                startActivity(intent);
            }

        });

        Button btlogin = findViewById(R.id.btlogin);
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrofit = ServicioApi.getServicoApi();
                final EditText etusuario = findViewById(R.id.etusuario);
                final EditText etcontra = findViewById(R.id.etcontrasena);

                login = new User();
                login.setId(0);
                login.setName("");
                login.setEmail(etusuario.getText().toString());
                login.setPassword(etcontra.getText().toString());
                login.setToken("");

                SecurityInterface securityInterface = retrofit.create(SecurityInterface.class);
                Call<User> userCall = securityInterface.iniciarSesion(login);

                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 201) {
                            token = response.body().getToken();
                            manejadorSesion.setSesion("tkn", "Bearer " + token);
                            manejadorSesion.setSesion("usr", String.valueOf(response.body().getId()));
                            manejadorSesion.setUserID(response.body().getId());
                            manejadorSesion.setToken("Bearer " + token);
                            ManejadorSesion.setToken_normal(token);

                            Intent intent = new Intent(getApplicationContext(), ListaPost.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Credenciales inválidas", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Ocurrió un errror de comunicación", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}
