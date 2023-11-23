package com.example.crudfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {

    //mqtt://rainboa742:RMeoI1yI2mvsHRMP@rainboa742.cloud.shiftr.io

    RecyclerView recyclerView;

    MainAdapter mainAdapter;

    FloatingActionButton floatingActionButton;

    static String MQTTHOST = "tcp://crudfirebase.cloud.shiftr.io:1883";
    static String MQTTUSER = "rainboa742";
    static String MQTTPASS = "RMeoI1yI2mvsHRMP";
    MqttAndroidClient cliente;
    MqttConnectOptions opciones;
    Boolean permisoPublicar;
    String clientID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectBroker();


        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Inventario"),MainModel.class)
                .build();

        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Agregar.class));
            }
        });
    }

    @Override
    protected  void onStart(){
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mainAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.buscador, menu);
        MenuItem item = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) item.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query){
                txtSearch(query);
                return false;
            }
        });
        return  super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str){
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Inventario").orderByChild("version")
                        .startAt(str).endAt(str+"-"),MainModel.class)
                .build();

        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }

    private void checkConnection(){
        if (this.cliente.isConnected()){
            this.permisoPublicar = true;
        }else{
            this.permisoPublicar = false;
            connectBroker();
        }
    }
    private void connectBroker(){
        this.cliente = new MqttAndroidClient(this.getApplicationContext(),MQTTHOST,this.clientID);
        this.opciones = new MqttConnectOptions();
        this.opciones.setUserName(MQTTUSER);
        this.opciones.setPassword(MQTTPASS.toCharArray());
        try{
            IMqttToken token = this.cliente.connect(opciones);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getBaseContext(), "Conectado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getBaseContext(), "Coneccion Fallida", Toast.LENGTH_SHORT).show();

                }
            });

        }catch (MqttException e){
            e.printStackTrace();
        }
    }
}