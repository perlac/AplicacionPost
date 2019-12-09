package com.itla.appblog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itla.appblog.api.ServicioApi;
import com.itla.appblog.api.interfaces.SecurityInterface;
import com.itla.appblog.api.modelos.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Registrarse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        Button btguardar = findViewById(R.id.btguardarpost);
        final EditText etnombre = findViewById(R.id.etnombreusuario);
        final EditText etemail = findViewById(R.id.etemail);
        final EditText etcontrasena = findViewById(R.id.etcontrasena);
        final EditText etconfirmacion = findViewById(R.id.etconfirmacion);
        btguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etcontrasena.getText().toString().equals(etconfirmacion.getText().toString())) {
                    User user = new User();
                    user.setEmail(etemail.getText().toString());
                    user.setName(etnombre.getText().toString());
                    user.setPassword(etcontrasena.getText().toString());

                    Retrofit servicioApi = ServicioApi.getServicoApi();
                    SecurityInterface securityInterface = servicioApi.create(SecurityInterface.class);

                    Call<User> userCall = securityInterface.registrarUsuario(user);

                    userCall.enqueue(new Callback<User>() {

                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 201) {
                                Toast.makeText(getApplicationContext(), "Usuario Registrado", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "No se ha podido registrar el usuario", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Contraseña no coinciden", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static class PrincipalActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_principal);
        }
    }
}

