package com.example.crudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Agregar extends AppCompatActivity {

    EditText version, motor, color, marca, imgURL;

    Button btnAdd, btnAtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        version = findViewById(R.id.VersionText);
        motor = findViewById(R.id.MotorText);
        color = findViewById(R.id.ColorText);
        marca = findViewById(R.id.MarcaText);
        imgURL = findViewById(R.id.img);

        btnAdd = findViewById(R.id.btn_agregar);
        btnAtras = findViewById(R.id.btn_atras);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarDatos();
            }
        });
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void insertarDatos(){
        Map<String,Object> map =new HashMap<>();
        map.put("Version", version.getText().toString());
        map.put("Motor", motor.getText().toString());
        map.put("Color", color.getText().toString());
        map.put("Marca", marca.getText().toString());
        map.put("imgURL", imgURL.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Inventario").push()
                .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Agregar.this,"Insertado Correctamente", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Agregar.this, "Error al Insertar",Toast.LENGTH_SHORT).show();
                    }
                });

    }
}