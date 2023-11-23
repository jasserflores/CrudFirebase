package com.example.crudfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Iniciar(View view) {
        Intent iniciar = new Intent(Login.this, IniciarSesion.class);
        startActivity(iniciar);
    }

    public void Registrar(View view) {
        Intent registrar = new Intent(Login.this, Registrarse.class);
        startActivity(registrar);
    }
}
