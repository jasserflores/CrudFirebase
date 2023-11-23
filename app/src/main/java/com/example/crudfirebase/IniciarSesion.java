package com.example.crudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesion extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText correo, pass;
    private TextView tvBienvenida; // Nuevo TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        correo = findViewById(R.id.et_emailInicio);
        pass = findViewById(R.id.et_passwordInicio);
        mAuth = FirebaseAuth.getInstance();
        tvBienvenida = findViewById(R.id.tv_bienvenida); // Inicializar el nuevo TextView
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void IniciarSesion(View view) {
        mAuth.signInWithEmailAndPassword(correo.getText().toString(), pass.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Ha ingresado correctamente", Toast.LENGTH_SHORT).show();


                    String nombreUsuario = correo.getText().toString();


                    tvBienvenida.setText("Bienvenido " + nombreUsuario);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent iniciar = new Intent(IniciarSesion.this, MainActivity.class);
                            startActivity(iniciar);
                            FirebaseUser user = mAuth.getCurrentUser();
                        }
                    }, 2000);
                } else {
                    Toast.makeText(getApplicationContext(), "Autorización fallida", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
