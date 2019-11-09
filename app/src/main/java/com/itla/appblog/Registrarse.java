package com.itla.appblog;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

            }
        });
    }
}
