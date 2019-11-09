package com.itla.appblog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button btguardarpost = findViewById(R.id.btguardarpost);
        final EditText ettitulo = findViewById(R.id.ettitulo);
        final EditText etpost = findViewById(R.id.etpost);

        btguardarpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
