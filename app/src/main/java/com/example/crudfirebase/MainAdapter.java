package com.example.crudfirebase;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull MainModel model) {

        //final int itemPosition = position;

        holder.version.setText(model.getVersion());
        holder.motor.setText(model.getMotor());
        holder.color.setText(model.getColor());
        holder.marca.setText(model.getMarca());

        Glide.with(holder.img.getContext())
                .load(model.getImgURL())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);


        holder.editar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.version.getContext())
                        .setContentHolder(new ViewHolder(R.layout.ventana_emergente))
                        .setExpanded(true,1200)
                        .create();
                View view = dialogPlus.getHolderView();
                EditText version = view.findViewById(R.id.VersionText);
                EditText motor = view.findViewById(R.id.MotorText);
                EditText color = view.findViewById(R.id.ColorText);
                EditText marca = view.findViewById(R.id.MarcaText);
                EditText imgURL = view.findViewById(R.id.img);

                Button actualizar = view.findViewById(R.id.btn_actualizar);

                version.setText(model.getVersion());
                motor.setText(model.getMotor());
                color.setText(model.getColor());
                marca.setText(model.getMarca());
                imgURL.setText(model.getImgURL());


                dialogPlus.show();



                actualizar.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Map<String,Object> map = new HashMap<>();
                        map.put("Version", version.getText().toString());
                        map.put("Motor", motor.getText().toString());
                        map.put("Color", color.getText().toString());
                        map.put("Marca", marca.getText().toString());
                        map.put("imgURL", imgURL.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Inventario")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.version.getContext(),"Actualizacion Correcta", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.version.getContext(), "Error en la Actualzación", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });

                    }
                });


            }

        });


        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.version.getContext());
                builder.setTitle("Estas seguro que quieres eliminarlo");
                builder.setMessage("Eliminado");

                builder.setPositiveButton("Eliminado", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Inventario")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.version.getContext(),"cancelar", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;

        TextView version, motor, color, marca;

        Button editar, eliminar;

        public myViewHolder(@NonNull View itemView){
            super(itemView);

            img = itemView.findViewById(R.id.img);
            version = itemView.findViewById(R.id.VersionText);
            motor = itemView.findViewById(R.id.MotorText);
            color = itemView.findViewById(R.id.ColorText);
            marca = itemView.findViewById(R.id.MarcaText);
            editar = itemView.findViewById(R.id.btn_edit);
            eliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }

}
